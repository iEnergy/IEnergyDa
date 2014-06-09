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
package it.proximacentauri.ienergy.da.dao.impl;

import it.proximacentauri.ienergy.da.dao.ConfortDao;
import it.proximacentauri.ienergy.da.domain.CategoryType;
import it.proximacentauri.ienergy.da.domain.FunctionType;
import it.proximacentauri.ienergy.da.domain.Indicator;
import it.proximacentauri.ienergy.da.domain.Vote;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class ConfortDaoImpl extends BaseDaoImpl implements ConfortDao {

	static private Logger log = LoggerFactory.getLogger(ConfortDaoImpl.class);

	@Override
	@Transactional
	public Indicator createIndicator(String name, String desc, FunctionType function, CategoryType type) {
		log.info("create a new indicator with name {} function {} type", name, function, type);

		final Indicator indicator = new Indicator();
		indicator.setName(name);
		indicator.setDescription(desc);
		indicator.setFunction(function);
		indicator.setType(type);

		em.persist(indicator);
		em.flush();
		return indicator;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Indicator> listIndicator() {
		return em.createQuery("FROM Indicator").getResultList();
	}

	@Override
	public Indicator findIndicator(String name) {
		Query query = em.createQuery("FROM Indicator WHERE name = :name");
		query.setParameter("name", name);

		// get the data from db
		@SuppressWarnings("unchecked")
		List<Indicator> list = query.getResultList();

		if (list.size() == 0)
			return null;

		return list.get(0);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteIndicator(Indicator indicator) {
		em.remove(em.merge(indicator));
		em.flush();
	}

	@Override
	public Indicator saveIndicator(Indicator indicator) {
		em.persist(em.merge(indicator));
		em.flush();
		return indicator;
	}

	@Override
	@Transactional(readOnly = false)
	public Vote appendVote(Indicator indicator, Vote vote) {
		indicator = em.merge(indicator);
		vote.setTime(new Date());

		// append the vote
		vote.setIndicator(indicator);
		em.persist(indicator);
		em.persist(vote);
		return vote;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Vote> listVote(Indicator indicator, Date start, Date end) {
		log.info("list votes of indicator {} with start {} and end {} time", indicator.getName(), start, end);
		final Query query = em
				.createQuery("SELECT v FROM Vote as v JOIN v.indicator as i where i.name = :name and v.time >= :start and v.time <= :end");
		query.setParameter("name", indicator.getName());
		query.setParameter("start", start);
		query.setParameter("end", end);
		return query.getResultList();
	}

	@Override
	public Double computeResult(Indicator indicator, Date start, Date end) {
		log.info("compute the result of indicator {} from date {} to date {}", indicator.getName(), start, end);
		Query query = null;

		switch (indicator.getFunction()) {
		case AVG:
			query = em
					.createQuery("SELECT AVG(v.value) FROM Vote as v JOIN v.indicator as i where i.name = :name and v.time >= :start and v.time <= :end");
			break;
		case LOGABSSUM:
			query = em
					.createQuery("SELECT SUM(v.value) FROM Vote as v JOIN v.indicator as i where i.name = :name and v.time >= :start and v.time <= :end");
			break;
		case LOGSUM:
		case SUM:
		default:
			query = em
					.createQuery("SELECT SUM(v.value) FROM Vote as v JOIN v.indicator as i where i.name = :name and v.time >= :start and v.time <= :end");
			break;

		}
		query.setParameter("name", indicator.getName());
		query.setParameter("start", start);
		query.setParameter("end", end);

		final Object object = query.getResultList().get(0);
		Double result = null;
		if (object == null)
			return null;
		if (object instanceof Long) {
			result = new Double(object.toString());
		} else {
			result = (Double) object;
		}

		// post selection computing
		log.debug("result of database group by is {}", result);

		switch (indicator.getFunction()) {
		case LOGABSSUM:
			if (result.doubleValue() > 0.0)
				return Math.log10(result);
			if (result.doubleValue() < 0.0)
				return -Math.log10(-result);
			return result;

		case LOGSUM:
			if (result.doubleValue() == 0.0)
				return result;
			return Math.log10(result);
		default:
			return result;

		}
	}

	@Override
	public void deleteVote(Vote vote) {
		log.info("delete a vote {}", vote.toString());
		em.remove(em.merge(vote));
		em.flush();
	}

	@Override
	public int countVote(Indicator indicator, Date start, Date end) {
		final Query query = em
				.createQuery("SELECT COUNT(v) FROM Vote as v JOIN v.indicator as i where i.name = :name and v.time >= :start and v.time <= :end");
		query.setParameter("name", indicator.getName());
		query.setParameter("start", start);
		query.setParameter("end", end);
		return ((Long) query.getResultList().get(0)).intValue();
	}
}
