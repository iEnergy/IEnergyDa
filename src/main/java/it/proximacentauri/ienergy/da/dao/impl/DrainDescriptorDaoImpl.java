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

import it.proximacentauri.ienergy.da.dao.DrainDescriptorDao;
import it.proximacentauri.ienergy.da.domain.DrainDescriptor;
import it.proximacentauri.ienergy.da.domain.HistoryArregation;
import it.proximacentauri.ienergy.da.domain.MeasureAggegation;
import it.proximacentauri.ienergy.da.domain.MeasureRealTime;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrainDescriptorDaoImpl extends BaseDaoImpl implements DrainDescriptorDao {

	final private Logger log = LoggerFactory.getLogger(MeasureRealTime.class);

	@Override
	@SuppressWarnings("unchecked")
	public List<DrainDescriptor> listDrains() {
		log.debug("list all drain in the system");
		return em.createQuery("FROM DrainDescriptor").getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public DrainDescriptor findDrain(String name) {
		log.debug("get descriptor for a given drain ");

		final Query query = em.createQuery("FROM DrainDescriptor WHERE drain = :drain");

		query.setParameter("drain", name);

		final List<DrainDescriptor> list = query.getResultList();

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	@Override
	public DrainDescriptor saveDrain(DrainDescriptor desc) {
		log.debug("save the descriptor {}", desc.toString());
		em.persist(em.merge(desc));
		return desc;
	}

	@Override
	public DrainDescriptor createDrain(String name, String description, String unit, boolean keepHistory, boolean real,
			HistoryArregation historyArregation, MeasureAggegation measureArregation) {
		log.debug("create a new drain {} {} {} {}", name, description, unit);

		// create the new drain
		DrainDescriptor desc = new DrainDescriptor();
		desc.setDrain(name);
		desc.setDescription(description);
		desc.setUnit(unit);
		desc.setReal(real);
		desc.setKeepHistory(keepHistory);
		desc.setAggregationFunction(measureArregation);
		desc.setHistoryFunction(historyArregation);

		em.persist(desc);

		return desc;
	}
}
