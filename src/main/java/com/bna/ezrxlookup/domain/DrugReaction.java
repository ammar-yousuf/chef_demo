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

import java.io.Serializable;

/**
 * Data binding class for drug adverse event reaction result set. 
 *
 */
public class DrugReaction implements Serializable, Comparable<DrugReaction> {
	
	private static final long serialVersionUID = 1L;
	
	private String term;		// term of reaction	
	private Integer count;		// total count of reported incident on term
	
	
	/**
	 * Default constructor
	 */
	public DrugReaction() {
		// Auto-generated constructor stub
	}
	
	/**
	 * Create instance of DrugReaction with provided parameters.
	 * @param term 
	 * @param count
	 */
	public DrugReaction(String term, Integer count) {
		setTerm(term);
		setCount(count);
	}

	/**
	 * @return the term
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * @param term the term to set
	 */
	public void setTerm(String term) {
		this.term = term;
	}

	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) 
			return false;
		
		if (!(obj instanceof DrugReaction)) 
			return false;
		
		if (this.getTerm() == null) 
			return false;
		
		DrugReaction other = (DrugReaction) obj;
		return this.getTerm().equalsIgnoreCase(other.getTerm());
	}

	/**
	 * Compare current object instance with provided instance
	 */
	@Override
	public int compareTo(DrugReaction other) {
		return this.getTerm().compareTo(other.getTerm());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (getTerm() == null) ? super.hashCode() : getTerm().hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().getName());
		sb.append(" { term: ").append(getTerm());
		sb.append(" , count: ").append(getCount()).append(" }");
		return sb.toString();
	}

}
