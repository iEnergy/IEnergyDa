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

import java.util.List;

public class DataPoints {

	private List<DataPoint> datapoints;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DataPoints [");
		if (datapoints != null)
			builder.append("datapoints=").append(datapoints);
		builder.append("]");
		return builder.toString();
	}

	public List<DataPoint> getDatapoints() {
		return datapoints;
	}

	public void setDatapoints(List<DataPoint> datapoints) {
		this.datapoints = datapoints;
	}

}
