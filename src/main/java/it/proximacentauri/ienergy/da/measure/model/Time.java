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

import it.proximacentauri.ienergy.da.util.DateUtil;

import java.util.Date;

/**
 * Time extreme result
 * 
 * @author proxima
 * 
 */
public class Time {

	private Date start;
	private Date end;

	public String getStart() {
		return DateUtil.formatDateString(start, Constants.DATIME_FORMAT);
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public String getEnd() {
		return DateUtil.formatDateString(end, Constants.DATIME_FORMAT);
	}

	public void setEnd(Date endDate) {
		this.end = endDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Time [start=");
		builder.append(start);
		builder.append(", end=");
		builder.append(end);
		builder.append("]");
		return builder.toString();
	}
}
