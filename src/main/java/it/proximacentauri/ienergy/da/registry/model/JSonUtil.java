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
package it.proximacentauri.ienergy.da.registry.model;

import it.proximacentauri.ienergy.da.domain.DrainDescriptor;
import it.proximacentauri.ienergy.da.domain.DrainFormula;

import java.util.ArrayList;
import java.util.List;

public class JSonUtil {

	/**
	 * Convert {@link DrainDescriptor} to {@link DrainDescriptorJson}
	 * 
	 * @param desc
	 * @return
	 */
	static public DrainDescriptorJson convertDrainDescriptorToJson(DrainDescriptor desc) {
		final DrainDescriptorJson bean = new DrainDescriptorJson();

		// bean converstion
		bean.setDescription(desc.getDescription());
		bean.setDrain(desc.getDrain());
		bean.setKeepHistory(desc.isKeepHistory());
		bean.setUnit(desc.getUnit());
		bean.setReal(desc.isReal());
		bean.setAggregationFunction(desc.getAggregationFunction());
		bean.setHistoryFunction(desc.getHistoryFunction());

		// formula conversion
		final List<DrainFormulaJson> beanFormulaList = new ArrayList<DrainFormulaJson>();
		for (DrainFormula formula : desc.getFormulaElement()) {
			final DrainFormulaJson beanFormula = new DrainFormulaJson();
			beanFormula.setDrain(formula.getArgument().getDrain());
			beanFormula.setSign(formula.getSign());
			beanFormulaList.add(beanFormula);
		}
		bean.setFormula(beanFormulaList);

		return bean;
	}
}
