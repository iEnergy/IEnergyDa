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
package it.proximacentauri.ienergy.da.registry.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Drain formula json rappresentation
 * 
 * @author proximacentauri
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class DrainFormulaJson {
	private String drain;
	private int sign;

	public String getDrain() {
		return drain;
	}

	public void setDrain(String drain) {
		this.drain = drain;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DrainFormula [drain=");
		builder.append(drain);
		builder.append(", sign=");
		builder.append(sign);
		builder.append("]");
		return builder.toString();
	}

}
