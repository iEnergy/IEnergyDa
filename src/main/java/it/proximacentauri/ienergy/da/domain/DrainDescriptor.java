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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Description of drain source
 * 
 * @author proximacentauri
 * 
 */
@Entity
@Table(name = "da_drain_descriptor")
public class DrainDescriptor extends BaseDomain {

	// drain name
	@Column(unique = true, nullable = false)
	private String drain;
	// drain name raw name
	private String description;
	// measure unit
	private String unit;

	@Column(columnDefinition = "boolean default true")
	private boolean real;
	@Column(columnDefinition = "boolean default false")
	private boolean keepHistory;

	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private MeasureAggegation aggregationFunction;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "VARCHAR(10) default 'AVG'")
	private HistoryArregation historyFunction;

	@OneToMany(mappedBy = "desc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<MeasureRealTime> measureRt;

	@OneToMany(mappedBy = "desc", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<DrainFormula> formulaElement = new ArrayList<DrainFormula>();

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

	public List<MeasureRealTime> getMeasureRt() {
		return measureRt;
	}

	public List<DrainFormula> getFormulaElement() {
		return formulaElement;
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

	public void addFormula(DrainFormula formula) {
		formula.setDesc(this);
		formulaElement.add(formula);
	}

	public void removeFormula(DrainFormula formula) {
		formulaElement.remove(formula);
		formula.setDesc(null);
		formula.setArgument(null);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DrainDescriptor [drain=");
		builder.append(drain);
		builder.append(", description=");
		builder.append(description);
		builder.append(", unit=");
		builder.append(unit);
		builder.append(", real=");
		builder.append(real);
		builder.append(", keepHistory=");
		builder.append(keepHistory);
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}

}
