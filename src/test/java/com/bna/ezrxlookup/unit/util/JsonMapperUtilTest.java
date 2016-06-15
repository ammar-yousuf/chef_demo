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

package com.bna.ezrxlookup.unit.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.bna.ezrxlookup.domain.DrugReaction;
import com.bna.ezrxlookup.testing.AbstractTest;
import com.bna.ezrxlookup.testing.UnitTests;
import com.bna.ezrxlookup.util.JsonMapperUtil;

/**
 * Unit test for JasonMapperUtil class
 *
 */
@Category(UnitTests.class)
public class JsonMapperUtilTest extends AbstractTest {
	
	private static final String RESULTS_NODE = "results";
	
	@Test
	public void parseOpenFdaResponseResults() {
		String response = "";
		List<DrugReaction> resultSet;

		try {
			File file = new File(getUserDir() + EVENT_SAMPLE_FILE);		
			response = FileUtils.readFileToString(file);
		} catch (IOException e1) {
			LOG.error(e1);
		}
		
		// expect successful test
		try {
			resultSet = JsonMapperUtil.readJsonToList(response, RESULTS_NODE,  DrugReaction.class);			
			for (DrugReaction result : resultSet) 
				LOG.debug("parseOpenFdaResponseResults: " + result);			
		} catch (Exception e) {
			LOG.error(e);
		}
		
		// expect failed test
		try {
			resultSet = JsonMapperUtil.readJsonToList(response, "result",  DrugReaction.class);			
			for (DrugReaction result : resultSet) 
				LOG.debug("parseOpenFdaResponseResults: " + result);			
		} catch (Exception e) {
			LOG.error(e);
		}

	}
	
}
