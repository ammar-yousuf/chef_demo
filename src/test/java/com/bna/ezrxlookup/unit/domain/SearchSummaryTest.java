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

package com.bna.ezrxlookup.unit.domain;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.bna.ezrxlookup.domain.DrugLabel;
import com.bna.ezrxlookup.domain.SearchSummary;
import com.bna.ezrxlookup.testing.AbstractTest;
import com.bna.ezrxlookup.testing.UnitTests;
import com.bna.ezrxlookup.util.MessageSimpleType.DerogatoryEnum;

@Category(UnitTests.class)
public class SearchSummaryTest extends AbstractTest {
	
	@Test
	public void instantiateTest() {
		DrugLabel label1 = new DrugLabel("Test1", "B&A");
		DrugLabel label2 = new DrugLabel("Test2", "FDA");
		
		SearchSummary ss = new SearchSummary();
		assertNull(ss.getDrugLabel());
		
		ss.getDrugLabelSet().add(label2);
		ss.getDrugLabelSet().add(label1);
		
		LOG.debug(ss.getDrugLabelSet());
		
		DrugLabel temp = ss.getDrugLabel();
		LOG.debug("label : " + temp.toString());
		LOG.debug("hashCode : " + temp.hashCode());
		LOG.debug("compareTo : " + label1.compareTo(label2));
		
		try {
			ss.setDerogStatus(DerogatoryEnum.MULTI_LABEL_FOUND);	
			assertTrue(ss.isMultiLabelFoundForDerog());
			
			ss.setDerogStatus(DerogatoryEnum.LABEL_NOT_FOUND);
			assertTrue(ss.isLabelNotFoundForDerog());
			
			assertNotEquals(label1, label2);
		} catch(AssertionError e) {
			LOG.error(e);
		}
	}

}
