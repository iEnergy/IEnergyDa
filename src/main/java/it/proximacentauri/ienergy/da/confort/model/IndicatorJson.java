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
package it.proximacentauri.ienergy.da.confort.model;

import it.proximacentauri.ienergy.da.domain.CategoryType;
import it.proximacentauri.ienergy.da.domain.FunctionType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndicatorJson {

	private String name;
	private String description;
	private Double result;
	private int count;
	private FunctionType function;
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

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IndicatorJson [name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", result=");
		builder.append(result);
		builder.append(", count=");
		builder.append(count);
		builder.append(", function=");
		builder.append(function);
		builder.append(", type=");
		builder.append(type);
		builder.append("]");
		return builder.toString();
	}

}
