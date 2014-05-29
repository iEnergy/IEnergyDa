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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "da_confort_indicator")
public class Indicator extends BaseDomain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7666940908753594719L;
	@Column(unique = true, nullable = false)
	private String name;
	private String description;

	@OneToMany(mappedBy = "indicator", cascade = CascadeType.ALL)
	private List<Vote> votes = new ArrayList<Vote>();

	@Enumerated(EnumType.STRING)
	private FunctionType function;
	@Enumerated(EnumType.STRING)
	private CategoryType type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FunctionType getFunction() {
		return function;
	}

	public void setFunction(FunctionType function) {
		this.function = function;
	}

	public CategoryType getType() {
		return type;
	}

	public void setType(CategoryType type) {
		this.type = type;
	}

	public List<Vote> getVotes() {
		return votes;
	}
}
