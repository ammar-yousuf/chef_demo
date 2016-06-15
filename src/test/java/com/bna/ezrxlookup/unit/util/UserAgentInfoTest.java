/* Copyright 2012-2015 Bart & Associates, Inc
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bna.ezrxlookup.testing.AbstractTest;
import com.bna.ezrxlookup.testing.UnitTests;
import com.bna.ezrxlookup.web.handler.UserAgentInfo;

/**
 * Unit test for ConfigReader class
 *
 */
@Category(UnitTests.class)
public class UserAgentInfoTest extends AbstractTest {
	
	private static String userAgentChrome = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36"; 
	private	static String httpAccept= "application/xml, text/xml, */*; q=0.01";
	private static String userAgentIE = "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko"; 
	private static String userAgentiPad = "Mozilla/5.0 (iPad; CPU OS 8_3 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12F69 Safari/600.1.4";
	private static String userAgentiPhone = "Mozilla/5.0 (iPhone; CPU iPhone OS 8_3 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12F70 Safari/600.1.4";
	
	@Test
	public void userAgentInfoTests() {
		
		UserAgentInfo ipad = new UserAgentInfo(userAgentiPad, httpAccept);
		assertTrue(ipad.detectIos());
		assertTrue(ipad.detectIpad());
		assertFalse(ipad.detectIphone());
		assertFalse(ipad.detectIphoneOrIpod());
		assertFalse(ipad.detectIpod());
		assertFalse(ipad.detectAndroid());
		assertFalse(ipad.detectAndroidPhone());
		assertFalse(ipad.detectAndroidTablet());
		assertFalse(ipad.detectAndroidWebKit());
		assertFalse(ipad.detectGoogleTV());
		assertTrue(ipad.detectWebkit());
		assertFalse(ipad.detectS60OssBrowser());
		assertFalse(ipad.detectSymbianOS());
		assertFalse(ipad.detectWindowsPhone7());
		assertFalse(ipad.detectWindowsMobile());
		assertFalse(ipad.detectBlackBerry());
		assertFalse(ipad.detectBlackBerryTablet());
		assertFalse(ipad.detectBlackBerryWebKit());
		assertFalse(ipad.detectBlackBerryTouch());
		assertFalse(ipad.detectBlackBerryHigh());
		assertFalse(ipad.detectBlackBerryLow());
		assertFalse(ipad.detectPalmOS());
		assertFalse(ipad.detectPalmWebOS());
		assertFalse(ipad.detectWebOSTablet());
		assertFalse(ipad.detectGarminNuvifone());
		assertFalse(ipad.detectSmartphone());
		assertFalse(ipad.detectBrewDevice());
		assertFalse(ipad.detectDangerHiptop());
		assertFalse(ipad.detectOperaAndroidPhone());
		assertFalse(ipad.detectOperaAndroidTablet());
		assertFalse(ipad.detectWapWml());
		assertFalse(ipad.detectKindle());
		assertFalse(ipad.detectMobileQuick());
		assertFalse(ipad.detectSonyPlaystation());
		assertFalse(ipad.detectNintendo());
		assertFalse(ipad.detectXbox());
		assertFalse(ipad.detectGameConsole());
		assertFalse(ipad.detectMidpCapable());
		assertFalse(ipad.detectMaemoTablet());
		assertFalse(ipad.detectArchos());
		assertFalse(ipad.detectSonyMylo());
		assertFalse(ipad.detectMobileLong());
		assertTrue(ipad.detectTierTablet());
		assertFalse(ipad.detectTierIphone());
		assertFalse(ipad.detectTierRichCss());
		assertFalse(ipad.detectTierOtherPhones());
		
		UserAgentInfo iPhone = new UserAgentInfo(userAgentiPhone, httpAccept);
		assertTrue(iPhone.detectIos());
		assertFalse(iPhone.detectIpad());
		assertTrue(iPhone.detectIphone());
	}
	
	
	
}
