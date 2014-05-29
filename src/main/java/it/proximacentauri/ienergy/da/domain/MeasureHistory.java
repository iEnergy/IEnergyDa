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
package it.proximacentauri.ienergy.da.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Rappresent a measure history
 * 
 * @author proximacentauri
 * 
 */
@Entity
@Table(name = "da_measure_history")
public class MeasureHistory extends BaseMeasure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8767446783156636302L;

}
