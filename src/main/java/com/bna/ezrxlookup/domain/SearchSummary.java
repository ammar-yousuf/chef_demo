/*
 * Copyright 2012-2015 Bart & Associates, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bna.ezrxlookup.domain;

import java.util.Set;
import java.util.TreeSet;

import com.bna.ezrxlookup.util.MessageSimpleType.DerogatoryEnum;

/**
 * Data transfer object for searching results.
 */
public class SearchSummary {
	
	private DerogatoryEnum derogStatus;	
	private Set<DrugLabel> drugLabelSet;
	
	/**
	 * Default constructor
	 */
	public SearchSummary() {
		setDrugLabelSet(new TreeSet<DrugLabel>());
	}

	/**
	 * @return the drugLabelSet
	 */
	public Set<DrugLabel> getDrugLabelSet() {
		return drugLabelSet;
	}

	/**
	 * @param drugLabelSet the drugLabelSet to set
	 */
	public void setDrugLabelSet(Set<DrugLabel> drugLabelSet) {
		this.drugLabelSet = drugLabelSet;
	}

	/**
	 * @return the drugLabel
	 */
	public DrugLabel getDrugLabel() {
		for (DrugLabel label : drugLabelSet) {
			return label;
		}
		return null;
	}

	/**
	 * @return the derogStatus
	 */
	public DerogatoryEnum getDerogStatus() {
		return derogStatus;		
	}

	/**
	 * @param derogStatus the derogStatus to set
	 */
	public void setDerogStatus(DerogatoryEnum derogStatus) {
		this.derogStatus = derogStatus;
	}

	/**
	 * @return true if derogatory status is MULTI_LABEL_FOUND, otherwise false
	 */
	public boolean isMultiLabelFoundForDerog() {
		return DerogatoryEnum.MULTI_LABEL_FOUND.equals(derogStatus);
	}
	
	/**
	 * @return true if derogatory status is LABEL_NOT_FOUND, otherwise false
	 */
	public boolean isLabelNotFoundForDerog() {
		return DerogatoryEnum.LABEL_NOT_FOUND.equals(derogStatus);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("SearchSummary: {");
		sb.append(" derog status : ").append(getDerogStatus());
		sb.append(" labels : ").append(getDrugLabelSet()).append("}");
		return sb.toString();
	}
	
}
