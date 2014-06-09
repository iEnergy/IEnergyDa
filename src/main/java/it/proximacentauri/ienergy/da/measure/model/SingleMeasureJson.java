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

import java.math.BigDecimal;
import java.util.Date;

/**
 * Single measure json model for rt measures
 * 
 * @author proximacentauri
 * 
 */
public class SingleMeasureJson {
	private String drain = null;
	private String unit = null;
	private BigDecimal value = null;
	private Date time = null;

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

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getTime() {
		return DateUtil.formatDateString(time, Constants.DATIME_FORMAT);
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
