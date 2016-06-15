/**
 * 
 */
package com.bna.ezrxlookup.util;


import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.json.JSONArray;
import org.json.JSONObject;
import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.jta.UserTransactionAdapter;
import org.elasticsearch.action.admin.cluster.health.*;




/**
 * @author chana
 *
 */

@Component
public class QuerySpl {
	
	private static final Logger LOG = Logger.getLogger(QuerySpl.class);
	private static CloseableHttpClient httpclient;
	
	public static final int DEFAULT_MAX_DATA_LOAD = 100; //Max allowed by openFDA API
	//private static Client client;
	
	private Client client;
	
	/**
	 * @param client the client to set
	 */
	@Autowired
	public void setClient(@Qualifier("elasticClient")Client client) {
		this.client = client;
	}
	
	
	private static final String BRAND_NAME = "brand_name";	
	private static boolean amIReady = false;
	
	@PostConstruct
    public void init() {
		EnumSet<ClusterHealthStatus> acceptable = EnumSet.of(ClusterHealthStatus.GREEN, ClusterHealthStatus.YELLOW);
		waitForStatus(client, acceptable );
		amIReady = true;
		LOG.debug("Elasticsearch is completely up and running");
    }
	

	/**
	 * 
	 * @param userEnteredText
	 * @return Matching brand_name from local Elasticsearch enriched data from SPL
	 * @throws Exception
	 */
	public ArrayList<String> getSuggestions(String userEnteredText) throws Exception {
		  
			
			try {
				 
				if(!amIReady) {
					LOG.error("Elasticsearch is Still not Ready!!!");
					ArrayList<String> list = new ArrayList<String>();
					return list;
				}
				
				QueryBuilder qb =   matchQuery( "_all", userEnteredText);

				SearchResponse searchResponse = client.prepareSearch("spl").setSearchType(SearchType.QUERY_AND_FETCH).setQuery(qb).setSize(5).execute().actionGet(); 

				ArrayList<String> list = new ArrayList<String>();

				for (SearchHit hit : searchResponse.getHits()) {
					Map<String,Object> source=hit.getSource();

					if(source != null) {
						try {

							String brand_name = "";
							@SuppressWarnings("unchecked") // OK since we know the return type. ClassCast Exception is handled to cover
							Map<String, Object> srcValue = (Map<String, Object>) (source.get("openfda"));

							if (srcValue != null) {
								@SuppressWarnings("unchecked") // OK since we know the return type. ClassCast Exception is handled to cover
								ArrayList<String> temp = (ArrayList<String>) srcValue.get(BRAND_NAME);
								if (temp != null) {
									brand_name = temp.get(0);
									if (!brand_name.isEmpty())
										list.add(brand_name);
									LOG.debug("OpenFda Field's Brand Name: " +  brand_name);
								}
							}

						} catch (ClassCastException ce) {
							LOG.error("Caught classcast error while extracting ES search/suggestion for " + userEnteredText);
							LOG.error("ClasscastException is: " + ce);
						} catch (Exception e) {
							LOG.error("Caught error while extracting ES search/suggestion for " + userEnteredText);
							LOG.error("Exception is: " + e);
						}
					}
				}			    			

				LOG.debug("List is" + list.toString());			
				return list;
			} catch (Exception e) {
				LOG.error("Received unxpected exception. Recovering gracefully:" + e);
				throw e;
			}							
		}

	static public void waitForStatus(Client client, EnumSet<ClusterHealthStatus> acceptableStatuses){
		ClusterHealthRequest clusterHealth=new ClusterHealthRequest();
		ClusterHealthResponse response;
		ClusterHealthStatus status=null;
		boolean ok=false;
		LOG.info("Waiting for status..." + acceptableStatuses.toString());
		while (!ok) {
			response=client.admin().cluster().health(clusterHealth).actionGet();
			status=response.getStatus();
			for (    ClusterHealthStatus acceptableStatus : acceptableStatuses) {
				if (acceptableStatus.equals(status)) {
					ok=true;
				}
			}
			LOG.warn("Current Status is Still : " + status );
			try {
				Thread.sleep(100);
			}
			catch (    InterruptedException e) {
				LOG.debug("Sleep of 100 milli sec - interrupped");
			}
		}
		LOG.info("Status achieved: " + status + "!");
	}
	  
		
}
