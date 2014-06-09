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

import it.proximacentauri.ienergy.da.domain.DrainDescriptor;
import it.proximacentauri.ienergy.da.domain.MeasureRealTime;
import it.proximacentauri.ienergy.da.measure.model.SingleMeasureJson;
import it.proximacentauri.ienergy.da.model.ErrorBean;
import it.proximacentauri.ienergy.da.service.MeasureService;
import it.proximacentauri.ienergy.da.util.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/measure/instants")
public class MeasureRtController {

	final private Logger log = LoggerFactory.getLogger(MeasureRtController.class);

	@Autowired
	private MeasureService service = null;

	@ResponseBody
	@RequestMapping(value = "{name}", method = RequestMethod.GET)
	public Object get(@PathVariable("name") String name) {
		log.info("get request in real time measure for {}", name);
		// load data fron db
		Pair<MeasureRealTime, DrainDescriptor> pair = service.findLastMeasure(name);

		// check if pair contain data
		if (pair.isEmpty())
			return ErrorBean.MEASURE_NOT_FOUND;

		// convert the object
		final SingleMeasureJson measure = new SingleMeasureJson();
		measure.setDrain(pair.getRight().getDrain());
		measure.setUnit(pair.getRight().getUnit());
		measure.setTime(pair.getLeft().getTime());
		measure.setValue(pair.getLeft().getValue());
		return measure;
	}
}
