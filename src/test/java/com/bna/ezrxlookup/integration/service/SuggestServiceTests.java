/**
 * 
 */
package com.bna.ezrxlookup.integration.service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bna.ezrxlookup.service.SuggestService;
import com.bna.ezrxlookup.testing.AbstractTest;
import com.bna.ezrxlookup.testing.IntegrationTests;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContextTest.xml")
@Category(IntegrationTests.class)
public class SuggestServiceTests extends AbstractTest {
	
	@Resource
	private SuggestService suggestService;
	
	
	@Test
	public void testSuggest() {

		try {
			Thread.sleep(10);

			List<String> resultList = suggestService.findSuggestions("Tyl");
			LOG.debug("SuggestedList1 : " + resultList.toString());

			resultList = suggestService.findSuggestions("Adv");
			LOG.debug("SuggestedList2 : " + resultList.toString());

			resultList = suggestService.findSuggestions("Meth");
			LOG.debug("SuggestedList3 : " + resultList.toString());

		} catch (InterruptedException e) {
			LOG.error(e);
		} catch (Exception e) {
			LOG.error(e);
		}

	}

}
