package com.bridgestone.bsro.aws.mobile.webservice;

import com.bridgestone.bsro.aws.mobile.webservice.error.Errors;

public class BSROWebServiceResponse {
	
	private Object payload;
	private String statusCode;
	private Errors errors;
	
	private String STATUS;
	private String Message;
	private String lastModifiedDesc;
	private String lastModifiedDate;
	
	public Object getPayload() {
		return payload;
	}
	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Errors getErrors() {
		return errors;
	}
	public void setErrors(Errors errors) {
		this.errors = errors;
	}
	
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String STATUS) {
		this.STATUS = STATUS;
	}
	public String getLastModifiedDesc() {
		return lastModifiedDesc;
	}
	public void setLastModifiedDesc(String lastModifiedDesc) {
		this.lastModifiedDesc = lastModifiedDesc;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		this.Message = message;
	}
	
	@Override
	public String toString() {
		return "BSROWebServiceResponse [payload=" + payload + ", statusCode="
				+ statusCode + ", errors=" + errors + ", STATUS=" + STATUS
				+ ", Message=" + Message + ", lastModifiedDesc="
				+ lastModifiedDesc + ", lastModifiedDate=" + lastModifiedDate
				+ "]";
	}
	
}
