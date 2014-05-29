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
package it.proximacentauri.ienergy.da.service;

import it.proximacentauri.ienergy.da.domain.BaseMeasure;
import it.proximacentauri.ienergy.da.domain.DrainDescriptor;
import it.proximacentauri.ienergy.da.domain.MeasureAggegation;
import it.proximacentauri.ienergy.da.domain.MeasureDetail;
import it.proximacentauri.ienergy.da.domain.MeasureRealTime;
import it.proximacentauri.ienergy.da.domain.TimeAggregation;
import it.proximacentauri.ienergy.da.util.Pair;

import java.util.Date;
import java.util.List;

public interface MeasureService {

	/**
	 * Find last real time measure of the system
	 * 
	 * @param drainName
	 * @return
	 */
	Pair<MeasureRealTime, DrainDescriptor> findLastMeasure(String drainName);

	/**
	 * Save a new measure detail
	 * 
	 * @param detail
	 *            the detail of measure to save
	 * @return
	 */
	void saveMeasureDetail(String drain, List<MeasureDetail> details);

	/**
	 * Find measure details
	 * 
	 * @param name
	 *            the name of drain
	 * @param start
	 *            the start time
	 * @param end
	 *            the end time
	 * @param timeAggregation
	 *            the time aggregation level
	 * @param measureArregation
	 *            the measure function aggregation
	 * @return
	 */
	Pair<DrainDescriptor, List<BaseMeasure>> findMeasureDetails(String name, Date start, Date end, TimeAggregation timeAggregation,
			MeasureAggegation measureArregation);

	/**
	 * Find measure history
	 * 
	 * @param name
	 *            the name of drain
	 * @param start
	 *            the start time
	 * @param end
	 *            the end time
	 * @param measureArregation
	 *            the measure function aggregation
	 * @return
	 */
	Pair<DrainDescriptor, List<BaseMeasure>> findMeasureHistory(String name, Date start, Date end, TimeAggregation timeAggregation,
			MeasureAggegation measureArregation);

	/**
	 * Find the measure history time, aka start/end time
	 * 
	 * @return the start/end time
	 */
	Pair<Date, Date> findMeasureHistoryTime();

	/**
	 * Find the measure details time, aka start/end time
	 * 
	 * @return the start/end time
	 */
	Pair<Date, Date> findMeasureDetailsTime();
}
