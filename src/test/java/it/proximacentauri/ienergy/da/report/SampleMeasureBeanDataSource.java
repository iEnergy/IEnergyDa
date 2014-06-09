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
package it.proximacentauri.ienergy.da.report;

import it.proximacentauri.ienergy.da.domain.BaseMeasure;
import it.proximacentauri.ienergy.da.domain.DrainDescriptor;
import it.proximacentauri.ienergy.da.domain.MeasureHistory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Sample example measure data source for report
 * 
 * @author proxima
 * 
 */
public class SampleMeasureBeanDataSource {

	static public Collection<BaseMeasure> createBeanCollection() {
		List<BaseMeasure> list = new ArrayList<BaseMeasure>();

		DrainDescriptor drainDescriptor = new DrainDescriptor();
		drainDescriptor.setDrain("m1-p1");
		drainDescriptor.setUnit("w");

		for (int i = 0; i < 100; ++i) {

			MeasureHistory measureHistory = new MeasureHistory();
			measureHistory.setDesc(drainDescriptor);
			measureHistory.setValue(new BigDecimal(i));
			measureHistory.setTime(new Date());
			list.add(measureHistory);
		}

		return list;

	}

}
