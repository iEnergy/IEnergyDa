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

import it.proximacentauri.ienergy.da.dao.DrainDescriptorDao;
import it.proximacentauri.ienergy.da.domain.DrainDescriptor;
import it.proximacentauri.ienergy.da.domain.DrainFormula;
import it.proximacentauri.ienergy.da.domain.HistoryArregation;
import it.proximacentauri.ienergy.da.domain.MeasureAggegation;
import it.proximacentauri.ienergy.da.service.RegistryService;
import it.proximacentauri.ienergy.da.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class RegistryServiceImpl implements RegistryService {

	final private Logger log = LoggerFactory.getLogger(RegistryServiceImpl.class);

	@Autowired
	private DrainDescriptorDao descriptorDao;
	@PersistenceContext
	protected EntityManager em;

	@Override
	public List<DrainDescriptor> listDrains(boolean loadFormula) {
		log.info("list all drains load fomula {}", loadFormula);

		final List<DrainDescriptor> list = descriptorDao.listDrains();

		if (loadFormula) {
			if (loadFormula) {
				// fake call to formula elements
				for (DrainDescriptor descriptor : list) {
					descriptor.getFormulaElement().size();
				}
			}
		}

		return descriptorDao.listDrains();
	}

	@Override
	public DrainDescriptor findDrainByName(String name, boolean loadFormula) {
		log.info("find drain by name {}", name);

		final DrainDescriptor descriptor = descriptorDao.findDrain(name);
		if (loadFormula && descriptor != null) {
			// fake call to formula elements
			log.trace("laod formula ");
			descriptor.getFormulaElement().size();
		}

		return descriptor;
	}

	@Override
	@Transactional(readOnly = false)
	public DrainDescriptor updateDrain(DrainDescriptor desc, Set<Pair<String, Integer>> formulaSet) {
		log.info("call update drain for {}  formula {}", desc, formulaSet);

		if (formulaSet != null) {

			final List<Pair<String, Integer>> formulaToAdd = new ArrayList<Pair<String, Integer>>(formulaSet);
			final Set<DrainFormula> formulaToRemove = new HashSet<DrainFormula>();

			// first delete element
			for (DrainFormula formula : desc.getFormulaElement()) {
				boolean toRemove = true;
				// check if element is in the formula set
				for (Pair<String, Integer> pair : formulaSet) {

					if (formula.getArgument().getDrain().equals(pair.getLeft())) {
						// elements found, don't remove or add
						formulaToAdd.remove(pair);
						toRemove = false;
						break;
					}
				}

				// if the code come here the formula has to be removed
				if (toRemove) {
					log.debug("Remove formula from drain {}", formula);
					formulaToRemove.add(formula);
				}
			}

			// real remove
			for (DrainFormula formula : formulaToRemove) {
				desc.removeFormula(formula);
			}

			// // add new drain
			for (Pair<String, Integer> pair : formulaToAdd) {
				// load drain descriptor
				DrainDescriptor descArgs = descriptorDao.findDrain(pair.getLeft());

				final DrainFormula formula = new DrainFormula();
				formula.setSign(pair.getRight());
				formula.setArgument(descArgs);
				desc.addFormula(formula);

				log.debug("Add formula {}", formula);
			}
		}

		descriptorDao.saveDrain(desc);
		return desc;
	}

	@Override
	public DrainDescriptor createDrain(String name, String description, String unit, boolean keepHistory, boolean real,
			HistoryArregation historyArregation, MeasureAggegation measureArregation, Set<Pair<String, Integer>> formulaSet) {
		log.info("call create drain for {}  formula {}", name, formulaSet);

		// create e new drain
		DrainDescriptor desc = descriptorDao.createDrain(name.toUpperCase(), description, unit, keepHistory, real, historyArregation,
				measureArregation);

		// add new drain
		for (Pair<String, Integer> pair : formulaSet) {
			// load drain descriptor
			DrainDescriptor descArgs = descriptorDao.findDrain(pair.getLeft());

			final DrainFormula formula = new DrainFormula();
			formula.setSign(pair.getRight());
			formula.setArgument(descArgs);

			log.debug("Add formula {}", formula);

			desc.addFormula(formula);
		}

		descriptorDao.saveDrain(desc);

		return desc;
	}
}
