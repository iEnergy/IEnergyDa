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
package it.proximacentauri.ienergy.da.dao;

import it.proximacentauri.ienergy.da.domain.DrainDescriptor;
import it.proximacentauri.ienergy.da.domain.HistoryArregation;
import it.proximacentauri.ienergy.da.domain.MeasureAggegation;

import java.util.List;

public interface DrainDescriptorDao {

	/**
	 * List all drains in the
	 * 
	 * @return the list of drain
	 */
	List<DrainDescriptor> listDrains();

	/**
	 * Find a drain by anme
	 * 
	 * @param name
	 *            the name of drain
	 * @return the drain descriptor
	 */
	DrainDescriptor findDrain(String name);

	/**
	 * Create a new drain
	 * 
	 * @param name
	 *            the drain name
	 * @param description
	 *            the description of drain
	 * @param unit
	 *            the measure unit
	 * @param keepHistory
	 *            the keep history data flag
	 * @param real
	 *            true for real drain, false for virtual drain
	 * @return the created drain
	 */
	DrainDescriptor createDrain(String name, String description, String unit, boolean keepHistory, boolean real,
			HistoryArregation historyArregation, MeasureAggegation measureArregation);

	/**
	 * Save a drain
	 * 
	 * @param desc
	 *            the descriptor to save
	 * @return
	 */
	DrainDescriptor saveDrain(DrainDescriptor desc);
}
