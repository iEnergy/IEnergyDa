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

import it.proximacentauri.ienergy.da.domain.BaseMeasure;
import it.proximacentauri.ienergy.da.domain.DrainDescriptor;
import it.proximacentauri.ienergy.da.domain.TimeAggregation;
import it.proximacentauri.ienergy.da.util.Pair;

import java.util.Date;
import java.util.List;

public class JSonUtil {

	/**
	 * Convert a Pair<DrainDescriptor, List<BaseMeasure>> to
	 * {@link MultipleMeasureJson}
	 * 
	 * @param measureList
	 *            the list to convert
	 * @return the converted list
	 */
	static public MultipleMeasureJson convertPairDrainDescMeasureToJson(Pair<DrainDescriptor, List<BaseMeasure>> measureList,
			TimeAggregation timeAggregation, Date start, Date end) {

		final MultipleMeasureJson multipleMeasureJson = new MultipleMeasureJson(timeAggregation, start, end);
		// convert drain desc
		multipleMeasureJson.setDrain(measureList.getLeft().getDrain());
		multipleMeasureJson.setUnit(measureList.getLeft().getUnit());

		// handle measures
		for (BaseMeasure measure : measureList.getRight()) {
			multipleMeasureJson.addValue(measure.getValue(), measure.getTime());
		}

		return multipleMeasureJson;
	}

	/**
	 * Convert pair time to {@link Time}
	 * 
	 * @param obj
	 *            the pair to convert
	 * @return the time converted
	 */
	static public Time convertTimePairToJson(Pair<Date, Date> obj) {
		final Time time = new Time();
		time.setStart(obj.getRight());
		time.setEnd(obj.getLeft());

		return time;
	}
}
