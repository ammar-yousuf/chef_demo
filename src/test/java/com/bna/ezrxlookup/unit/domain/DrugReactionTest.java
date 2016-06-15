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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.experimental.categories.Category;

import com.bna.ezrxlookup.domain.DrugReaction;
import com.bna.ezrxlookup.testing.AbstractTest;
import com.bna.ezrxlookup.testing.UnitTests;

/**
 *
 * @author chana
 *
 */
@Category(UnitTests.class)
public class DrugReactionTest extends AbstractTest {
	
	@Test
	public void ojbectTest() {
		final String term = "Pain";
		final Integer count = 3;
		
		DrugReaction obj1 = new DrugReaction();
		obj1.setTerm(term);
		obj1.setCount(count);
		
		DrugReaction obj2 = new DrugReaction(term, count);
		DrugReaction obj3 = new DrugReaction("Tire", count);
		
		LOG.debug("term: " + obj1.getTerm() + " / " + obj1.getCount());
		LOG.debug("toString test: " + obj1.toString());
		LOG.debug("hashCode: " + obj2.hashCode());
		LOG.debug("compareTo obj1 & obj2: " + obj1.compareTo(obj2));
		LOG.debug("compareTo obj1 & obj3: " + obj1.compareTo(obj3));
		
		try {
			assertEquals(obj1, obj2);
			assertNotEquals(obj1, obj3);
			
			assertTrue(obj1.equals(obj2));
			assertFalse(obj3.equals(null));
			assertFalse(obj3.equals(term));
		} catch(AssertionError e) {
			LOG.error(e);
		}
	}

}
