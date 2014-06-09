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
package it.proximacentauri.ienergy.da.confort.controller;

import it.proximacentauri.ienergy.da.confort.model.IndicatorJson;
import it.proximacentauri.ienergy.da.confort.model.JsonUtil;
import it.proximacentauri.ienergy.da.confort.model.VoteJson;
import it.proximacentauri.ienergy.da.dao.ConfortDao;
import it.proximacentauri.ienergy.da.domain.Indicator;
import it.proximacentauri.ienergy.da.domain.Vote;
import it.proximacentauri.ienergy.da.domain.VoteOptions;
import it.proximacentauri.ienergy.da.measure.model.Constants;
import it.proximacentauri.ienergy.da.model.ErrorBean;
import it.proximacentauri.ienergy.da.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/confort/indicators")
public class IndicatorController {

	final private Logger log = LoggerFactory.getLogger(IndicatorController.class);

	@Autowired
	private ConfortDao confortDao;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public List<IndicatorJson> list(
			@RequestParam(value = "start", required = false) @DateTimeFormat(pattern = Constants.DATIME_FORMAT) Date start,
			@RequestParam(value = "end", required = false) @DateTimeFormat(pattern = Constants.DATIME_FORMAT) Date end) {
		log.info("gets the list of indicators");

		// Load start and end time
		end = DateUtil.extractEndDate(end);
		start = DateUtil.extractStartDate(start, end);

		final List<Indicator> list = confortDao.listIndicator();
		final List<IndicatorJson> listBean = new ArrayList<IndicatorJson>();

		for (Indicator indicator : list) {
			// loads result of votes
			final IndicatorJson bean = JsonUtil.indicatorToIndicatorBean(indicator);

			// compute the result of votation
			final Double result = confortDao.computeResult(indicator, start, end);
			if (result != null)
				bean.setResult(result);
			listBean.add(bean);

			// count the votes
			int count = confortDao.countVote(indicator, start, end);
			bean.setCount(count);
		}

		return listBean;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public IndicatorJson postIndicator(@RequestBody IndicatorJson indicatorJson) {
		log.info("post a new indicator {}", indicatorJson);
		// load data
		final Indicator indicator = confortDao.findIndicator(indicatorJson.getName());

		log.info("indicator {}", indicator);

		if (indicator == null) {
			confortDao.createIndicator(indicatorJson.getName().toUpperCase(), indicatorJson.getDescription(), indicatorJson.getFunction(),
					indicatorJson.getType());
		} else {
			indicator.setDescription(indicatorJson.getDescription());
			indicator.setFunction(indicatorJson.getFunction());
			indicator.setType(indicatorJson.getType());
			confortDao.saveIndicator(indicator);
		}
		return indicatorJson;
	}

	@ResponseBody
	@RequestMapping(value = "{name}", method = RequestMethod.GET)
	public Object getIndicator(@PathVariable("name") String name,
			@RequestParam(value = "start", required = false) @DateTimeFormat(pattern = Constants.DATIME_FORMAT) Date start,
			@RequestParam(value = "end", required = false) @DateTimeFormat(pattern = Constants.DATIME_FORMAT) Date end) {

		// load the indicator
		final Indicator indicator = confortDao.findIndicator(name);

		// check if element it's present
		if (indicator == null) {
			return ErrorBean.ELEMENT_NOT_FOUND;
		}

		// Load start and end time
		end = DateUtil.extractEndDate(end);
		start = DateUtil.extractStartDate(start, end);

		// loads result of votes
		final IndicatorJson bean = JsonUtil.indicatorToIndicatorBean(indicator);

		// compute the result of votation
		final Double result = confortDao.computeResult(indicator, start, end);
		if (result != null)
			bean.setResult(result);

		// count the votes
		int count = confortDao.countVote(indicator, start, end);
		bean.setCount(count);

		return bean;
	}

	@ResponseBody
	@RequestMapping(value = "{name}/votes", method = RequestMethod.POST)
	public Object postVote(@PathVariable("name") String name, @RequestBody VoteJson voteBean) {
		log.info("recv a new votes on {} and value {}", name, voteBean);

		// loads the indicator
		final Indicator indicator = confortDao.findIndicator(name);

		// check if element it's present
		if (indicator == null) {
			return ErrorBean.ELEMENT_NOT_FOUND;
		}

		// Validate the vote
		if (!VoteOptions.validateOptions(indicator.getType(), voteBean.getValue()))
			return ErrorBean.INVALID_VOTE;

		// Append the vote
		Vote vote = new Vote();
		vote.setValue(voteBean.getValue());

		return JsonUtil.voteToVoteBean(confortDao.appendVote(indicator, vote));
	}

	@ResponseBody
	@RequestMapping(value = "{name}/votes", method = RequestMethod.GET)
	public Object listVotes(@PathVariable("name") String name,
			@RequestParam(value = "start", required = false) @DateTimeFormat(pattern = Constants.DATIME_FORMAT) Date start,
			@RequestParam(value = "end", required = false) @DateTimeFormat(pattern = Constants.DATIME_FORMAT) Date end) {

		log.info("list votes on {} with start time {} to end time {}", name, start, end);

		// Load start and end time
		end = DateUtil.extractEndDate(end);
		start = DateUtil.extractStartDate(start, end);

		log.debug("list votes on {} with start time {} to end time {} (computed time)", name, start, end);

		// gets the indicator
		final Indicator indicator = confortDao.findIndicator(name);

		// check if element it's present
		if (indicator == null) {
			return ErrorBean.ELEMENT_NOT_FOUND;
		}

		// loads votes
		return JsonUtil.voteToVoteBean(confortDao.listVote(indicator, start, end));
	}

	@ResponseBody
	@RequestMapping(value = "{name}/options", method = RequestMethod.GET)
	public Object listOptions(@PathVariable("name") String name) {

		log.info("get options of indicator {}", name);

		// loads the indicator
		final Indicator indicator = confortDao.findIndicator(name);

		final Map<String, Integer> optionsMap = VoteOptions.getOptions(indicator.getType());

		return JsonUtil.optionsToOptionsBeans(optionsMap);
	}
}
