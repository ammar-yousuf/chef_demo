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
package com.bna.ezrxlookup.web.handler;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.bna.ezrxlookup.web.DrugController;

public class MobileFilter implements Filter {
	
	private static final Logger LOG = Logger.getLogger(MobileFilter.class);
	
	private static final String USER_AGENT_HDR 	= "user-agent";
	private static final String ACCEPT_HDR 		= "Accept";
	private static final String INDEX_PAGE 		= "faces/indexmobile.xhtml";
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Auto-generated method stub		
	}
	
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) 
			throws IOException, ServletException {
		DrugController dctrl = new DrugController();
		dctrl.setDrugName("");
		HttpServletRequest req = (HttpServletRequest) arg0;
		String userAgent = req.getHeader(USER_AGENT_HDR);
		String accept = req.getHeader(ACCEPT_HDR);

		if (userAgent != null && accept != null) {
			
			UserAgentInfo agent = new UserAgentInfo(userAgent, accept);
			HttpServletResponse res = (HttpServletResponse) arg1;
			LOG.debug(userAgent + "" + agent);

			if (agent.isMobileDevice()) {
				LOG.debug(req.getContextPath());
				res.sendRedirect(INDEX_PAGE);
			} else {
				arg2.doFilter(arg0, arg1);
			}
		}
	}
	
	@Override
	public void destroy() {
		// Auto-generated method stub		
	}
}
