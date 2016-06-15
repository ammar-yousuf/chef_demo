/**
 * 
 */
package com.bna.ezrxlookup.util;

import org.apache.log4j.Logger;

/**
 * @author Ravi
 * 
 * This class has the utility methods to facilitate commonly
 * reused methods
 *
 */
public class EZRxLookupUtil {
	
	private static final Logger LOG = Logger.getLogger(EZRxLookupUtil.class);
	
	private static final String SPACE = " ";
	private static final String BLANK = "";
	private static final String NDC_PREFIX = "ndc";
	private static final String NDC_SEPARATOR = "-";
	
		
	public static boolean isValid(String str) {
		return true;
	}
	
	/**
	 * 
	 * @param str - user input search string
	 * @return - a ndc formatted string (XXXXX-XXX-XX) if it is a ndc number
	 * 			-return the actual string otherwise
	 */
	public static String convertToProductNdcCode(String str) {
		
		String ret = str;		
		
		//First convert to lower case for easy comparison
		String tmp = str.toLowerCase().replace(SPACE, BLANK);
		
		//check if it starts with ndc 
		String noNdc = BLANK;
		if (tmp.startsWith(NDC_PREFIX)) {
			noNdc = tmp.replaceFirst(NDC_PREFIX, BLANK);	
		} else {
			noNdc = tmp;
		}
			
		//Assume its a valid ndc# with embedded '-'	
		if (noNdc.length() == 12) { 
			if (EZRxLookupUtil.isNdc(noNdc)) {
				return noNdc;
			}
		} 
					
		return ret;				
	}
	
	/**
	 * 
	 * @param noNdc in XXXXX-XXX-XX or XXXX-XXXX-XX or XXXXX-XXXX-X - 12 Total Length
	 * 			Any 10 digit number with two dashes in between
	 * @return True if it contains two "-"s and 10 numbers
	 * 			False otherwise
	 */
	public static boolean isNdc(String noNdc) {
		boolean bFlag = false;
		
		if (noNdc.length() != 12)
			return bFlag;
		
		// Strip embedded "-" and check if numeric
		// There should only be two "-" total
		
		String noDash = noNdc.replaceAll(NDC_SEPARATOR, BLANK);
		LOG.debug("After stripping dashes: " + noDash);
		
		if (noDash.length() != 10) {
			LOG.debug("Not a Product NDC, 2 hyphens NOT found: Will use brand name lookup");
			return bFlag;
		}
		
		// Check if it is all numeric
		try {
			Long.parseLong(noDash);
			bFlag = true;
		} catch (NumberFormatException ex) {
			LOG.debug("Not a Product NDC, Non-Numerics found, will use brand name lookup");
		}
		
		return bFlag;
	}

}
