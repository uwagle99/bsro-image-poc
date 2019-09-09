package com.bridgestone.bsro.aws.mobile.webservice;

public enum BSROWebServiceResponseCode {
	
	SUCCESSFUL("SUCCESSFUL"),
	VALIDATION_ERROR("VALIDATION_ERROR"),
	DATABASE_ERROR("DATABASE_ERROR"),
	BUSINESS_SERVICE_ERROR("BUSINESS_SERVICE_ERROR"),
	BUSINESS_SERVICE_INFO("BUSINESS_SERVICE_INFO"),
	UNKNOWN_ERROR("UNKNOWN_ERROR"),
	UNAUTHORIZED_ERROR("UNAUTHORIZED_ERROR");
	
	String errorCode;
	
	BSROWebServiceResponseCode(String errorCode){
		this.errorCode = errorCode;
	}	

}
