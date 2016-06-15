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

package com.bna.ezrxlookup.integration.util;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bna.ezrxlookup.service.OpenFdaService;
import com.bna.ezrxlookup.testing.AbstractTest;
import com.bna.ezrxlookup.testing.IntegrationTests;
import com.bna.ezrxlookup.util.IngestSpl;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContextTest.xml")
@Category(IntegrationTests.class)
public class IngestSplTest extends AbstractTest {

	@Autowired
	private IngestSpl ingestSpl;

	@Test
	@Ignore // The ingestSpl will ingest records on statup now
	public void loadTheSplDataFromOpenFda() {
		LOG.debug("Invoking the ingestSplData Test");
			try {
			Thread.sleep(10);
			int i = ingestSpl.ingestSplData(300);
			LOG.debug("Ingested a total of :" + i);
			LOG.debug("Total documents in SPL: " + ingestSpl.getSplCount());
			
			//ingestSpl.shutdownElastic();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getStackTrace().toString());
		}
		
	}
}