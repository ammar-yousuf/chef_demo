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

package com.bna.ezrxlookup.integration.service;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bna.ezrxlookup.service.OpenFdaService;
import com.bna.ezrxlookup.service.SuggestService;
import com.bna.ezrxlookup.testing.AbstractTest;
import com.bna.ezrxlookup.testing.IntegrationTests;
import com.bna.ezrxlookup.web.DrugController;

/**
 * @author Ravi
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContextTest.xml")
@Category(IntegrationTests.class)
public class DrugControllerTests extends AbstractTest {
		
	private DrugController drugController;
	
	@Autowired
	private OpenFdaService openFdaService;
	
	@Autowired SuggestService suggestService;
	
	@Test
	public void getSearch() {
		
		try { 
			
			drugController = new DrugController();
			
			drugController.setOpenFdaService(openFdaService);
			
			drugController.setDrugName("Claritin PM");
			drugController.setSuggestService(suggestService);
						
			
			String returnPage = drugController.search();
			LOG.debug("Search page to go is " + returnPage);
			
		} catch (Exception e) {
			LOG.error(e);
		}
	}
}
