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
package it.proximacentauri.ienergy.da.measure.model;

import it.proximacentauri.ienergy.da.domain.TimeAggregation;
import it.proximacentauri.ienergy.da.util.DateUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * List of measures
 * 
 * @author proximacentauri
 * 
 */
public class MultipleMeasureJson {
	// drain name
	private String drain = null;
	// drain unit of measure
	private String unit = null;

	// query definition
	private QueryParam queryParam;

	// list of measures
	private List<Value> measures = new ArrayList<Value>();

	/**
	 * Create a new mutiple measure result
	 * 
	 * @param timeAggregation
	 * @param start
	 * @param end
	 */
	public MultipleMeasureJson(TimeAggregation timeAggregation, Date start, Date end) {
		this.queryParam = new QueryParam();
		queryParam.setEnd(end);
		queryParam.setStart(start);
		queryParam.setTimeAggregation(timeAggregation);
	}

	public String getDrain() {
		return drain;
	}

	public void setDrain(String name) {
		this.drain = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<Value> getMeasures() {
		return measures;
	}

	public void addValue(BigDecimal value, Date time) {
		measures.add(new Value(value, time));
	}

	public QueryParam getQueryParam() {
		return this.queryParam;
	}

	public class Value {
		private BigDecimal value = null;
		private Date time = null;

		public Value(BigDecimal value, Date time) {
			super();
			this.value = value;
			this.time = time;
		}

		public BigDecimal getValue() {
			return value;
		}

		public String getTime() {
			switch (MultipleMeasureJson.this.queryParam.timeAggregation) {
			case DAY:
				return DateUtil.formatDateString(time, Constants.DATIME_DAY);
			case HOUR:
				return DateUtil.formatDateString(time, Constants.DATIME_HOUR);
			case MONTH:
				return DateUtil.formatDateString(time, Constants.DATIME_MONTH);
			case YEAR:
				return DateUtil.formatDateString(time, Constants.DATIME_YEAR);
			default:
				return DateUtil.formatDateString(time, Constants.DATIME_FORMAT);
			}
		}

	}

	/**
	 * Class containing the query parameter of request
	 * 
	 * @author proximacentauri
	 * 
	 */
	public class QueryParam {
		// current time aggregation
		private TimeAggregation timeAggregation;
		private Date start = null;
		private Date end = null;

		public TimeAggregation getTimeAggregation() {
			return timeAggregation;
		}

		public void setTimeAggregation(TimeAggregation timeAggregation) {
			this.timeAggregation = timeAggregation;
		}

		public String getStart() {
			return DateUtil.formatDateString(start, Constants.DATIME_FORMAT);
		}

		public void setStart(Date start) {
			this.start = start;
		}

		public String getEnd() {
			return DateUtil.formatDateString(end, Constants.DATIME_FORMAT);
		}

		public void setEnd(Date end) {
			this.end = end;
		}
	}
}
