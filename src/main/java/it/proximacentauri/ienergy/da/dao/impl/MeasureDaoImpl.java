/*******************************************************************************
 * Copyright (c) 2014 Proxima Centauri SRL <info@proxima-centauri.it>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Proxima Centauri SRL <info@proxima-centauri.it> - initial API and implementation
 ******************************************************************************/
package it.proximacentauri.ienergy.da.dao.impl;

import it.proximacentauri.ienergy.da.dao.MeasureDao;
import it.proximacentauri.ienergy.da.domain.BaseMeasure;
import it.proximacentauri.ienergy.da.domain.DrainDescriptor;
import it.proximacentauri.ienergy.da.domain.MeasureAggegation;
import it.proximacentauri.ienergy.da.domain.MeasureDetail;
import it.proximacentauri.ienergy.da.domain.MeasureRealTime;
import it.proximacentauri.ienergy.da.domain.TimeAggregation;
import it.proximacentauri.ienergy.da.util.Pair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeasureDaoImpl extends BaseDaoImpl implements MeasureDao {

	final private Logger log = LoggerFactory.getLogger(MeasureDaoImpl.class);

	final static private Map<TimeAggregation, String> queryMap = new HashMap<TimeAggregation, String>();

	static {

		// year
		queryMap.put(
				TimeAggregation.YEAR,
				"SELECT :aggregate:(measure.value), YEAR(measure.time) FROM :classType: measure WHERE measure.desc.id = :drain_id AND measure.time BETWEEN :startDate AND :endDate GROUP BY YEAR(measure.time) ORDER BY YEAR(measure.time)");

		// month
		queryMap.put(
				TimeAggregation.MONTH,
				"SELECT :aggregate:(measure.value), YEAR(measure.time), MONTH(measure.time) FROM :classType: measure WHERE measure.desc.id = :drain_id AND measure.time BETWEEN :startDate AND :endDate GROUP BY YEAR(measure.time),  MONTH(measure.time) ORDER BY YEAR(measure.time),  MONTH(measure.time)");

		// month
		queryMap.put(
				TimeAggregation.DAY,
				"SELECT :aggregate:(measure.value), YEAR(measure.time), MONTH(measure.time), DAY(measure.time) FROM :classType: measure WHERE measure.desc.id = :drain_id AND measure.time BETWEEN :startDate AND :endDate GROUP BY YEAR(measure.time),  MONTH(measure.time), DAY(measure.time) ORDER BY YEAR(measure.time),  MONTH(measure.time), DAY(measure.time)");

		// hour
		queryMap.put(
				TimeAggregation.HOUR,
				"SELECT :aggregate:(measure.value), YEAR(measure.time), MONTH(measure.time), DAY(measure.time), HOUR(measure.time) FROM :classType: measure WHERE measure.desc.id = :drain_id AND measure.time BETWEEN :startDate AND :endDate GROUP BY YEAR(measure.time),  MONTH(measure.time), DAY(measure.time), HOUR(measure.time) ORDER BY YEAR(measure.time),  MONTH(measure.time), DAY(measure.time), HOUR(measure.time)");

		// nome aggregation
		queryMap.put(
				TimeAggregation.MINUTE,
				"SELECT measure.value, measure.time FROM :classType: measure WHERE measure.desc.id = :drain_id AND measure.time BETWEEN :startDate AND :endDate ORDER BY measure.time");
	}

	@Override
	public Pair<MeasureRealTime, DrainDescriptor> findLastMeasureRealTimePair(String name) {
		log.debug("find real time measure for drain {}", name);
		Query q = em.createQuery("SELECT rt, d FROM MeasureRealTime rt JOIN rt.desc d WHERE d.drain = :name");

		q.setParameter("name", name);

		@SuppressWarnings("unchecked")
		final List<Object[]> list = q.getResultList();

		// check if the list contains elements
		if (list.isEmpty())
			return new Pair<MeasureRealTime, DrainDescriptor>();

		// load the data
		final MeasureRealTime measureRealTime = (MeasureRealTime) list.get(0)[0];
		final DrainDescriptor descriptor = (DrainDescriptor) list.get(0)[1];

		return new Pair<MeasureRealTime, DrainDescriptor>(measureRealTime, descriptor);
	}

	@Override
	public MeasureDetail saveMeasureDetail(MeasureDetail detail) {
		log.debug("save measure detail {}", detail);
		em.persist(detail);
		return detail;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<BaseMeasure> findMeasure(DrainDescriptor desc, Date start, Date end, TimeAggregation timeAggregation,
			Class<? extends BaseMeasure> measureClass, MeasureAggegation measureArregation) {
		log.debug("start selection of measure {} start: {} end: {} time aggregation: {} class: {} measureAggregation {}", desc, start, end,
				timeAggregation, measureClass, measureArregation);

		// default aggregation level
		String queryString = queryMap.get(TimeAggregation.MINUTE);

		// select the aggregation function
		if (queryMap.containsKey(timeAggregation)) {
			queryString = queryMap.get(timeAggregation);
		}

		// replace the class type
		queryString = queryString.replaceFirst(":classType:", measureClass.getSimpleName());
		// replace the aggregate
		if (measureArregation != null) {
			queryString = queryString.replaceFirst(":aggregate:", measureArregation.toString().toUpperCase());
		} else if (desc.getAggregationFunction() != null) {
			queryString = queryString.replaceFirst(":aggregate:", desc.getAggregationFunction().toString().toUpperCase());
		} else {
			queryString = queryString.replaceFirst(":aggregate:", "AVG");
		}

		log.debug(" template query selected is {}", queryString);

		Query query = em.createQuery(queryString);

		// set drain id
		query.setParameter("drain_id", desc.getId());

		// add start date and end date
		query.setParameter("startDate", start, TemporalType.TIMESTAMP);
		query.setParameter("endDate", end, TemporalType.TIMESTAMP);

		List<Object[]> resultList = query.getResultList();

		final List<BaseMeasure> measures = new ArrayList<BaseMeasure>();

		// create calendar
		final Calendar calendar = Calendar.getInstance();

		for (Object[] arrayObjects : resultList) {

			try {
				BaseMeasure measure = measureClass.newInstance();

				// first step, load value
				if (arrayObjects[0] instanceof Double) {
					measure.setValue(new BigDecimal((Double) arrayObjects[0]));
				} else {
					measure.setValue((BigDecimal) arrayObjects[0]);
				}

				// handle time
				switch (timeAggregation) {
				// minute
				case MINUTE:
					measure.setTime((Date) arrayObjects[1]);
					break;
				// hour
				case HOUR:
					// set time
					calendar.set(Calendar.YEAR, (int) arrayObjects[1]);
					calendar.set(Calendar.MONTH, (int) arrayObjects[2] - 1);
					calendar.set(Calendar.DAY_OF_MONTH, (int) arrayObjects[3]);
					calendar.set(Calendar.HOUR_OF_DAY, (int) arrayObjects[4]);

					measure.setTime(calendar.getTime());
					break;
				case DAY:
					// set time
					calendar.set(Calendar.YEAR, (int) arrayObjects[1]);
					calendar.set(Calendar.MONTH, (int) arrayObjects[2] - 1);
					calendar.set(Calendar.DAY_OF_MONTH, (int) arrayObjects[3]);

					measure.setTime(calendar.getTime());
					break;
				case MONTH:
					calendar.set(Calendar.YEAR, (int) arrayObjects[1]);
					calendar.set(Calendar.MONTH, (int) arrayObjects[2] - 1);

					measure.setTime(calendar.getTime());
					break;
				case YEAR:
					calendar.set(Calendar.YEAR, (int) arrayObjects[1]);

					measure.setTime(calendar.getTime());
					break;
				default:
					break;

				}

				// add the measure
				measures.add(measure);
			} catch (Exception e) {
				// NOTE Not possible
			}

		}

		return measures;
	}

	@Override
	public Pair<Date, Date> findMeasureTime(Class<? extends BaseMeasure> measureClass) {
		log.debug("find the time extreme for class {}", measureClass);

		// find the max and min

		Date startDate = extractData("MAX", measureClass);
		Date endDate = extractData("MIN", measureClass);

		return new Pair<Date, Date>(startDate, endDate);
	}

	private Date extractData(String operationString, Class<? extends BaseMeasure> measureClass) {

		// find the max
		String queryString = "SELECT " + operationString + "(time) FROM " + measureClass.getSimpleName();

		// perform the query
		final Query query = em.createQuery(queryString);
		@SuppressWarnings("unchecked")
		final List<Date> listDate = query.getResultList();

		if (listDate != null && !listDate.isEmpty()) {
			return listDate.get(0);
		}

		return null;

	}
}
