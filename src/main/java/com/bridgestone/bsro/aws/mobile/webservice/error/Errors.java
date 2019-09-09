package com.bridgestone.bsro.aws.mobile.webservice.error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Errors implements Serializable {
	private static final long serialVersionUID = 1566817002233590072L;
	private List<String> globalErrors = new ArrayList<String>();
	private Map<String, List<String>> fieldErrors = new HashMap<String, List<String>>();
	private String exceptionStackTrace = null;
	public List<String> getGlobalErrors() {
		return globalErrors;
	}
	public void setGlobalErrors(List<String> globalErrors) {
		this.globalErrors = globalErrors;
	}
	public Map<String, List<String>> getFieldErrors() {
		return fieldErrors;
	}
	public void setFieldErrors(Map<String, List<String>> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
	public String getExceptionStackTrace() {
		return exceptionStackTrace;
	}
	public void setExceptionStackTrace(String exceptionStackTrace) {
		this.exceptionStackTrace = exceptionStackTrace;
	}
	public boolean hasErrors(){
		return hasFieldErrors() || hasGlobalErrors();
	}
	public boolean hasFieldErrors(){
		return !fieldErrors.isEmpty();
	}
	public boolean hasGlobalErrors(){
		return !globalErrors.isEmpty();
	}
}
