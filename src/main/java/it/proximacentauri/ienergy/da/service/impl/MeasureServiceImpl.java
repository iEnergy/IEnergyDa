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
package it.proximacentauri.ienergy.da.service.impl;

import it.proximacentauri.ienergy.da.dao.MeasureDao;
import it.proximacentauri.ienergy.da.domain.BaseMeasure;
import it.proximacentauri.ienergy.da.domain.DrainDescriptor;
import it.proximacentauri.ienergy.da.domain.MeasureAggegation;
import it.proximacentauri.ienergy.da.domain.MeasureDetail;
import it.proximacentauri.ienergy.da.domain.MeasureHistory;
import it.proximacentauri.ienergy.da.domain.MeasureRealTime;
import it.proximacentauri.ienergy.da.domain.TimeAggregation;
import it.proximacentauri.ienergy.da.service.RegistryService;
import it.proximacentauri.ienergy.da.util.Pair;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class MeasureServiceImpl implements it.proximacentauri.ienergy.da.service.MeasureService {

	@Autowired
	private MeasureDao measureRtDao;

	@Autowired
	private RegistryService registryService;

	@Override
	public Pair<MeasureRealTime, DrainDescriptor> findLastMeasure(String drainName) {
		return measureRtDao.findLastMeasureRealTimePair(drainName);
	}

	@Override
	@Transactional(readOnly = false)
	public void saveMeasureDetail(String drain, List<MeasureDetail> details) {
		final DrainDescriptor descriptor = registryService.findDrainByName(drain, false);

		if (descriptor == null || !descriptor.isReal())
			return;

		for (MeasureDetail detail : details) {
			detail.setDesc(descriptor);
			measureRtDao.saveMeasureDetail(detail);
		}
	}

	@Override
	public Pair<DrainDescriptor, List<BaseMeasure>> findMeasureDetails(String name, Date start, Date end, TimeAggregation timeAggregation,
			MeasureAggegation measureArregation) {
		// get descriptor
		DrainDescriptor descriptor = registryService.findDrainByName(name, false);

		if (descriptor == null)
			return null;

		List<BaseMeasure> list = measureRtDao.findMeasure(descriptor, start, end, timeAggregation, MeasureDetail.class, measureArregation);

		// create pair
		return new Pair<DrainDescriptor, List<BaseMeasure>>(descriptor, list);
	}

	@Override
	public Pair<DrainDescriptor, List<BaseMeasure>> findMeasureHistory(String name, Date start, Date end, TimeAggregation timeAggregation,
			MeasureAggegation measureArregation) {
		// get descriptor
		DrainDescriptor descriptor = registryService.findDrainByName(name, false);

		if (descriptor == null)
			return null;
		List<BaseMeasure> list = measureRtDao.findMeasure(descriptor, start, end, timeAggregation, MeasureHistory.class, measureArregation);

		// create pair
		return new Pair<DrainDescriptor, List<BaseMeasure>>(descriptor, list);
	}

	@Override
	public Pair<Date, Date> findMeasureHistoryTime() {
		return measureRtDao.findMeasureTime(MeasureHistory.class);
	}

	@Override
	public Pair<Date, Date> findMeasureDetailsTime() {
		return measureRtDao.findMeasureTime(MeasureDetail.class);
	}
}
