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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Utility class for Java to/from JSON string.
 *
 */
public final class JsonMapperUtil {

	/**
	 * Parse JSON string and return a list of object type.
	 * @param jsonString - input JSON string
	 * @param rootName - root node name
	 * @param type - object class type
	 * @return list of object class
	 */
	public static <T> List<T> readJsonToList(String jsonString, String rootName, Class<T> clazz) throws Exception {
	    List<T> objList = null;
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    try {
	        // get json content with root name
	        JsonNode root = objectMapper.readTree(jsonString).get(rootName);	        
	        TypeFactory tf = objectMapper.getTypeFactory();
	        JavaType listOfObjs = tf.constructCollectionType(ArrayList.class, clazz);
	        objList = objectMapper.readValue(root.traverse(), listOfObjs);	
	    } catch (Exception e) {
	        throw e;
	    }
	    
	    return  objList;
	}
	
}
