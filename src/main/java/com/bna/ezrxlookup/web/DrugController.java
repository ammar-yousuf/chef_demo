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
package com.bna.ezrxlookup.web;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;
import org.primefaces.model.chart.PieChartModel;

import com.bna.ezrxlookup.domain.DrugLabel;
import com.bna.ezrxlookup.domain.DrugReaction;
import com.bna.ezrxlookup.domain.SearchSummary;
import com.bna.ezrxlookup.service.OpenFdaService;
import com.bna.ezrxlookup.service.SuggestService;
import com.bna.ezrxlookup.util.MessageSimpleType.DerogatoryEnum;


@ManagedBean
@SessionScoped
public class DrugController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(DrugController.class);
	
    private static final int CHART_MAX_ITEMS = 5;
    
    private static final String RESULTS_PAGE 		= "results";
    private static final String HOME_PAGE 			= "home";
    private static final String MOBILE_HOME_PAGE 	= "mobilehome";
    private static final String CHART_TITLE  		= "Top 5 Adverse Reactions";
    private static final String CHART_LOCATION 		= "s";	// south border
    
	//@Autowired
	@ManagedProperty("#{openFdaService}")
    transient OpenFdaService openFdaService;
	
	//@Autowired
	@ManagedProperty("#{suggestService}")
    transient SuggestService suggestService;
    
    private String cmdMfName;
    private String cmdBName;
    
    private String derogLabel;
    private String derogImage;
    private boolean derogShowDetails;
    
	private String drugName;
	private String noDrgFnd;
	
	
	/**
	 * Default constructor
	 */
	public DrugController() {
	}

	/**
	 * Reset managed bean data
	 */
	public void reset() {
		cmdMfName = "0";
	    cmdBName  = "0";
		drugName  = "";
		noDrgFnd  = "";
		derogLabel = "";
		derogImage = "";
		derogShowDetails = false;
	}
	
	/**
     * Text search on document
     * @return
     */
    public String goHome() {
    	reset();
    	return HOME_PAGE;
    }
    
	/**
     * Text search on document
     * @return
     */
    public String goMobileHome() {
    	reset();
    	return MOBILE_HOME_PAGE;
    }
    
    /**
     * Text search on document
     * @return
     */
    public String search() throws Exception {
    	LOG.debug("Search drug: " + getDrugName());
    	
    	String returnPage = RESULTS_PAGE;
    	
    	SearchSummary searchResult = openFdaService.findDrugName(drugName);
    	LOG.debug(searchResult);
    	
    	DerogatoryEnum derog = searchResult.getDerogStatus();
    	setDerogLabel(derog.getStatusLabel());
    	setDerogImage(derog.getImageFile());
    	setDerogShowDetails(derog.getShowDetails());
    	
    	if (searchResult.isMultiLabelFoundForDerog() 
    			|| searchResult.isLabelNotFoundForDerog()) {
    		setNoDrgFnd(searchResult.getDerogStatus().getStatusLabel());
    		returnPage = null;
    		
    	} else {
        	DrugLabel label = searchResult.getDrugLabel();
        	this.setCmdMfName(label.getManufactureName());
        	this.setCmdBName(label.getBrandName());
    		setNoDrgFnd("");
    	}
    	
    	LOG.debug("NoDrgFnd label: " + getNoDrgFnd());        	
    	LOG.debug("Drug label set count: " + searchResult.getDrugLabelSet().size());   
    	
    	return returnPage;
    }
 
    /*
     * This method calls the suggest service to get the list to provide to the UI.
     */
    public List<String> completeText(String query) throws Exception {
        try {
			List<String> results = suggestService.findSuggestions(query);
			LOG.debug(results);
			return results;
		} catch (Exception e) {			
			LOG.error("Received unxpected exception. Recovering gracefully:" + e);
			throw e;
		}
    }
    
    /**
     * Listener to clear drug label field.
     */
    public void searchListener() {
    	setNoDrgFnd("");
    }
    
    /**
     * Perform detail search on drug adverse events and formulate result set as pie chart.
     * @return PieChartModel
     */
	public PieChartModel getPieModelList() {
		LOG.debug("brand_name: " + getCmdBName());
		LOG.debug("manufacture_name: " + getCmdMfName());
		
		List<DrugReaction> resultList = openFdaService.findDrugEventReaction(getCmdBName());		
		int maxSize = (resultList.size() > CHART_MAX_ITEMS) ? CHART_MAX_ITEMS : resultList.size();
		
		PieChartModel pieModelList = new PieChartModel();
		for (int i=0; i < maxSize; i++) {
			DrugReaction obj = resultList.get(i);
			LOG.debug(obj);
			pieModelList.set(obj.getTerm(), obj.getCount());
		}
		
		pieModelList.setTitle(CHART_TITLE);
		pieModelList.setLegendPosition(CHART_LOCATION);
		pieModelList.setShowDataLabels(true);
		pieModelList.setDiameter(150);
        
		return pieModelList;
	}

	/**
	 * @return the drugName
	 */
	public String getDrugName() {
		return drugName;
	}

	/**
	 * @param drugName the drugName to set
	 */
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	/**
	 * @return the noDrgFnd
	 */
	public String getNoDrgFnd() {
		return noDrgFnd;
	}

	/**
	 * @param noDrgFnd the noDrgFnd to set
	 */
	public void setNoDrgFnd(String noDrgFnd) {
		this.noDrgFnd = noDrgFnd;
	}

	/**
	 * @return the derogImage
	 */
	public String getDerogImage() {
		return derogImage;
	}

	/**
	 * @param derogImage the derogImage to set
	 */
	public void setDerogImage(String derogImage) {
		this.derogImage = derogImage;
	}

	/**
	 * @return the derogLabel
	 */
	public String getDerogLabel() {
		return derogLabel;
	}

	/**
	 * @param derogLabel the derogLabel to set
	 */
	public void setDerogLabel(String derogLabel) {
		this.derogLabel = derogLabel;
	}

	/**
	 * @return the derogShowDetails
	 */
	public boolean getDerogShowDetails() {
		return derogShowDetails;
	}

	/**
	 * @param derogShowDetails the derogShowDetails to set
	 */
	public void setDerogShowDetails(boolean derogShowDetails) {
		this.derogShowDetails = derogShowDetails;
	}

	/**
	 * @return the cmdMfName
	 */
	public String getCmdMfName() {
		return cmdMfName;
	}

	/**
	 * @param cmdMfName the cmdMfName to set
	 */
	public void setCmdMfName(String cmdMfName) {
		this.cmdMfName = cmdMfName;
	}

	/**
	 * @return the cmdBName
	 */
	public String getCmdBName() {
		return cmdBName;
	}

	/**
	 * @param cmdBName the cmdBName to set
	 */
	public void setCmdBName(String cmdBName) {
		this.cmdBName = cmdBName;
	}

	/**
	 * @return the openFdaService
	 */
	public OpenFdaService getOpenFdaService() {
		return openFdaService;
	}

	/**
	 * @param openFdaService the openFdaService to set
	 */
	public void setOpenFdaService(OpenFdaService openFdaService) {
		this.openFdaService = openFdaService;
	}

	/**
	 * @return the suggestService
	 */
	public SuggestService getSuggestService() {
		return suggestService;
	}

	/**
	 * @param suggestService the suggestService to set
	 */
	public void setSuggestService(SuggestService suggestService) {
		this.suggestService = suggestService;
	}

}