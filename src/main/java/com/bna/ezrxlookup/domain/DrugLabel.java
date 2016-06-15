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

import org.apache.commons.lang3.StringUtils;

/**
 * DAta transfer object for drug label.

 */
public class DrugLabel extends BaseEntity implements Comparable<DrugLabel> {

	private static final long serialVersionUID = 1L;
	
	private String brandName;
	private String manufactureName;
	
	/**
	 * Default constructor
	 */
	public DrugLabel() {
		// Auto-generated
	}
	
	/**
	 * Construct instance of DrugLabel with brand name label and manufacture name
	 * @param brandName - drug label
	 * @param manufacture - name of manufacture
	 */
	public DrugLabel(String brandName, String manufacture) {
		setBrandName(brandName);
		setManufactureName(manufacture);
	}
	
	/**
	 * @return the brandName
	 */
	public String getBrandName() {
		return brandName;
	}
	
	/**
	 * @param brandName the brandName to set
	 */
	public void setBrandName(String brandName) {
		if (StringUtils.isNotEmpty(brandName))
			this.brandName = brandName.toUpperCase();
	}
	
	/**
	 * @return the manufactureName
	 */
	public String getManufactureName() {
		return manufactureName;
	}
	
	/**
	 * @param manufactureName the manufactureName to set
	 */
	public void setManufactureName(String manufactureName) {
		if (StringUtils.isNotEmpty(manufactureName))
			this.manufactureName = manufactureName.toUpperCase();
	}

	/**
	 * Compare current object instance with provided instance
	 */
	@Override
	public int compareTo(DrugLabel other) {
		return this.getBrandName().compareToIgnoreCase(other.getBrandName());
	}	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof DrugLabel)) return false;
		if (this.getBrandName() == null) return false;
		
		DrugLabel other = (DrugLabel) obj;
		return this.getBrandName().equalsIgnoreCase(other.getBrandName());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (getBrandName() == null) ? super.hashCode() : getBrandName().hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().getName());
		sb.append(" {id: ").append(getId());
		sb.append(", version: ").append(getVersion());
		sb.append(", brandName: ").append(getBrandName());
		sb.append(", manufactureName: ").append(getManufactureName());
		sb.append("}");
		return sb.toString();
	}

}
