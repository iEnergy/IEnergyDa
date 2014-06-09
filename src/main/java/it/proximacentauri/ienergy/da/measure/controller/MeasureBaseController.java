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

import it.proximacentauri.ienergy.da.domain.BaseMeasure;
import it.proximacentauri.ienergy.da.domain.DrainDescriptor;
import it.proximacentauri.ienergy.da.domain.MeasureAggegation;
import it.proximacentauri.ienergy.da.domain.TimeAggregation;
import it.proximacentauri.ienergy.da.measure.model.Constants;
import it.proximacentauri.ienergy.da.measure.model.JSonUtil;
import it.proximacentauri.ienergy.da.model.ErrorBean;
import it.proximacentauri.ienergy.da.service.MeasureService;
import it.proximacentauri.ienergy.da.util.Pair;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

public abstract class MeasureBaseController {

	// the service instance
	@Autowired
	protected MeasureService service = null;

	/**
	 * return data for a measure
	 */
	@ResponseBody
	@RequestMapping(value = "{name}", method = RequestMethod.GET)
	public Object get(@PathVariable("name") String name,
			@RequestParam(value = "start", required = false) @DateTimeFormat(pattern = Constants.DATIME_FORMAT) Date start,
			@RequestParam(value = "end", required = false) @DateTimeFormat(pattern = Constants.DATIME_FORMAT) Date end,
			@RequestParam(value = "timeAggregation", required = false, defaultValue = "MINUTE") TimeAggregation timeAggregation,
			@RequestParam(value = "measureAggregation", required = false) MeasureAggegation measureAggregation) {

		// load the measure from db
		MyResult result = queryData(name, start, end, timeAggregation, measureAggregation);

		if (result == null) {
			return ErrorBean.MEASURE_NOT_FOUND;
		}

		return JSonUtil.convertPairDrainDescMeasureToJson(result.result, timeAggregation, result.start, result.end);
	}

	/**
	 * export data based on query parameters
	 */
	@RequestMapping(value = "/report/{name}", method = RequestMethod.GET)
	public ModelAndView export(@PathVariable("name") String name,
			@RequestParam(value = "start", required = false) @DateTimeFormat(pattern = Constants.DATIME_FORMAT) Date start,
			@RequestParam(value = "end", required = false) @DateTimeFormat(pattern = Constants.DATIME_FORMAT) Date end,
			@RequestParam(value = "timeAggregation", required = false, defaultValue = "MINUTE") TimeAggregation timeAggregation,
			@RequestParam(value = "measureAggregation", required = false) MeasureAggegation measureAggregation) {

		// load the measure from db
		final MyResult result = queryData(name, start, end, timeAggregation, measureAggregation);

		// prepare the query map for jasper report
		final Map<String, Object> parameterMap = new HashMap<String, Object>();

		parameterMap.put("datasource", result.result.getRight());
		parameterMap.put("DESC", result.result.getLeft());
		parameterMap.put("format", "csv");
		// set the output file name (work only for custom extension)
		parameterMap.put("filename", name);
		return new ModelAndView("csvMeasureViewReport", parameterMap);
	}

	@ResponseBody
	@RequestMapping(value = "/time", method = RequestMethod.GET)
	public Object getTime() {
		final Pair<Date, Date> pair = queryTime();
		return JSonUtil.convertTimePairToJson(pair);
	}

	protected class MyResult {
		public Pair<DrainDescriptor, List<BaseMeasure>> result;
		public Date start;
		public Date end;
	}

	protected abstract MyResult queryData(String name, Date start, Date end, TimeAggregation timeAggregation,
			MeasureAggegation measureAggregation);

	protected abstract Pair<Date, Date> queryTime();

}
