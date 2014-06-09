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
package it.proximacentauri.ienergy.da.registry.controller;

import it.proximacentauri.ienergy.da.domain.DrainDescriptor;
import it.proximacentauri.ienergy.da.model.ErrorBean;
import it.proximacentauri.ienergy.da.registry.model.DrainDescriptorJson;
import it.proximacentauri.ienergy.da.registry.model.DrainFormulaJson;
import it.proximacentauri.ienergy.da.registry.model.JSonUtil;
import it.proximacentauri.ienergy.da.service.RegistryService;
import it.proximacentauri.ienergy.da.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
@RequestMapping("/registry/drains")
public class DrainRegistryController {

	final private Logger log = LoggerFactory.getLogger(DrainRegistryController.class);

	@Autowired
	private RegistryService service = null;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public Object get() {
		log.info("get request in list of all drains");

		// gets the list of all drains
		final List<DrainDescriptor> descriptors = service.listDrains(true);

		// covert the object
		final List<DrainDescriptorJson> beans = new ArrayList<DrainDescriptorJson>();

		for (DrainDescriptor drainDescriptor : descriptors) {
			beans.add(JSonUtil.convertDrainDescriptorToJson(drainDescriptor));
		}

		return beans;
	}

	@ResponseBody
	@RequestMapping(value = "{drain}", method = RequestMethod.GET)
	public Object getDetail(@PathVariable("drain") String drain) {
		log.info("get request for drain detail {}", drain);

		// gets descriptor
		final DrainDescriptor descriptor = service.findDrainByName(drain, true);

		if (descriptor == null)
			return ErrorBean.ELEMENT_NOT_FOUND;

		return JSonUtil.convertDrainDescriptorToJson(descriptor);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public Object post(@RequestBody DrainDescriptorJson descriptorJson) {
		log.info("post request for a drain {}", descriptorJson);

		// gets the drain descriptor
		DrainDescriptor descriptor = service.findDrainByName(descriptorJson.getDrain(), true);

		// build formula set
		final Set<Pair<String, Integer>> formulaSet = new HashSet<Pair<String, Integer>>();
		final List<DrainFormulaJson> formulaJson = descriptorJson.getFormula();

		if (descriptorJson.getFormula() != null) {
			for (DrainFormulaJson formula : formulaJson) {
				// create formula pair
				formulaSet.add(new Pair<String, Integer>(formula.getDrain(), formula.getSign()));
			}
		}

		if (descriptor == null) {
			// drain creation
			service.createDrain(descriptorJson.getDrain(), descriptorJson.getDescription(), descriptorJson.getUnit(),
					descriptorJson.isKeepHistory(), false, descriptorJson.getHistoryFunction(), descriptorJson.getAggregationFunction(),
					formulaSet);
		} else {
			descriptor.setDescription(descriptorJson.getDescription());
			descriptor.setKeepHistory(descriptorJson.isKeepHistory());
			descriptor.setUnit(descriptorJson.getUnit());
			descriptor.setAggregationFunction(descriptorJson.getAggregationFunction());
			descriptor.setHistoryFunction(descriptorJson.getHistoryFunction());

			// update the drain
			service.updateDrain(descriptor, formulaSet);
		}

		return descriptorJson;
	}
}
