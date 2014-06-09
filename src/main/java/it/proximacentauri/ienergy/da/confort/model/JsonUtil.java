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
package it.proximacentauri.ienergy.da.confort.model;

import it.proximacentauri.ienergy.da.domain.Indicator;
import it.proximacentauri.ienergy.da.domain.Vote;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class JsonUtil {

	/**
	 * Convert a indicator list to indicator bean list for jackson
	 * 
	 * @param list
	 *            the input list
	 * @return the coverted list
	 */
	static public List<IndicatorJson> indicatorToIndicatorBean(final List<Indicator> list) {

		final List<IndicatorJson> listBean = new ArrayList<IndicatorJson>();

		for (Indicator indicator : list) {
			final IndicatorJson bean = indicatorToIndicatorBean(indicator);
			listBean.add(bean);
		}
		return listBean;
	}

	/**
	 * Convert a indicator to indicator bean
	 * 
	 * @param indicator
	 *            the input indicator
	 * @return the converted bean
	 */
	static public IndicatorJson indicatorToIndicatorBean(final Indicator indicator) {

		final IndicatorJson bean = new IndicatorJson();
		bean.setName(indicator.getName());
		bean.setDescription(indicator.getDescription());
		bean.setFunction(indicator.getFunction());
		bean.setType(indicator.getType());

		return bean;
	}

	/**
	 * Convert a vote to vote bean
	 * 
	 * @param vote
	 *            the vote to convert
	 * @return the converted vote
	 */
	static public VoteJson voteToVoteBean(Vote vote) {
		final VoteJson bean = new VoteJson();
		bean.setValue(vote.getValue());
		bean.setTime(vote.getTime());
		return bean;
	}

	/**
	 * Convert a vote to vote bean
	 * 
	 * @param vote
	 *            the vote to convert
	 * @return the converted vote
	 */
	static public List<VoteJson> voteToVoteBean(List<Vote> listVote) {

		final List<VoteJson> listBeans = new ArrayList<VoteJson>();
		for (Vote vote : listVote) {

			final VoteJson bean = new VoteJson();
			bean.setTime(vote.getTime());
			bean.setValue(vote.getValue());
			listBeans.add(bean);
		}

		return listBeans;
	}

	/**
	 * Convert map options to options list
	 * 
	 * @param map
	 *            the mat to convert
	 * @return the converted list
	 */
	static public List<OptionsJson> optionsToOptionsBeans(Map<String, Integer> map) {
		final List<OptionsJson> list = new ArrayList<OptionsJson>();

		for (String name : map.keySet()) {
			final OptionsJson bean = new OptionsJson();

			bean.setName(name);
			bean.setValue(map.get(name));
			list.add(bean);
		}

		return list;
	}
}
