/**
 * 
 */
package com.bna.ezrxlookup.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bna.ezrxlookup.util.QuerySpl;

/**
 * @author Ravi
 *
 */

@Service("suggestService")
public class SuggestService {
	
	private static final Logger LOG = Logger.getLogger(SuggestService.class);
	
	@Autowired
	private QuerySpl splQuery;


	public QuerySpl getSplQuery() {
		return splQuery;
	}


	public void setSplQuery(QuerySpl splQuery) {
		this.splQuery = splQuery;
	}
	
	/**
	 * 
	 * @param query -user entered query value
	 * @return Matching phrases from local Elasticsearch enriched data if any
	 * @throws Exception
	 */
	public List<String> findSuggestions(String query) throws Exception {
		
		LOG.debug("findSuggestions for: " + query);
		
		List<String> resultList = new ArrayList<String>();
		
		if (splQuery == null) {
			LOG.error("No SPL service available");						
			return resultList;			
		}
				
		try {
			long startTime = System.nanoTime();
			
			List<String> suggestedList = splQuery.getSuggestions(query);
			LOG.debug("The ES returned list is: " + suggestedList);
			//Eliminate duplicates
			Set<String> set = new HashSet<String>(suggestedList);
			resultList = new ArrayList<String>(set);		

			long endTime = System.nanoTime();
			long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
			LOG.debug("findSuggestions Took: " + duration/1000000 +  " milliseconds to return Scrubbed List returned: " + resultList.toString());
			return resultList;
		} catch (Exception e) {
			LOG.error("Received unxpected exception from Suggestions Service. Recovering gracefully:" + e);
			throw e;
		}
	}

}
