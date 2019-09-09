package com.bridgestone.bsro.aws.mobile.util;

public class MobileAppUserValidation {
	public static String getErrorMessage(String siteName, String sourceType, String quoteId, String firstName, String lastName, String emailAddress) {
		StringBuffer buf = new StringBuffer();
		String errmsg = null;
		int errFieldsCount = 0;
		if (isNullOrEmpty(siteName)) {
				buf.append("Site Name");
				++errFieldsCount;
		}
		if (isNullOrEmpty(sourceType)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("Source Type");
			++errFieldsCount;
		}
		if (isNullOrEmpty(quoteId)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("Quote Id");
			++errFieldsCount;
		}
		if (isNullOrEmpty(firstName)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("Firstname");
			++errFieldsCount;
		}
		if (isNullOrEmpty(lastName)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("Lastname");
			++errFieldsCount;
		}
		if (isNullOrEmpty(emailAddress)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("Email Address");
			++errFieldsCount;
		}
		
		errmsg = (errFieldsCount == 1) ? "Missing parameter value for field " : "Missing parameter value for fields ";
		errmsg += buf.toString();
		return errmsg;
	}
	
	public static boolean isNullOrEmpty(String str) {
        
        if (str == null || str.trim().equals("") || str.trim().equals("null")) {
            return true;
        } 
        return false;
    }
}
