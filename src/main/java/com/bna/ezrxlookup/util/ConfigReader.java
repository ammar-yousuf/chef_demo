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
package com.bna.ezrxlookup.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * Utility class to load application configuration into memory.
 *
 */
@Component
@PropertySource("classpath:/openFdaConfig.properties")
public class ConfigReader {
	
	@Value("${openFdaApiUrl}")
	private String apiUrl;
	
	@Value("${openFdaDrugEventUri}")
	private String drugEventUri;
	
	@Value("${openFdaDrugRecallUri}")
	private String drugRecallUri;
	
	@Value("${openFdaDrugLabelUri}")
	private String drugLabelUri;
	
	@Value("${openFdaApiKey}")
	private String apiKey;
	
	@Value("${serverURL}")
	private String serverURL;
	
	@Value("${splSuggestionMappingFile}")
	private String splSuggestionMappingFile;

	@Value("${splSuggestionSettingsFile}")
	private String splSuggestionSettingsFile;
	
	
	/**
	 * Default constructor
	 */
	public ConfigReader() {	
	}

	/**
	 * @return the apiUrl
	 */
	public String getApiUrl() {
		return apiUrl;
	}

	/**
	 * @return the drugEventUri
	 */
	public String getDrugEventUri() {
		return drugEventUri;
	}

	/**
	 * @return the drugRecallUri
	 */
	public String getDrugRecallUri() {
		return drugRecallUri;
	}

	/**
	 * @return the drugLabelUri
	 */
	public String getDrugLabelUri() {
		return drugLabelUri;
	}

	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * @return the serverURL
	 */
	public String getServerURL() {
		return serverURL;
	}

	/**
	 * @return the splSuggestionSettingsFile
	 */
	public String getSplSuggestionSettingsFile() {
		return splSuggestionSettingsFile;
	}

	/**
	 * @param splSuggestionSettingsFile the splSuggestionSettingsFile to set
	 */
	public void setSplSuggestionSettingsFile(String splSuggestionSettingsFile) {
		this.splSuggestionSettingsFile = splSuggestionSettingsFile;
	}

	/**
	 * @return the splSuggestionMappingFile
	 */
	public String getSplSuggestionMappingFile() {
		return splSuggestionMappingFile;
	}
	
	
	/**
	 * @param splSuggestionMappingFile the splSuggestionMappingFile to set
	 */
	public void setSplSuggestionMappingFile(String splSuggestionMappingFile) {
		this.splSuggestionMappingFile = splSuggestionMappingFile;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getName());
		sb.append("\nopenFdaApiUrl : ").append(getApiUrl());
		sb.append("\nopenFdaDrugEventUri : ").append(getDrugEventUri());
		sb.append("\nopenFdaDrugRecallUri : ").append(getDrugRecallUri());
		sb.append("\nopenFdaDrugLabelUri : ").append(getDrugLabelUri());
		sb.append("\nopenFdaApiKey : ").append(getApiKey());
		sb.append("\nsplSuggestionMappingFile : ").append(getSplSuggestionMappingFile());
		return sb.toString();
	}
	
}
