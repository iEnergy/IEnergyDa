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
package it.proximacentauri.ienergy.da.dao;

import it.proximacentauri.ienergy.da.domain.BaseMeasure;
import it.proximacentauri.ienergy.da.domain.DrainDescriptor;
import it.proximacentauri.ienergy.da.domain.MeasureAggegation;
import it.proximacentauri.ienergy.da.domain.MeasureDetail;
import it.proximacentauri.ienergy.da.domain.MeasureRealTime;
import it.proximacentauri.ienergy.da.domain.TimeAggregation;
import it.proximacentauri.ienergy.da.util.Pair;

import java.util.Date;
import java.util.List;

/**
 * Dao interfaface to measure
 * 
 * @author proximacentauri
 * 
 */
public interface MeasureDao {

	/**
	 * Get the last rt measure form dao
	 * 
	 * @param name
	 *            the drain name
	 * @return the last rt measure
	 */
	Pair<MeasureRealTime, DrainDescriptor> findLastMeasureRealTimePair(String name);

	MeasureDetail saveMeasureDetail(MeasureDetail detail);

	List<BaseMeasure> findMeasure(DrainDescriptor desc, Date start, Date end, TimeAggregation timeAggregation,
			Class<? extends BaseMeasure> measureClass, MeasureAggegation measureArregation);

	/**
	 * Find the measure time, aka start/end time
	 * 
	 * @return the start/end time
	 */
	Pair<Date, Date> findMeasureTime(Class<? extends BaseMeasure> measureClass);

}
