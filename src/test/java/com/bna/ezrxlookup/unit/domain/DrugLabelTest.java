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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.bna.ezrxlookup.domain.DrugLabel;
import com.bna.ezrxlookup.testing.AbstractTest;
import com.bna.ezrxlookup.testing.UnitTests;

@Category(UnitTests.class)
public class DrugLabelTest extends AbstractTest {
	
    @Test
    public void canConstructAPersonWithAName() {
    	final String brand = "TYLENOL";
    	final String manufacture = "MCNEIL";
    	
        DrugLabel label1 = new DrugLabel();
        label1.setBrandName(brand);
        label1.setManufactureName(manufacture);
        
        DrugLabel label2 = new DrugLabel("ADVIL", "TEST");
        DrugLabel label3 = new DrugLabel(brand, manufacture);
        
        LOG.debug("toString: " + label1.toString());
        LOG.debug("hashCode: " + label2.hashCode());
        LOG.debug("compareTo: " + label1.compareTo(label2));
       
        try {
	        assertNotEquals(label1, label2);
	        assertEquals(label1, label3);
	        
	        assertTrue(label1.equals(label3));
	        assertFalse(label1.equals(null));
	        assertFalse(label3.equals(manufacture));
	        
        } catch(AssertionError e) {
        	LOG.error(e);
        }
    }
}
