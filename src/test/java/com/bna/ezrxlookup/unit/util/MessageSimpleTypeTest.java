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

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.bna.ezrxlookup.testing.AbstractTest;
import com.bna.ezrxlookup.testing.UnitTests;
import com.bna.ezrxlookup.util.MessageSimpleType.DerogatoryEnum;
import com.bna.ezrxlookup.util.MessageSimpleType.DrugRepoEnum;

/**
 * Perform unit test on MessageSimpleType class
 */
@Category(UnitTests.class)
public class MessageSimpleTypeTest extends AbstractTest {

	@Test 
	public void derogatoryEnumTest() {
		for (DerogatoryEnum value : DerogatoryEnum.values())
			LOG.debug("DerogatoryEnum value : " + value);
	}
	
	@Test
	public void DrugRepoEnumTest() {
		for (DrugRepoEnum value : DrugRepoEnum.values())
			LOG.debug("DrugRepoEnum value : " + value);
	}
}
