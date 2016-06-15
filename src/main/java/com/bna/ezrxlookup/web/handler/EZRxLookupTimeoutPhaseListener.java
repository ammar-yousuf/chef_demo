/**
 * 
 */
package com.bna.ezrxlookup.web.handler;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * @author Ravi
 *
 */
public class EZRxLookupTimeoutPhaseListener implements PhaseListener {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(EZRxLookupTimeoutPhaseListener.class);
	
	/* (non-Javadoc)
	 * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
	 */
	@Override
	public void afterPhase(PhaseEvent event) {
		
		/*
		 * Fall thru to base handler
		 */
	}

	/* (non-Javadoc)
	 * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
	 */
	@Override
	public void beforePhase(PhaseEvent event) {
				  
		  FacesContext context = event.getFacesContext();
	        ExternalContext ext = context.getExternalContext();
	        HttpSession session = (HttpSession) ext.getSession(false);
	        boolean newSession = (session == null) || (session.isNew());
	        boolean postback = !ext.getRequestParameterMap().isEmpty();
	        boolean timedout = postback && newSession;
	        if (timedout) {
	        	if (LOG != null) {
	        		LOG.debug("Session timed out - Redirecting to the home page now...........");
	        	}
	        	
	            Application app = context.getApplication();
	            ViewHandler viewHandler = app.getViewHandler();
	            UIViewRoot view = viewHandler.createView(context, "/" + "index.xhtml");
	            context.setViewRoot(view);
	            context.renderResponse();
	            try {
	                viewHandler.renderView(context, view);
	                context.responseComplete();
	            } catch (Exception e) {
	                throw new FacesException("Session timed out", e);
	            }
	         }
	
	}

	/* (non-Javadoc)
	 * @see javax.faces.event.PhaseListener#getPhaseId()
	 */
	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
