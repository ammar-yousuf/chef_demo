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

package com.bna.ezrxlookup.util;


/**
 * Utility class for maintaining list of enum classes.
 *
 */
public final class MessageSimpleType {
		
	public enum DerogatoryEnum {
		// label not found
		LABEL_NOT_FOUND ("", false, "FDA-Approved Label Not Found"),
		
		MULTI_LABEL_FOUND ("", false, "Multiple Labels Found, Please Refine Your Search"),
		
		// label found, no events, no recalls
		LABEL_FOUND_GREEN ("green.gif", false, "No Recalls and Adverse Events / Negative Reports Found"),
		
		// label found, has events, no recalls
		LABEL_FOUND_YELLOW ("yellow.gif", true, "Adverse Events / Negative Reports Found"),
		
		// label found, has recalls, events (DON'T CARE)
		LABEL_FOUND_RED ("red.gif", false, "Recall Found"),
		
		// label found, has recalls and events
		ADVERSE_RECALL_RED ("red.gif", true, "Recall Found and Adverse Events / Negative Reports Found");
		
		private String imageFile;
		private boolean showDetails;
		private String statusLabel;
		
		DerogatoryEnum(String imageFile, boolean showDetails, String statusLabel) {
			this.setImageFile(imageFile);
			this.setShowDetails(showDetails);
			this.setStatusLabel(statusLabel);
		}
		
		/**
		 * @return the imageFile
		 */
		public String getImageFile() {
			return imageFile;
		}
		/**
		 * @param imageFile the imageFile to set
		 */
		public void setImageFile(String imageFile) {
			this.imageFile = imageFile;
		}
		/**
		 * @return the statusLabel
		 */
		public String getStatusLabel() {
			return statusLabel;
		}
		/**
		 * @param statusLabel the statusLabel to set
		 */
		public void setStatusLabel(String statusLabel) {
			this.statusLabel = statusLabel;
		}
		/**
		 * @return the showDetails
		 */
		public boolean getShowDetails() {
			return showDetails;
		}
		/**
		 * @param showDetails the showDetails to set
		 */
		public void setShowDetails(boolean showDetails) {
			this.showDetails = showDetails;
		}
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(name());
			sb.append(" [image: ").append(this.getImageFile());
			sb.append(", showDetails: ").append(this.getShowDetails());
			sb.append(", status: ").append(this.getStatusLabel()).append("]");
			return sb.toString();
		}
	};
	
	/**
	 * OpenFDA repository tags
	 */
	public enum DrugRepoEnum {
		SPL,
		EVENT,
		RECALL;
	};

}
