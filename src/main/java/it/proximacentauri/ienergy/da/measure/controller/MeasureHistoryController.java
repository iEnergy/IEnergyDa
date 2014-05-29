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
package it.proximacentauri.ienergy.da.measure.controller;

import it.proximacentauri.ienergy.da.domain.MeasureAggegation;
import it.proximacentauri.ienergy.da.domain.TimeAggregation;
import it.proximacentauri.ienergy.da.measure.model.Constants;
import it.proximacentauri.ienergy.da.util.DateUtil;
import it.proximacentauri.ienergy.da.util.Pair;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/measure/history")
public class MeasureHistoryController extends MeasureBaseController {

	final private Logger log = LoggerFactory.getLogger(MeasureHistoryController.class);

	@Override
	protected MyResult queryData(String name, Date start, Date end, TimeAggregation timeAggregation, MeasureAggegation measureAggregation) {
		// create the resutl data
		final MyResult myResult = new MyResult();

		// count start/end time
		myResult.end = DateUtil.extractEndDate(end);
		myResult.start = DateUtil.extractStartDate(start, myResult.end, Constants.MEASURE_HISTORY_TIME_WINDOW);

		log.info("get measure (history) for name {} with start: {} end: {} time aggregation: {} measureArregation; {}", name,
				myResult.start, myResult.end, timeAggregation, measureAggregation);

		// load the measure from db
		myResult.result = service.findMeasureHistory(name, myResult.start, myResult.end, timeAggregation, measureAggregation);
		return myResult;
	}

	@Override
	protected Pair<Date, Date> queryTime() {
		return service.findMeasureHistoryTime();
	}

}
