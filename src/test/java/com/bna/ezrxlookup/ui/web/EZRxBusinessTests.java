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
package com.bna.ezrxlookup.ui.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bna.ezrxlookup.testing.WebTests;


@Category(WebTests.class)
public class EZRxBusinessTests {
	
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		driver = (WebDriver) new FirefoxDriver();
		baseUrl = "http://52.7.4.133:8080";  //Ideally this should be local hosts and all test will be run in a docker container
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testEZRxLookupResultPageNoNegativeNoRecalls() throws Exception {
		try {
			driver.get(baseUrl + "/EZRxLookup/");
			driver.findElement(By.id("j_idt5:j_idt18:drugName_input")).clear();
			driver.findElement(By.id("j_idt5:j_idt18:drugName_input")).sendKeys("");
			driver.findElement(By.id("j_idt5:j_idt18:drugName_input")).clear();
			driver.findElement(By.id("j_idt5:j_idt18:drugName_input")).sendKeys(
					"51439-007-02");
			driver.findElement(By.id("j_idt5:j_idt18:search")).click();
			for (int second = 0;; second++) {
		    	if (second >= 10) fail("timeout");
		    	try { if (isElementPresent(By.cssSelector("label"))) break; } catch (Exception e) {}
		    	Thread.sleep(1000);
		    }
			Thread.sleep(3000);
			assertEquals("ACNE CLEARNING TREATMENT FACE", driver.findElement(By.id("j_idt5:j_idt19:cmdBName")).getText());
			
			WebElement element = driver.findElement(By.id("j_idt5:j_idt19:j_idt28"));
			String path = element.getAttribute("src");
			assert(path.contains("green"));
			 
		} catch (Error e) {
			verificationErrors.append(e);

		}
	}

	@Test
	public void testEZRxLookupResultPageNegativeEventsNoRecalls()
			throws Exception {
		try {
			driver.get(baseUrl + "/EZRxLookup/");
			driver.findElement(By.id("j_idt5:j_idt18:drugName_input")).clear();
			driver.findElement(By.id("j_idt5:j_idt18:drugName_input")).sendKeys("");
			driver.findElement(By.id("j_idt5:j_idt18:drugName_input")).clear();
			driver.findElement(By.id("j_idt5:j_idt18:drugName_input")).sendKeys(
					"53746-078-01");
			driver.findElement(By.id("j_idt5:j_idt18:search")).click();
			for (int second = 0;; second++) {
		    	if (second >= 10) fail("timeout");
		    	try { if (isElementPresent(By.cssSelector("label"))) break; } catch (Exception e) {}
		    	Thread.sleep(1000);
		    }
			Thread.sleep(3000);
			assertEquals("ESTERIFIED ESTROGENS AND METHYLTESTOSTERONE", driver.findElement(By.id("j_idt5:j_idt19:cmdBName")).getText());
			
			WebElement element = driver.findElement(By.id("j_idt5:j_idt19:j_idt28"));
			String path = element.getAttribute("src");
			assert(path.contains("yellow"));
		} catch (Error e) {
			verificationErrors.append(e);

		}
	}

	@Test
	public void testEZRxLookupResultPageRecallsFound() throws Exception {
		try {
			driver.get(baseUrl + "/EZRxLookup/");
			driver.findElement(By.id("j_idt5:j_idt18:drugName_input")).clear();
			driver.findElement(By.id("j_idt5:j_idt18:drugName_input")).sendKeys("");
			driver.findElement(By.id("j_idt5:j_idt18:drugName_input")).clear();
			driver.findElement(By.id("j_idt5:j_idt18:drugName_input")).sendKeys(
					"43598-209-53");
			driver.findElement(By.id("j_idt5:j_idt18:search")).click();
			for (int second = 0;; second++) {
		    	if (second >= 10) fail("timeout");
		    	try { if (isElementPresent(By.cssSelector("label"))) break; } catch (Exception e) {}
		    	Thread.sleep(1000);
		    }
			Thread.sleep(3000);
			assertEquals("AMOXICILLIN", driver.findElement(By.id("j_idt5:j_idt19:cmdBName")).getText());
			WebElement element = driver.findElement(By.id("j_idt5:j_idt19:j_idt28"));
			String path = element.getAttribute("src");
			assert(path.contains("red"));
		} catch (Error e) {
			verificationErrors.append(e);

		}
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
