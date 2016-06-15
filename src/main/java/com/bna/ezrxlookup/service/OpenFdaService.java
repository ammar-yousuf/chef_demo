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

package com.bna.ezrxlookup.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.bna.ezrxlookup.domain.DrugLabel;
import com.bna.ezrxlookup.domain.DrugReaction;
import com.bna.ezrxlookup.domain.SearchSummary;
import com.bna.ezrxlookup.util.ConfigReader;
import com.bna.ezrxlookup.util.EZRxLookupUtil;
import com.bna.ezrxlookup.util.JsonMapperUtil;
import com.bna.ezrxlookup.util.MessageSimpleType.DerogatoryEnum;
import com.bna.ezrxlookup.util.MessageSimpleType.DrugRepoEnum;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service facade layer for interfacing with Open FDA.
 *
 */
@Service("openFdaService")
public class OpenFdaService {

	private static final Logger LOG = Logger.getLogger(OpenFdaService.class);
	
	private static final String UTF_8 = "UTF-8";
	
	private static final String META_TAG 	= "meta";
	private static final String TOTAL_TAG 	= "total";

	private static final String RESULTS_TAG = "results";
	private static final String ID_TAG 		= "id";
	private static final String VERSION_TAG	= "version";

	private static final String OPENFDA_TAG 	= "openfda";	
	private static final String BRAND_NAME_TAG	= "brand_name";
	private static final String MFR_NAME_TAG 	= "manufacturer_name";
	
	private static final String LABEL_NAME_PROP 	= "search.drug.by.name";
	private static final String LABEL_NDC_PROP 		= "search.drug.by.ncd";
	private static final String EVENT_REACTION_PROP = "search.drug.reaction";
		
	@Autowired
	private ConfigReader config;
	
	@Autowired
	private MessageSource messageSource;
	
	
	/**
	 * Query drug by brand name
	 * @param drugName
	 * @return
	 */
	public SearchSummary findDrugName(String drugName) throws Exception {
		LOG.debug("Execute SPL search : " + drugName);
		long startTime = System.nanoTime();
		SearchSummary summary = new SearchSummary();
		
		String ndc = EZRxLookupUtil.convertToProductNdcCode(drugName);
		if (EZRxLookupUtil.isNdc(ndc)) {			
			summary.setDrugLabelSet(findDrugLabelByNDC(ndc));
		} else {
			summary.setDrugLabelSet(findDrugLabelByBrandName(drugName));				
		}
	
		int splCount = summary.getDrugLabelSet().size();
		LOG.debug("SPL result count: " + splCount);
				
		if (splCount < 1) {				// no label found
			summary.setDerogStatus(DerogatoryEnum.LABEL_NOT_FOUND);
			
		} else if (splCount > 1) {		// looking for exact match
			summary.setDerogStatus(DerogatoryEnum.MULTI_LABEL_FOUND);
			
		} else {
			DrugLabel label = summary.getDrugLabel();
		
			int eventCount = getTotalCountByBrandName(DrugRepoEnum.EVENT, label.getBrandName(), 1);
			int recallCount = getTotalCountByBrandName(DrugRepoEnum.RECALL, label.getBrandName(), 1);		
			
			if (recallCount > 0 && eventCount > 0) {
				summary.setDerogStatus(DerogatoryEnum.ADVERSE_RECALL_RED);				
			} else if (recallCount > 0) {
				summary.setDerogStatus(DerogatoryEnum.LABEL_FOUND_RED);
			} else if (eventCount > 0) {
				summary.setDerogStatus(DerogatoryEnum.LABEL_FOUND_YELLOW);
			} else {
				summary.setDerogStatus(DerogatoryEnum.LABEL_FOUND_GREEN);
			}
		}
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
		LOG.debug("findDrugName Took: " + duration/1000000 +  " milliseconds");
		return summary;
	}
	
	/**
	 * Query drug label count by drug brand name
	 * @param brandName - drug label search criteria
	 * @return total found
	 */
	public Set<DrugLabel> findDrugLabelByBrandName(String brandName) throws Exception {
		String response = queryOpenFda(getSearchByBrandNameURL(DrugRepoEnum.SPL, brandName, 10));		
		return getDrugLabelResults(response);
	}
	
	/**
	 * Query drug recall count by drug brand name
	 * @param brandName - drug label search criteria
	 * @return total found
	 */
	public int getTotalCountByBrandName(DrugRepoEnum repo, String brandName, int limit) throws Exception {
		int result = 0;

		String response = queryOpenFda(getSearchByBrandNameURL(repo, brandName, limit));
		if (StringUtils.isNotEmpty(response))
			result = getResponseTotal(response);

		return result;
	}

	/**
	 * Find drug label by NDC number
	 * @param drugNdc - drug NDC number
	 * @return a <code>TreeSet</code> of <code>DrugLabel</code>
	 */
	public Set<DrugLabel> findDrugLabelByNDC(String drugNdc) {		
		LOG.debug("findDrugLabelByNDC for: " + drugNdc);
		
		String ndcNumber = drugNdc;
		Set<DrugLabel> result = new TreeSet<DrugLabel>();
		
		try {
			String url = getRepoURL(DrugRepoEnum.SPL, LABEL_NDC_PROP,
					URLEncoder.encode(ndcNumber, UTF_8), 10);
			String response = queryOpenFda(url);

			if (StringUtils.isEmpty(response)) {
				LOG.debug("Did not find label with " + drugNdc);
			} else {
				LOG.debug("Found matches: " + getResponseTotal(response));
				result = getDrugLabelResults(response);
			}
		} catch (Exception e) {
			LOG.error(e);
		}

		return result;
	}
	
	/**
	 * Find drug adverse event with reaction
	 * @param brandName - drug label
	 * @param manufactureName - drug manufacture
	 * @return list of DrugReaction
	 */
	public List<DrugReaction> findDrugEventReaction(String brandName) {
		List<DrugReaction> resultList = new ArrayList<DrugReaction>();
		long startTime = System.nanoTime();
		try {
			String url = getRepoURL(DrugRepoEnum.EVENT, EVENT_REACTION_PROP,
					URLEncoder.encode(brandName, UTF_8), 20);
			
			String response = this.queryOpenFda(url);

			if (StringUtils.isEmpty(response)) {
				LOG.debug("Did not find adverse event reaction with " + brandName);
			} else {
				resultList = JsonMapperUtil.readJsonToList(response,
						RESULTS_TAG, DrugReaction.class);
				for (DrugReaction f : resultList) {
					LOG.debug(f.getTerm() + "/" + f.getCount());
				}
			}
		} catch (Exception e) {
			LOG.error(e);
		}
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
		LOG.debug("findDrugEventReaction Took: " + duration/1000000 +  " milliseconds");
		return resultList;
	}
	
	private String queryOpenFda(String url) throws Exception {
		InputStreamReader isReader = null;
		String result = null;
		
		try {
	    	CloseableHttpClient httpclient = HttpClients.createDefault();
	    	 HttpGet request = new HttpGet(url);
	    	 HttpResponse response = httpclient.execute(request);
	    	 
	    	 isReader = new InputStreamReader(response.getEntity().getContent());			 
			 
			// Get data out of response			 
			 if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {		// 404
				 LOG.debug("No result found : " + url);				 				 
				 return result;		
				 
			 } else if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_OK 
					 &&  response.getStatusLine().getStatusCode() < HttpStatus.SC_MULTIPLE_CHOICES) {	// 200 - SC_OK, 300 - SC_MULIPLE_CHOICES
				 
			 	isReader = new InputStreamReader(response.getEntity().getContent(), UTF_8);
		        BufferedReader reader = new BufferedReader(isReader);
		        StringBuilder sb = new StringBuilder();
		        String line = "";
		        
		        while ((line = reader.readLine()) != null) {
		            sb.append(line);
		        }
		        
		        result = sb.toString();
			        			        		        
	        } else {
	        	LOG.error("Encountered Error:" + response.getStatusLine().toString());
	        }	 
			 
	    } catch (Exception e) {
	    	LOG.error("Encountered Error: ", e);
	    	throw e;
	    } finally {
	    	closeInputStream(isReader);
	    }
		
		return result;
	}
	
	/**
	 * Retrieve set of <code>DrugLabel</code> from JSON response
	 * @param response
	 * @return TreeSet with DrugLabel objects
	 */
	public Set<DrugLabel> getDrugLabelResults(String jsonResponse) throws Exception {		
		Set<DrugLabel> labelList = new TreeSet<DrugLabel>();
		
		if (StringUtils.isEmpty(jsonResponse))
			return labelList;
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(jsonResponse);
			JsonNode resultsNodes = rootNode.path(RESULTS_TAG);	
			LOG.debug("Results size : " + resultsNodes.size());
			
			Iterator<JsonNode> elements = resultsNodes.elements();	

			while (elements.hasNext()) {
				JsonNode aResultsNode = elements.next();	// results node
				JsonNode openFdaNode = aResultsNode.get(OPENFDA_TAG);	

				DrugLabel label = new DrugLabel();
				label.setId(aResultsNode.path(ID_TAG).asText());
				label.setVersion(aResultsNode.path(VERSION_TAG).asText());
				label.setBrandName(openFdaNode.get(BRAND_NAME_TAG).elements().next().asText());
				label.setManufactureName(openFdaNode.get(MFR_NAME_TAG).elements().next().asText());
				
				labelList.add(label);
			}
		} catch (IOException e) {
			LOG.error(e);
			throw e;
		}
		
		LOG.debug(labelList);
		return labelList;
	}
	
	private String getSearchByBrandNameURL(DrugRepoEnum repo, String brandName, int limit) throws Exception {
		String url = "";
		
		try {
			url = getRepoURL(repo, LABEL_NAME_PROP, URLEncoder.encode(brandName, UTF_8), limit);
		} catch (UnsupportedEncodingException e) {
			LOG.error(e);
			throw e;
		}
		
		return url;
	}
	
	/**
	 * Extract total count from response meta node
	 * @param response - open fda query response
	 * @return meta result total count
	 */
	private int getResponseTotal(String response) {
        JSONObject jsonObj = new JSONObject(response);	     
        JSONObject meta =  jsonObj.getJSONObject(META_TAG);
        JSONObject results = meta.getJSONObject(RESULTS_TAG);
        return results.getInt(TOTAL_TAG);
	}
	
	/**
	 * Build FDA repository URL.
	 * @param repo - DrugRepoEnum 
	 * @param messageProp - message resource property
	 * @param searchParam - search criteria
	 * @param limit - search limit
	 * @return URL in string
	 */
	private String getRepoURL(DrugRepoEnum repo, String messageProp, String searchParam, int limit) {
		String url = messageSource.getMessage(messageProp,
				new String[] { getRepoURI(repo), config.getApiKey(),
								searchParam, String.valueOf(limit) }, Locale.US);	
		LOG.debug(repo + " - " + url);
		return url;
	}
	
	/**
	 * Return URI based on repository
	 * @param repo - openFDA repository
	 * @return URI
	 */
	private String getRepoURI(DrugRepoEnum repo) {
		String repoStr;
		if (DrugRepoEnum.SPL.equals(repo))
			repoStr = config.getDrugLabelUri();
		else if (DrugRepoEnum.EVENT.equals(repo))
			repoStr = config.getDrugEventUri();
		else 
			repoStr = config.getDrugRecallUri();
		return repoStr;
	}
	
	/**
	 * Close input stream resource
	 * @param isReader
	 */
	private void closeInputStream(InputStreamReader isReader) throws Exception {
    	if (isReader != null) {
    		try {
				isReader.close();
			} catch (IOException e) {
				LOG.error(e);
				throw e;
			} 
			isReader = null;
    	}
	}

	/**
	 * @return the config
	 */
	public ConfigReader getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(ConfigReader config) {
		this.config = config;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
