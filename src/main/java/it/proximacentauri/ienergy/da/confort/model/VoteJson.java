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
package it.proximacentauri.ienergy.da.confort.model;

import it.proximacentauri.ienergy.da.measure.model.Constants;
import it.proximacentauri.ienergy.da.util.DateUtil;

import java.util.Date;

public class VoteJson {

	private int value;
	private Date time;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getTime() {
		return DateUtil.formatDateString(time, Constants.DATIME_FORMAT);
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("VoteBean [value=");
		builder.append(value);
		builder.append(", time=");
		builder.append(time);
		builder.append("]");
		return builder.toString();
	}

}
