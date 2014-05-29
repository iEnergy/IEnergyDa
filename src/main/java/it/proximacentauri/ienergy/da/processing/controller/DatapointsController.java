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
package it.proximacentauri.ienergy.da.processing.controller;

import it.proximacentauri.ienergy.da.domain.MeasureDetail;
import it.proximacentauri.ienergy.da.processing.model.DataPoint;
import it.proximacentauri.ienergy.da.processing.model.DataPoints;
import it.proximacentauri.ienergy.da.service.MeasureService;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/processing/")
public class DatapointsController {

	final private Logger log = LoggerFactory.getLogger(DatapointsController.class);

	// @Autowired
	// private SurveryDAO dao = null;

	@Autowired
	private MeasureService service;

	/*
	 * 
	 * Start send data point %s %s {"datapoints": [{ "at":
	 * "2013-11-29T16:50:01.000+0100", "value": "350.0", "unitOfMeasure": "ppm"
	 * }]}
	 * http://localhost:8080/JeerpDa/processing/datastreams/GAL_T03_BOTTOM_CVAL
	 * /datapoints
	 */

	@RequestMapping(value = "datastreams/{name}/datapoints", method = RequestMethod.POST)
	@ResponseBody
	public String post(@PathVariable("name") String name, @RequestBody DataPoints input) {
		log.info("Rec drain measure of {} and {}", name, input);

		// list o measure to save
		final List<MeasureDetail> list = new ArrayList<MeasureDetail>();
		for (DataPoint dataPoint : input.getDatapoints()) {
			// create the new measure detail
			MeasureDetail detail = new MeasureDetail();
			detail.setTime(dataPoint.getAt());
			detail.setValue(dataPoint.getValue());
			list.add(detail);
		}

		// save data in db
		service.saveMeasureDetail(name.toUpperCase(), list);

		return "ok";
	}
}
