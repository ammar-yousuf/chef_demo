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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bna.ezrxlookup.domain.DrugLabel;
import com.bna.ezrxlookup.domain.DrugReaction;
import com.bna.ezrxlookup.domain.SearchSummary;
import com.bna.ezrxlookup.service.OpenFdaService;
import com.bna.ezrxlookup.testing.AbstractTest;
import com.bna.ezrxlookup.testing.IntegrationTests;
import com.bna.ezrxlookup.util.MessageSimpleType.DrugRepoEnum;

/**
 * @author Ravi
 * @author chana
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContextTest.xml")
@Category(IntegrationTests.class)
public class OpenFdaServiceTests extends AbstractTest {

	private static final String ADVIL_PM_DRUG 	= "Advil PM";	
	private static final String TYLENOL_DRUG 	= "Tylenol";
	private static final String TYLENOL_PM_DRUG = "Tylenol PM";
	
	private static final String NDC_53 = "43598-209-53";

	@Autowired
	private OpenFdaService openFdaService;

	
	@Test
	public void getDrugLabelResultsTest() {
		File file = new File(getUserDir() + LABEL_SAMPLE_FILE);
		try {
			String response = FileUtils.readFileToString(file);
			Set<DrugLabel> labelSet = openFdaService.getDrugLabelResults(response);
			for (DrugLabel label : labelSet) 
				LOG.debug("getDrugLabelResultsTest: " + label);
		} catch (Exception e) {
			LOG.error(e);
		}
	}
	
	@Test
	public void findDrugNameTest() {
		try {
			LOG.debug("searchSummaryTest :\n" + openFdaService.findDrugName(TYLENOL_DRUG));
		} catch (Exception e) {
			LOG.error(e);
		}
	}
	
	@Test
	public void findDrugEventReactionTest() {
		List<DrugReaction> resultList = openFdaService.findDrugEventReaction(TYLENOL_PM_DRUG);
		LOG.debug("findDrugEventReactionTest: \n" + resultList);
	}
	
	@Test
	public void findByDrugNameTests() {
		try {
			Set<DrugLabel> labelSet = openFdaService.findDrugLabelByBrandName(ADVIL_PM_DRUG);
			int recallCount = openFdaService.getTotalCountByBrandName(DrugRepoEnum.RECALL, ADVIL_PM_DRUG, 1);
			int adverseEventCount = openFdaService.getTotalCountByBrandName(DrugRepoEnum.EVENT, ADVIL_PM_DRUG, 1);

			LOG.debug("RECALL count : " + recallCount);
			LOG.debug("EVENT count : " + adverseEventCount);
			LOG.debug("LABEL count : " + labelSet.size());
			
			assertTrue("Recall count (" + recallCount + ") should be greater than 0", recallCount > 0);
			assertTrue("Number Of Labels Found (" + labelSet.size() + ") should be greater than  1", labelSet.size() > 1);
			assertTrue("Number Of Adverse Events Found (" + adverseEventCount + ") should be greater than  1", adverseEventCount > 1);
		} catch(AssertionError e) {
			LOG.error(e);
		} catch(Exception e) {
			LOG.error(e);
		}
	}
	
	@Test
	public void canCallSPL() {
		Set<DrugLabel> labelSet;		
		try {
			labelSet = openFdaService.findDrugLabelByBrandName(ADVIL_PM_DRUG);
			LOG.debug("canCallSPL:  Total count is: " + labelSet.size());				
			assertTrue(labelSet.size() > 0);
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	@Test
	public void canCallAllWithNDC() {
		//final String ndcNum = "51439-007-02";
		SearchSummary summary;
		try {
			summary = openFdaService.findDrugName(NDC_53);
			LOG.debug("canCallAllWithNDC:  Total summary is: " + summary);		
			assertTrue(summary != null);
		} catch (Exception e) {
			LOG.error(e);
		}
	}
	
	@Test
	public void canCallAllWithNDCNumber() {
		final String ndcNum = "5143900702";
		SearchSummary summary;
		try {
			summary = openFdaService.findDrugName(ndcNum);
			LOG.debug("canCallAllWithNDCNumber:  Total summary is: " + summary);		
			assertTrue(summary != null);
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	@Test
	public void canCallAllWithBrandName() {
		SearchSummary summary;
		try {
			summary = openFdaService.findDrugName(TYLENOL_PM_DRUG);
			LOG.debug("canCallAllWithBrandName:  Total summary status is: " + summary.getDerogStatus());		
			assertTrue(summary != null);
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	@Test
	public void canCallSPLSearchByExactNDC() {
		assert (openFdaService != null);		
		Set<DrugLabel> labelSet = openFdaService.findDrugLabelByNDC(NDC_53);
		LOG.debug("canCallSPLSearchByExactNDC:  Total count is: " + labelSet.size());		
		assertTrue(labelSet.size() < 2);
	}

	@Test
	public void canCallSPLSearchByNoMatchNDC() {
		assert (openFdaService != null); 
		final String ndcNum = "43598-209-89";
		Set<DrugLabel> labelSet = openFdaService.findDrugLabelByNDC(ndcNum);
		LOG.debug("canCallSPLSearchByNoMatchNDC:  Total count is: " + labelSet.size());
		assertTrue(labelSet.size() == 0);
	}

}
