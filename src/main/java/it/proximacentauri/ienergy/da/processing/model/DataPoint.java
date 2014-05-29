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
package it.proximacentauri.ienergy.da.processing.model;

import java.math.BigDecimal;
import java.util.Date;

public class DataPoint {
	private String unitOfMeasure;
	private Date at;
	private BigDecimal value;

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public Date getAt() {
		return at;
	}

	public void setAt(Date at) {
		this.at = at;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DataPoint [");
		if (unitOfMeasure != null)
			builder.append("unitOfMeasure=").append(unitOfMeasure).append(", ");
		if (at != null)
			builder.append("at=").append(at).append(", ");
		if (value != null)
			builder.append("value=").append(value);
		builder.append("]");
		return builder.toString();
	}

}
