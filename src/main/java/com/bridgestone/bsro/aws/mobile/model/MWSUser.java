package com.bridgestone.bsro.aws.mobile.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="MWSUser")
public class MWSUser {
	
	private Long userId;
	private String email;
	private String previousEmail;
	private String password;
	private String siteName;
	private String status;
	private int unsuccessfulAttempts;
	private String deleteFlag;
	private String descFlag;
	private String jsonData;
	private String registerDate;
	private String lastModifiedDate;
	
	public MWSUser() {
	}
	
	public MWSUser(Long userId, String email, String siteName, String password,
			String registerDate, String previousEmail, String status,
			String lastModifiedDate, int unsuccessfulAttempts) {
		super();
		this.userId = userId;
		this.email = email;
		this.siteName = siteName;
		this.password = password;
		this.registerDate = registerDate;
		this.previousEmail = previousEmail;
		this.status = status;
		this.lastModifiedDate = lastModifiedDate;
		this.unsuccessfulAttempts = unsuccessfulAttempts;
	}
	
	@DynamoDBHashKey(attributeName="userId")
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@DynamoDBAttribute(attributeName="email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@DynamoDBAttribute(attributeName="siteName")
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	@DynamoDBAttribute(attributeName="password")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@DynamoDBAttribute(attributeName="registerDate")
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	
	@DynamoDBAttribute(attributeName="previousEmail")
	public String getPreviousEmail() {
		return previousEmail;
	}
	public void setPreviousEmail(String previousEmail) {
		this.previousEmail = previousEmail;
	}
	
	@DynamoDBAttribute(attributeName="status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@DynamoDBAttribute(attributeName="lastModifiedDate")
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	@DynamoDBAttribute(attributeName="unsuccessfulAttempts")
	public int getUnsuccessfulAttempts() {
		return unsuccessfulAttempts;
	}
	public void setUnsuccessfulAttempts(int unsuccessfulAttempts) {
		this.unsuccessfulAttempts = unsuccessfulAttempts;
	}

	@DynamoDBAttribute(attributeName="jsonData")
	public String getJsonData() {
		return jsonData;
	}
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	@DynamoDBAttribute(attributeName="deleteFlag")
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	@DynamoDBAttribute(attributeName="descFlag")
	public String getDescFlag() {
		return descFlag;
	}
	public void setDescFlag(String descFlag) {
		this.descFlag = descFlag;
	}
	
}
