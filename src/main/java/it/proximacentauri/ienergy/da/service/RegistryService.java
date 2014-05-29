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
package it.proximacentauri.ienergy.da.service;

import it.proximacentauri.ienergy.da.domain.DrainDescriptor;
import it.proximacentauri.ienergy.da.domain.HistoryArregation;
import it.proximacentauri.ienergy.da.domain.MeasureAggegation;
import it.proximacentauri.ienergy.da.util.Pair;

import java.util.List;
import java.util.Set;

public interface RegistryService {

	/**
	 * List all drains
	 * 
	 * @return the list of drains
	 */
	List<DrainDescriptor> listDrains(boolean loadFormula);

	/**
	 * Get a drain by name
	 * 
	 * @param name
	 * @param loadFormula
	 * @return the loaded descriptor
	 */
	DrainDescriptor findDrainByName(String name, boolean loadFormula);

	/**
	 * Create a new drain
	 * 
	 * @param name
	 *            the drain name
	 * @param description
	 *            the description of drain
	 * @param unit
	 *            the unit on measure
	 * @param keepHistory
	 *            the history flag
	 * @param real
	 *            the drain is real of virtual measure
	 * @return the new created drain
	 */
	DrainDescriptor createDrain(String name, String description, String unit, boolean keepHistory, boolean real,
			HistoryArregation historyArregation, MeasureAggegation measureArregation, Set<Pair<String, Integer>> formula);

	/**
	 * Update a new drain descriptor
	 * 
	 * @param desc
	 *            the descriptor to save
	 * @return the saved instance
	 */
	DrainDescriptor updateDrain(DrainDescriptor desc, Set<Pair<String, Integer>> formula);
}
