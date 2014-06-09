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

import it.proximacentauri.ienergy.da.domain.HistoryArregation;
import it.proximacentauri.ienergy.da.domain.MeasureAggegation;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class DrainDescriptorJson {

	// drain name
	private String drain;
	private String description;
	// measure unit
	private String unit;
	private boolean real;
	private boolean keepHistory;
	private List<DrainFormulaJson> formula;

	// aggregation parameters
	private MeasureAggegation aggregationFunction;
	private HistoryArregation historyFunction;

	public String getDrain() {
		return drain;
	}

	public void setDrain(String drain) {
		this.drain = drain;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public boolean isReal() {
		return real;
	}

	public void setReal(boolean real) {
		this.real = real;
	}

	public boolean isKeepHistory() {
		return keepHistory;
	}

	public void setKeepHistory(boolean keepHistory) {
		this.keepHistory = keepHistory;
	}

	public List<DrainFormulaJson> getFormula() {
		return formula;
	}

	public void setFormula(List<DrainFormulaJson> formula) {
		this.formula = formula;
	}

	public MeasureAggegation getAggregationFunction() {
		return aggregationFunction;
	}

	public void setAggregationFunction(MeasureAggegation aggregationFunction) {
		this.aggregationFunction = aggregationFunction;
	}

	public HistoryArregation getHistoryFunction() {
		return historyFunction;
	}

	public void setHistoryFunction(HistoryArregation historyFunction) {
		this.historyFunction = historyFunction;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DrainDescriptorJson [drain=");
		builder.append(drain);
		builder.append(", description=");
		builder.append(description);
		builder.append(", unit=");
		builder.append(unit);
		builder.append(", real=");
		builder.append(real);
		builder.append(", keepHistory=");
		builder.append(keepHistory);
		builder.append(", formula=");
		builder.append(formula);
		builder.append("]");
		return builder.toString();
	}
}
