package com.bridgestone.bsro.aws.mobile.util;

public class MailerUtility {
	public String getDoNotReply(String siteName) {
		if(siteName.equalsIgnoreCase("TP")){
			return "DO-NOT-REPLY<webmaster@tiresplus.com>";
		}else if(siteName.equals("FCAC") || siteName.equals("ADMIN")){
			return "DO-NOT-REPLY<webmaster@firestonecompleteautocare.com>";
		}
		return "";
	}
	
	public String getSiteNameString(String siteName) {
		if(siteName.equalsIgnoreCase("TP")){
			return "Tires Plus Mobile App";
		}else if(siteName.equals("FCAC")){
			return "Firestone Mobile App";
		}
		return "";
	}

	public String generateEmailBody(String newPassword, String appName) {
		StringBuffer sb = new StringBuffer();
		sb.append("You recently requested that your "+ appName +" password be reset.\r\n");
		sb.append("Your new password is "+newPassword+".\n\n");
		sb.append("If you have not requested this change, please contact administrator using Contact Us feature.\n\n");
		sb.append("Thank You,\n");
		sb.append(getSiteNameString(appName) + " Team.\r\n");
		return sb.toString();
	}
}
