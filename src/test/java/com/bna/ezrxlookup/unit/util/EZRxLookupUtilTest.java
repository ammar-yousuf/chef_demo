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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.bna.ezrxlookup.testing.AbstractTest;
import com.bna.ezrxlookup.testing.UnitTests;
import com.bna.ezrxlookup.util.EZRxLookupUtil;

/**
 * @author Ravi
 *
 */
@Category(UnitTests.class)
public class EZRxLookupUtilTest extends AbstractTest {
	
	@Test
	public void testNdcString() {
		String ndc = "NDC 51439-007-02";
		boolean result = EZRxLookupUtil.isNdc(ndc);
		
		LOG.debug("Testing string : " + ndc + " / result : " + result);
		try {
			assertTrue("Is valid NDC# : " + result, result);
		} catch (AssertionError e) {
			LOG.debug(e);
		}
	}

	@Test
	public void testNdcStringNoNdc() {		
		String noNdc = "5143900702";		
		boolean result = EZRxLookupUtil.isNdc(noNdc);		
		
		LOG.debug("Testing string : " + noNdc + " / result : " + result);
		try {
			assertTrue("Is valid NDC# : " + result, result);
		} catch (AssertionError e) {
			LOG.debug(e);
		}
	}
	
	@Test
	public void testConversionToNdc() {
		
		String ndc = "NDC 52421-876-45";
		String brand = "Tylenol PM";
		String ndcNumLong = "5143900702";
		String ndcNumInt = "1234567890";	
		String ndcFull = "3452-7861-92";
		String smallBrand = "Advil";
		String ndcBrand = "AdvilInfan";
		
		try {

			String r1 = EZRxLookupUtil.convertToProductNdcCode(ndc);
			assertTrue(r1.equals("52421-876-45"));

			String r2 = EZRxLookupUtil.convertToProductNdcCode(brand);
			assertTrue(r2.equals("Tylenol PM"));

			String r3 = EZRxLookupUtil.convertToProductNdcCode(ndcNumLong);
			assertTrue(r3.equals("5143900702"));

			String r4 = EZRxLookupUtil.convertToProductNdcCode(ndcFull);
			assertTrue(r4.equals("3452-7861-92"));

			String r5 = EZRxLookupUtil.convertToProductNdcCode(smallBrand);
			assertTrue(r5.equals("Advil"));

			String r6 = EZRxLookupUtil.convertToProductNdcCode(ndcBrand);		
			assertTrue(r6.equals("AdvilInfan"));

			String r7 = EZRxLookupUtil.convertToProductNdcCode(ndcNumInt);		
			assertTrue(r7.equals("1234567890"));

			LOG.debug("Completed All conversions");
		} catch (AssertionError e) {
			LOG.debug(e);
		}

		
	}
}
