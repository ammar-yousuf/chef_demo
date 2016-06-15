/**
 * 
 */
package com.bna.ezrxlookup.util;


import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.EnumSet;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



@Component
@EnableScheduling
public class IngestSpl  {

	
	private static final Logger LOG = Logger.getLogger(IngestSpl.class);
	private static CloseableHttpClient httpclient;
	
	public static final int DEFAULT_MAX_DATA_LOAD = 100; //Max allowed by openFDA API
		
	private Client client;
	
	/**
	 * @param client the client to set
	 */
	@Autowired
	public void setClient(@Qualifier("elasticClient")Client client) {
		this.client = client;
	}
	
	
	@Autowired
	private ConfigReader configReader;
	
	

	/**
	 * This is so that if the SPL index does not exist we will create it and load the data only the first time.
	 * We don't want to delay the startup of the container. Initial Delay accounts for that.
	 * FixedDelay calls this method every 5 mins.
	 */
	@Scheduled(initialDelay=6000, fixedDelay=300000)
    public void init() {
		
		if( ! doesSplIndexExist("spl") ) {
			if( ingestSplData(-1) >= 0 ){
				long count = getSplCount();
				if(count > 0) {
					LOG.debug("Number of records in SPL index to be used for suggestion: " + count);
					LOG.warn("Please Note Suggestions are based on a limited SPL record set due to openFDA API restrictions");
				}
				else
					LOG.error("SPL Index is not available for suggestions!");
			}else {
				LOG.error("SPL Index is not available for suggestions!");
			}
		}else {
			long count = getSplCount();
			if(count > 0)
				LOG.debug("Number of records in SPL index to be used for suggestion: " + count);
			else
				LOG.error("SPL Index is not available for suggestions!");
		}
		
    }
	

	
	/**
	 * Ingest SPL data from OpenFDA -- Currently limited to 5K records 
	 * @param String  - Max rows to ingest (-1) for all
	 */
	 public  int ingestSplData( int maxRowsToIngest){
		 int totalRecordsIngested = 0;
		
		    try {
		    	httpclient = HttpClients.createDefault();
		    	int offset = 0;
		 		Boolean moreRecords = true;
		 		int batchSize = DEFAULT_MAX_DATA_LOAD;

		 		if(client == null){
		 			LOG.error("Elasticsearch client has not be injected and is null. Aborting");
		 			return(-1);
		 		}
		    	 
		 		// Wait for the elasticsearch to come to an acceptable state.
		    	EnumSet<ClusterHealthStatus> acceptable = EnumSet.of(ClusterHealthStatus.GREEN, ClusterHealthStatus.YELLOW);
		 		waitForStatus(client, acceptable );
		 		LOG.debug("Elasticsearch is completely up and running");
		 		
		 		//Create the index if one does not exist
		 		if(!createSplIndex() ){
		 			return(-1);
		 		}
				
		 		//This is where get the data and loop until we ingest all
		 		BulkRequestBuilder bulkBuilder = client.prepareBulk();
		 		if(maxRowsToIngest == -1) {
		 			LOG.debug("Requested to load all SPL records. Max Allowed is 5100");
		 			maxRowsToIngest =5100;
		 		}
		 		while ((moreRecords && maxRowsToIngest > 0 )) {
		 			if (maxRowsToIngest > 0) {
		 				batchSize = ((maxRowsToIngest - offset) > DEFAULT_MAX_DATA_LOAD) ? DEFAULT_MAX_DATA_LOAD : (maxRowsToIngest - offset);
		 			}
		 			LOG.debug("Requested to load:" + maxRowsToIngest + " Fetching " + batchSize + " records from an offset of " + offset);
		 			JSONArray jsonarray = getSplData(offset, maxRowsToIngest);
		 			if(jsonarray == null){
		 				LOG.debug("Got null back from getSplData");
		 				return totalRecordsIngested;
		 			}
		 			//Add to the bulk request
		 			totalRecordsIngested += jsonarray.length();
		 			for (int i = 0; i < jsonarray.length(); i++) {
		 				JSONObject obj = jsonarray.getJSONObject(i);
		 				bulkBuilder.add(client.prepareIndex("spl", "label").setSource(obj.toString()));	
		 			}
		 			//Execute Bulk Request
		 			try {
		 				LOG.debug("About to execute bulk load request");
		 				BulkResponse bulkRes = bulkBuilder.execute().actionGet();
		 				if(bulkRes.hasFailures()){
		 					LOG.error("##### Bulk Request failure with error: " + bulkRes.buildFailureMessage());
		 					return totalRecordsIngested;
		 				}
		 				LOG.debug("Ingested a total of " + totalRecordsIngested);
		 			} catch (ElasticsearchException e) {
		 				LOG.error("Failed during a bulk insert:" + e);
		 				return totalRecordsIngested;
		 			}
		 			bulkBuilder = client.prepareBulk();
		 			//Do we need to fetch more records
		 			offset += jsonarray.length();
		 			if ((jsonarray.length() != batchSize) || 
		 					((maxRowsToIngest > 0) && (maxRowsToIngest - offset) <= 0)) {
		 				moreRecords = false;
		 			} 
		 		}  					
		 	} catch (Exception e1) {
		 		LOG.error("Encountered an exception while ingesting records:" + e1);
		 		return -1;
			}
			return maxRowsToIngest; 

	    }
		
	 /**
	  * Check if the index exists
	  * @param indexName
	  * @return boolean
	  */
	 
	 private boolean doesSplIndexExist(String indexName){
		 try {
			 final IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
			
			 // If the index does not exist - need to create the index.
			 if (res.isExists()) 
				 return true;
		 
		 }catch (Exception e){
			 LOG.error("Error while checking if " + indexName + " exists. Error:" + e);
		 }
		 
		 
		 return false;
	 }
	 
	 	/**
		 * Create the spl index with the mapping 
		 * @param none
		 */
	 private boolean createSplIndex(){
		 
		 LOG.debug("createSplIndex called");
		 String indexName = "spl";
		 String type = "label";
		 try {
			 final IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
			
			 // If the index does not exist - need to create the index.
			 if (!res.isExists()) {
				String mappingFilePath = configReader.getSplSuggestionMappingFile();
				String settingFilePath = configReader.getSplSuggestionSettingsFile();
		 		
				// Get the mapping file 
				LOG.debug("Fetching SPL mappings file");				
		 		
		 		// Create the index with settings and mappings
		 		LOG.debug("Creating an index for spl");
				final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);  
				createIndexRequestBuilder.setSettings(IOUtils.toString(IngestSpl.class.getResourceAsStream(settingFilePath), "UTF-8" ));   
				createIndexRequestBuilder.addMapping(type, IOUtils.toString(IngestSpl.class.getResourceAsStream(mappingFilePath), "UTF-8"));
		        createIndexRequestBuilder.execute().actionGet();
				return true;

			}else { //if Index exist just check if we have enough records in the index.
				LOG.debug("Index already exists. Skipping creation of index and ingesting records");
				return false;
			}
		 }catch (Exception e){
			 LOG.error("Caught Exception While Creating Index:" + e );
			 return false;
		 }
	 }
	 
	 	/**
		 * Get SPL data from OpenFDA -- Currently limited to 5K records 
		 * @param int -Offset 
		 * @param int - maxRowsToFetch
		 */
	  private static JSONArray getSplData(int offset, int maxRowsToIngest){
			 InputStreamReader isReader = null;
			 
			 try {		 
			 HttpGet request = new HttpGet("https://api.fda.gov/drug/label.json?api_key=FhysguTaR3Lwq3DjwBS82FtwmnmPqnOQip0HZ7dF&limit=100&skip=" + offset);
	    	 HttpResponse response = httpclient.execute(request);
	    	 
	    	 
			 
			// Get data out of response
		        if(response.getStatusLine().getStatusCode() >= 200 &&  response.getStatusLine().getStatusCode() < 300) {
		        	 isReader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
					 BufferedReader reader = new BufferedReader(isReader);
			        StringBuffer sb = new StringBuffer();
			        String line = "";
			        while ((line = reader.readLine()) != null) {
			            sb.append(line);
			        }
			        JSONObject jsonObj = new JSONObject(sb.toString());
			        Iterator<?> keys = jsonObj.keys();

			        JSONObject meta = null;
			        JSONObject results= null;
			        /* Check if we got any values, if yes extract the results count from meta tag */
			        while( keys.hasNext() ) {
			            String key = (String)keys.next();
			            if(key.equalsIgnoreCase("meta")) {
			            	meta = jsonObj.getJSONObject("meta");
			            	results = meta.getJSONObject("results");
			            	break;
			            }
			            else if(key.equalsIgnoreCase("error")) {
			            	JSONObject error = jsonObj.getJSONObject("error");
			            	LOG.debug("Encountered Error: " + error.toString());
			            	isReader.close(); 
			            	isReader = null;
			            	return null;
			            }
			        }
			        
			        
			        if(results.getInt("total") > 0) {
			        	if(maxRowsToIngest == -1)
			        		maxRowsToIngest = results.getInt("total");
			        	JSONArray jsonarray = jsonObj.getJSONArray("results");
			        	isReader.close();
			        	return jsonarray;
			        }else {
			        	LOG.debug("End: " + "returned results: " + results.toString());
			        	isReader.close();
			        	return null;
			        }
			        
		        
	        } else {
	        	LOG.error("Encountered Error:" + response.getStatusLine().toString());
	        	return null;
	        }	 
	    } catch (Exception e) {
	    	LOG.error("Encountered Error:" + e);
	    	return null;
	    } finally {
	    	if (isReader != null) {
	    		try {
					isReader.close();
				} catch (IOException e) {
					LOG.error("Encountered Error:" + e);
				} finally {
					isReader = null;
				}
	    	}
	    }
	  }
	  
	  
	  	/**
			 * Get the total documents in spl index 
			 * @param String - path (OS Specific)
			 * @param Charset - encoding charset
			 */
	  public long getSplCount(){
		 
		  try {
			  QueryBuilder qb =  matchAllQuery();
			  CountResponse response = client.prepareCount("spl")
			        .setQuery(qb)
			        .execute()
			        .actionGet();
			  return(response.getCount());
		  }catch (Exception e){
			  
			  LOG.error("Encountered Error while trying to get total count of documents in SPL index: " + e);
			  return -1;
		  }
	  }
	  
	  
	  	/**
		 * Wait Until The Elasticsearch cluster attains the acceptableStatus.
		 * @param Client - Elastic  client
		 * @param EnumSet - Acceptable Statuses
		 */
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
					Thread.sleep(1000);
				}
				catch (    InterruptedException e) {
					LOG.debug("Sleep of 1sec got interrupted, while waiting for elasticsearch to come up. Error" + e);
				}
			}
			LOG.info("Status achieved: " + status + "!");
		}
		  
	  /**
		 * Shutdown the Elasticsearch cluster
		 */
//	  @PreDestroy
//	  public  void shutdownElastic( ){
//		  LOG.warn("Shutting Down Local Elasticsearch");
//		  client.admin().cluster().prepareNodesShutdown().execute().actionGet();
//		  LOG.debug("Done shutting down Elasticsearch node");
//	  }

	  
}