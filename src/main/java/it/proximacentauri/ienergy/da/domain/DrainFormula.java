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
package it.proximacentauri.ienergy.da.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "da_drain_formula", uniqueConstraints = { @UniqueConstraint(columnNames = { "drain_id", "drain_argument_id" }) })
public class DrainFormula extends BaseDomain {

	private int sign = 1;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "drain_id", nullable = false)
	private DrainDescriptor desc;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "drain_argument_id", nullable = false)
	private DrainDescriptor argument;

	/**
	 * For Hibernate
	 */
	public DrainFormula() {

	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public DrainDescriptor getDesc() {
		return desc;
	}

	public void setDesc(DrainDescriptor desc) {
		this.desc = desc;
	}

	public DrainDescriptor getArgument() {
		return argument;
	}

	public void setArgument(DrainDescriptor argument) {
		this.argument = argument;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DrainFormula [sign=");
		builder.append(sign);
		builder.append(", desc=");
		builder.append(desc);
		builder.append(", argument=");
		builder.append(argument);
		builder.append("]");
		return builder.toString();
	}
}
