--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- Name: apply_formula_to_details(integer, timestamp without time zone); Type: FUNCTION; Schema: public; Owner: ienergy
--

CREATE FUNCTION apply_formula_to_details(measure_drain_id integer, measure_time timestamp without time zone) RETURNS void
    LANGUAGE plpgsql
    AS $$	
	DECLARE
		formula_id integer;
		formula_name text;
		formula_result numeric;
		formula_last_time timestamp;
		formula_count integer;
		formula_count_actual integer;
	BEGIN
		RAISE NOTICE 'apply formula to drain %', measure_drain_id;
	
		-- get all drain formula definition  
		FOR formula_id, formula_name, formula_count IN SELECT dd.id, drain, count(*) FROM da_drain_descriptor dd, da_drain_formula df
			WHERE dd.id = df.drain_id  AND dd.id IN (SELECT drain_id FROM da_drain_formula, da_drain_descriptor dd WHERE  drain_argument_id = dd.id AND dd.id = measure_drain_id) GROUP BY dd.id LOOP
			RAISE NOTICE '--> found drain with formula id % and name % (%)', formula_id, formula_name, formula_count;

			-- now get last update time of formula result 
			SELECT MAX("time") INTO formula_last_time FROM da_measure_detail WHERE drain_id = formula_id;

			IF formula_last_time IS NULL THEN
				formula_last_time = TIMESTAMP '1970-01-01 00:00:00';
			END IF;

			RAISE NOTICE 'Last measure found at %', formula_last_time;

			-- check number of measure available
			SELECT COUNT(1) INTO formula_count_actual FROM da_measure_detail detail
			INNER JOIN (SELECT MAX(time) AS max_time, formula.drain_argument_id AS args_drain_id FROM da_measure_detail detail, da_drain_formula formula
			WHERE detail.drain_id = formula.drain_argument_id AND formula.drain_id = formula_id AND time >  formula_last_time 
			GROUP BY formula.drain_argument_id) AS last_time_table ON last_time_table.args_drain_id = detail.drain_id AND last_time_table.max_time = detail.time;
	
			RAISE NOTICE 'formula_count_actual % formula_count %', formula_count_actual, formula_count;

			IF formula_count = formula_count_actual THEN 
				RAISE NOTICE '--> compute formula for %', formula_name;

				-- compute formula 
				SELECT SUM("value"*args_sign) INTO formula_result FROM da_measure_detail detail INNER JOIN 
				(SELECT MAX(time) AS max_time, formula.drain_argument_id AS args_drain_id, formula.sign AS args_sign FROM da_measure_detail detail, da_drain_formula formula
				WHERE detail.drain_id = formula.drain_argument_id AND formula.drain_id = formula_id AND time >  formula_last_time GROUP BY formula.drain_argument_id, formula.sign) AS last_time_table
				ON last_time_table.args_drain_id = detail.drain_id AND last_time_table.max_time = detail.time;

				RAISE NOTICE '--> formula result %', formula_result;

				-- save the result 
				-- insert the data in the database 
				RAISE NOTICE 'Insert measure to detail for % and value % INSERT DATA', formula_name, formula_result;
				INSERT INTO da_measure_detail("time", value, drain_id) VALUES (measure_time, formula_result, formula_id);

				RETURN;
			END IF;
		END LOOP;
	
	END;
	
	$$;


ALTER FUNCTION public.apply_formula_to_details(measure_drain_id integer, measure_time timestamp without time zone) OWNER TO ienergy;

--
-- Name: apply_formula_to_rt(integer, timestamp without time zone); Type: FUNCTION; Schema: public; Owner: ienergy
--

CREATE FUNCTION apply_formula_to_rt(measure_drain_id integer, measure_time timestamp without time zone) RETURNS void
    LANGUAGE plpgsql
    AS $$
	
	DECLARE
		formula_id integer;
		formula_name text;
		formula_result numeric;
		formula_last_time timestamp;
		formula_count integer;
		formula_count_actual integer;
	BEGIN
		RAISE NOTICE 'apply formula to drain %', measure_drain_id;
	
		-- get all drain formula definition  
		FOR formula_id, formula_name, formula_count IN SELECT dd.id, drain, count(*) FROM da_drain_descriptor dd, da_drain_formula df
			WHERE dd.id = df.drain_id  AND dd.id IN (SELECT drain_id FROM da_drain_formula, da_drain_descriptor dd WHERE  drain_argument_id = dd.id AND dd.id = measure_drain_id) GROUP BY dd.id LOOP
			RAISE NOTICE '--> found drain with formula id % and name % (%)', formula_id, formula_name, formula_count;
	
			-- now get last update time of formula result 
			SELECT "time" INTO formula_last_time FROM da_measure_rt WHERE drain_id = formula_id;
	
			IF formula_last_time IS NOT NULL THEN
				RAISE NOTICE 'Last measure found';
				-- check number of measure available
				select COUNT(1) INTO formula_count_actual from da_measure_rt rt, da_drain_formula formula WHERE rt.drain_id = formula.drain_argument_id AND formula.drain_id = formula_id AND time >  formula_last_time;

				if (formula_count_actual <> formula_count) THEN 
					RETURN;
				END IF;
			END IF;
	
			-- now apply the formula 
			SELECT SUM("value"*sign) INTO formula_result FROM da_measure_rt rt, da_drain_formula df WHERE rt.drain_id = df.drain_argument_id AND df.drain_id = formula_id;
			RAISE NOTICE '---> result %', formula_result;
	
			-- insert the data in the database 
			IF formula_last_time IS NULL THEN
				RAISE NOTICE 'No last measure found INSERT DATA';
				INSERT INTO da_measure_rt("time", value, drain_id) VALUES (measure_time, formula_result, formula_id);
			ELSE
				RAISE NOTICE 'Update the data of formula';
				UPDATE da_measure_rt SET "time"  = measure_time, value = formula_result WHERE drain_id = formula_id;
			END IF;
	
		END LOOP ;
	
	END;
	
	$$;


ALTER FUNCTION public.apply_formula_to_rt(measure_drain_id integer, measure_time timestamp without time zone) OWNER TO ienergy;

--
-- Name: handle_measure_formula_detail(); Type: FUNCTION; Schema: public; Owner: ienergy
--

CREATE FUNCTION handle_measure_formula_detail() RETURNS trigger
    LANGUAGE plpgsql
    AS $$    BEGIN
	PERFORM apply_formula_to_details(NEW.drain_id, NEW.time);
        RETURN NEW;
    END;
$$;


ALTER FUNCTION public.handle_measure_formula_detail() OWNER TO ienergy;

--
-- Name: handle_measure_formula_rt(); Type: FUNCTION; Schema: public; Owner: ienergy
--

CREATE FUNCTION handle_measure_formula_rt() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    BEGIN
	PERFORM apply_formula_to_rt(NEW.drain_id, NEW.time);
        RETURN NEW;
    END;
$$;


ALTER FUNCTION public.handle_measure_formula_rt() OWNER TO ienergy;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: da_confort_indicator; Type: TABLE; Schema: public; Owner: ienergy; Tablespace: 
--

CREATE TABLE da_confort_indicator (
    id integer NOT NULL,
    description character varying(255),
    function character varying(255),
    name character varying(255) NOT NULL,
    type character varying(255)
);


ALTER TABLE public.da_confort_indicator OWNER TO ienergy;

--
-- Name: da_confort_indicator_id_seq; Type: SEQUENCE; Schema: public; Owner: ienergy
--

CREATE SEQUENCE da_confort_indicator_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.da_confort_indicator_id_seq OWNER TO ienergy;

--
-- Name: da_confort_indicator_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ienergy
--

ALTER SEQUENCE da_confort_indicator_id_seq OWNED BY da_confort_indicator.id;


--
-- Name: da_confort_vote; Type: TABLE; Schema: public; Owner: ienergy; Tablespace: 
--

CREATE TABLE da_confort_vote (
    id integer NOT NULL,
    "time" timestamp without time zone,
    value integer NOT NULL,
    indicator_id integer NOT NULL
);


ALTER TABLE public.da_confort_vote OWNER TO ienergy;

--
-- Name: da_confort_vote_id_seq; Type: SEQUENCE; Schema: public; Owner: ienergy
--

CREATE SEQUENCE da_confort_vote_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.da_confort_vote_id_seq OWNER TO ienergy;

--
-- Name: da_confort_vote_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ienergy
--

ALTER SEQUENCE da_confort_vote_id_seq OWNED BY da_confort_vote.id;


--
-- Name: da_drain_descriptor; Type: TABLE; Schema: public; Owner: ienergy; Tablespace: 
--

CREATE TABLE da_drain_descriptor (
    id integer NOT NULL,
    aggregationfunction character varying(255),
    description character varying(255),
    drain character varying(255) NOT NULL,
    historyfunction character varying(10) DEFAULT 'AVG'::character varying NOT NULL,
    keephistory boolean DEFAULT false,
    "real" boolean DEFAULT true,
    unit character varying(255)
);


ALTER TABLE public.da_drain_descriptor OWNER TO ienergy;

--
-- Name: da_drain_descriptor_id_seq; Type: SEQUENCE; Schema: public; Owner: ienergy
--

CREATE SEQUENCE da_drain_descriptor_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.da_drain_descriptor_id_seq OWNER TO ienergy;

--
-- Name: da_drain_descriptor_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ienergy
--

ALTER SEQUENCE da_drain_descriptor_id_seq OWNED BY da_drain_descriptor.id;


--
-- Name: da_drain_formula; Type: TABLE; Schema: public; Owner: ienergy; Tablespace: 
--

CREATE TABLE da_drain_formula (
    id integer NOT NULL,
    sign integer NOT NULL,
    drain_argument_id integer NOT NULL,
    drain_id integer NOT NULL
);


ALTER TABLE public.da_drain_formula OWNER TO ienergy;

--
-- Name: da_drain_formula_id_seq; Type: SEQUENCE; Schema: public; Owner: ienergy
--

CREATE SEQUENCE da_drain_formula_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.da_drain_formula_id_seq OWNER TO ienergy;

--
-- Name: da_drain_formula_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: ienergy
--

ALTER SEQUENCE da_drain_formula_id_seq OWNED BY da_drain_formula.id;


--
-- Name: da_measure_detail; Type: TABLE; Schema: public; Owner: ienergy; Tablespace: 
--

CREATE TABLE da_measure_detail (
    "time" timestamp without time zone NOT NULL,
    value numeric(19,2),
    drain_id integer NOT NULL
);


ALTER TABLE public.da_measure_detail OWNER TO ienergy;

--
-- Name: da_measure_history; Type: TABLE; Schema: public; Owner: ienergy; Tablespace: 
--

CREATE TABLE da_measure_history (
    "time" timestamp without time zone NOT NULL,
    value numeric(19,2),
    drain_id integer NOT NULL
);


ALTER TABLE public.da_measure_history OWNER TO ienergy;

--
-- Name: da_measure_rt; Type: TABLE; Schema: public; Owner: ienergy; Tablespace: 
--

CREATE TABLE da_measure_rt (
    "time" timestamp without time zone NOT NULL,
    value numeric(19,2),
    drain_id integer NOT NULL
);


ALTER TABLE public.da_measure_rt OWNER TO ienergy;

--
-- Name: diagnostic_missed_data; Type: TABLE; Schema: public; Owner: ienergy; Tablespace: 
--

CREATE TABLE diagnostic_missed_data (
    year integer NOT NULL,
    month integer NOT NULL,
    minute integer NOT NULL,
    hour integer NOT NULL,
    drain character varying(255) NOT NULL,
    day integer NOT NULL
);


ALTER TABLE public.diagnostic_missed_data OWNER TO ienergy;

--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ienergy
--

ALTER TABLE ONLY da_confort_indicator ALTER COLUMN id SET DEFAULT nextval('da_confort_indicator_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ienergy
--

ALTER TABLE ONLY da_confort_vote ALTER COLUMN id SET DEFAULT nextval('da_confort_vote_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ienergy
--

ALTER TABLE ONLY da_drain_descriptor ALTER COLUMN id SET DEFAULT nextval('da_drain_descriptor_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: ienergy
--

ALTER TABLE ONLY da_drain_formula ALTER COLUMN id SET DEFAULT nextval('da_drain_formula_id_seq'::regclass);


--
-- Name: da_confort_indicator_pkey; Type: CONSTRAINT; Schema: public; Owner: ienergy; Tablespace: 
--

ALTER TABLE ONLY da_confort_indicator
    ADD CONSTRAINT da_confort_indicator_pkey PRIMARY KEY (id);


--
-- Name: da_confort_vote_pkey; Type: CONSTRAINT; Schema: public; Owner: ienergy; Tablespace: 
--

ALTER TABLE ONLY da_confort_vote
    ADD CONSTRAINT da_confort_vote_pkey PRIMARY KEY (id);


--
-- Name: da_drain_descriptor_pkey; Type: CONSTRAINT; Schema: public; Owner: ienergy; Tablespace: 
--

ALTER TABLE ONLY da_drain_descriptor
    ADD CONSTRAINT da_drain_descriptor_pkey PRIMARY KEY (id);


--
-- Name: da_drain_formula_pkey; Type: CONSTRAINT; Schema: public; Owner: ienergy; Tablespace: 
--

ALTER TABLE ONLY da_drain_formula
    ADD CONSTRAINT da_drain_formula_pkey PRIMARY KEY (id);


--
-- Name: da_measure_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: ienergy; Tablespace: 
--

ALTER TABLE ONLY da_measure_detail
    ADD CONSTRAINT da_measure_detail_pkey PRIMARY KEY ("time", drain_id);


--
-- Name: da_measure_history_pkey; Type: CONSTRAINT; Schema: public; Owner: ienergy; Tablespace: 
--

ALTER TABLE ONLY da_measure_history
    ADD CONSTRAINT da_measure_history_pkey PRIMARY KEY ("time", drain_id);


--
-- Name: da_measure_rt_pkey; Type: CONSTRAINT; Schema: public; Owner: ienergy; Tablespace: 
--

ALTER TABLE ONLY da_measure_rt
    ADD CONSTRAINT da_measure_rt_pkey PRIMARY KEY ("time", drain_id);


--
-- Name: diagnostic_missed_data_pkey; Type: CONSTRAINT; Schema: public; Owner: ienergy; Tablespace: 
--

ALTER TABLE ONLY diagnostic_missed_data
    ADD CONSTRAINT diagnostic_missed_data_pkey PRIMARY KEY (year, month, minute, hour, drain, day);


--
-- Name: uk_729ncj9vjykl7ckgg7sleh6qg; Type: CONSTRAINT; Schema: public; Owner: ienergy; Tablespace: 
--

ALTER TABLE ONLY da_confort_indicator
    ADD CONSTRAINT uk_729ncj9vjykl7ckgg7sleh6qg UNIQUE (name);


--
-- Name: uk_7y7ldedpo4rejyx8nvo3h5btf; Type: CONSTRAINT; Schema: public; Owner: ienergy; Tablespace: 
--

ALTER TABLE ONLY da_drain_formula
    ADD CONSTRAINT uk_7y7ldedpo4rejyx8nvo3h5btf UNIQUE (drain_id, drain_argument_id);


--
-- Name: uk_nm75p8ksi9sllq8lbcy63tysc; Type: CONSTRAINT; Schema: public; Owner: ienergy; Tablespace: 
--

ALTER TABLE ONLY da_drain_descriptor
    ADD CONSTRAINT uk_nm75p8ksi9sllq8lbcy63tysc UNIQUE (drain);


--
-- Name: new_measure_detail; Type: TRIGGER; Schema: public; Owner: ienergy
--

CREATE TRIGGER new_measure_detail AFTER INSERT ON da_measure_detail FOR EACH ROW EXECUTE PROCEDURE handle_measure_formula_detail();


--
-- Name: new_rt_measure; Type: TRIGGER; Schema: public; Owner: ienergy
--

CREATE TRIGGER new_rt_measure AFTER UPDATE ON da_measure_rt FOR EACH ROW EXECUTE PROCEDURE handle_measure_formula_rt();


--
-- Name: fk_1l1b6xjd8cjj1il54j3c0gnnh; Type: FK CONSTRAINT; Schema: public; Owner: ienergy
--

ALTER TABLE ONLY da_measure_detail
    ADD CONSTRAINT fk_1l1b6xjd8cjj1il54j3c0gnnh FOREIGN KEY (drain_id) REFERENCES da_drain_descriptor(id);


--
-- Name: fk_74rkv3fcn6dclxjdden9idiu2; Type: FK CONSTRAINT; Schema: public; Owner: ienergy
--

ALTER TABLE ONLY da_drain_formula
    ADD CONSTRAINT fk_74rkv3fcn6dclxjdden9idiu2 FOREIGN KEY (drain_argument_id) REFERENCES da_drain_descriptor(id);


--
-- Name: fk_ed1jeqqucjw8yvljj1ua6f7sg; Type: FK CONSTRAINT; Schema: public; Owner: ienergy
--

ALTER TABLE ONLY da_measure_rt
    ADD CONSTRAINT fk_ed1jeqqucjw8yvljj1ua6f7sg FOREIGN KEY (drain_id) REFERENCES da_drain_descriptor(id);


--
-- Name: fk_gtslhueu037t3kd48wcs42mme; Type: FK CONSTRAINT; Schema: public; Owner: ienergy
--

ALTER TABLE ONLY da_confort_vote
    ADD CONSTRAINT fk_gtslhueu037t3kd48wcs42mme FOREIGN KEY (indicator_id) REFERENCES da_confort_indicator(id);


--
-- Name: fk_jvpwqg4e7k41p48biraas5jkn; Type: FK CONSTRAINT; Schema: public; Owner: ienergy
--

ALTER TABLE ONLY da_drain_formula
    ADD CONSTRAINT fk_jvpwqg4e7k41p48biraas5jkn FOREIGN KEY (drain_id) REFERENCES da_drain_descriptor(id);


--
-- Name: fk_td2arpqwtea6ntdsthpv8ajix; Type: FK CONSTRAINT; Schema: public; Owner: ienergy
--

ALTER TABLE ONLY da_measure_history
    ADD CONSTRAINT fk_td2arpqwtea6ntdsthpv8ajix FOREIGN KEY (drain_id) REFERENCES da_drain_descriptor(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--
