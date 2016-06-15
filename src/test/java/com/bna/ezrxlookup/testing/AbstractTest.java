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

package com.bna.ezrxlookup.testing;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;

/**
 * Based class for unit test
 * @author chana
 *
 */
public abstract class AbstractTest {

	protected final Logger LOG = Logger.getLogger(this.getClass());
	
	protected final String USER_DIR_PROP = "user.dir";
	protected final String LOG4J_FILE	 = "/src/main/resources/log4j.xml";
	
	protected static final String EVENT_SAMPLE_FILE = "/src/test/resources/samples/event_reaction_sample.txt";
	protected static final String LABEL_SAMPLE_FILE = "/src/test/resources/samples/label_response_sample.txt";
	
	
	@Before
	public void init() {
		PropertyConfigurator.configure(getUserDir() + LOG4J_FILE);
	}

	protected String getUserDir() {
		return System.getProperty(USER_DIR_PROP);
	}
}
