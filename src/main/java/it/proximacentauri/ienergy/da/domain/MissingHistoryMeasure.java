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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Domain object for missing measure used by diagnostics
 * 
 * @author proximacentauri
 * 
 */
@Entity
@Table(name = "da_diagnostic_missed_data")
public class MissingHistoryMeasure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3877033240960881118L;
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "drain_id", nullable = false)
	private DrainDescriptor desc;
	@Id
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	public DrainDescriptor getDesc() {
		return desc;
	}

	public void setDesc(DrainDescriptor desc) {
		this.desc = desc;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
