package com.bridgestone.bsro.aws.mobile.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ValidationConstants {
	
	public static final Integer MAX_PASSWORD_FAILURE_ATTEMPTS = 5;
	
	public static final String USER_SUCCESSFULLY_REGISTERED = "UserRegistrationSuccess";
	public static final String USER_EMAIL_INVALID = "UserEmailInvalid";
	public static final String USER_PASSWORD_INVALID = "UserPasswordInvalid";
	public static final String USER_AUTHENTICATION_SUCCESSFUL = "UserAuthSuccess";
	public static final String USER_AUTHENTICATION_FAILED = "UserAuthFailed";
	public static final String USER_ALREADY_EXISTS = "UserExist";
	public static final String USER_DOES_NOT_EXIST = "UserNotExist";
	public static final String USER_UPDATE_SUCCESSFUL = "UserUpdateSuccess";
	public static final String USER_EMAIL_UPDATE_SUCCESSFUL = "UserEmailUpdateSuccess";
	public static final String USER_PASSWORD_UPDATE_SUCCESSFUL = "UserPasswordUpdateSuccess";
	public static final String USER_UPDATE_FAILED = "UserNotUpdate";
	public static final String USER_HAS_NOT_REGISTERED = "UserHasNotRegistered";
	public static final String USER_PASSWORD_RESET_FAILED = "ResetPwdServerError";
	public static final String USER_PASSWORD_RESET_SUCCESSFUL = "ResetPwdSuccess";
	public static final String USER_DATA_BACKUP_SUCCESSFUL = "BackupSuccess";
	public static final String USER_ALREADY_HAS_BACKUP_DATA = "YesBackupFound";
	public static final String USER_HAS_NO_BACKUP_DATA = "NoBackupFound";
	
	public static final String USER_ACCOUNT_LOCKED = "AccountLocked";
	
	public static final String INVALID_JSON_STRING = "InvalidJsonString";
	
	public static final String USER_DATA_BACKUP_FAILED = "BackupFailed";
	public static final String USER_DATA_RESTORE_ERROR = "RestoreError";
	
	public static final String UNKNOWN_ERROR = "UnknownError";
	
	private static final Map<String, String> errorMap;
	
	public static Map<String, String> errorCollection = new HashMap<String, String>();
	
    static {
          errorCollection.put("UserEmailInvalid", "Invalid User Email ID");
		  errorCollection.put("UserPasswordInvalid", "Invalid Password");
		  errorCollection.put("noAppName", "Missing App Name");
		  errorCollection.put("InvalidJsonString", "Invalid Json String");
		  errorCollection.put("UserExist", "User Exist");
		  errorCollection.put("UserRegistrationSuccess", "User Registration Success");
		  errorCollection.put("UserHasNotRegistered", "Unable to register user");	
		  errorCollection.put("UserAuthSuccess", "User Authentication is Successful");
		  errorCollection.put("UserAuthFailed", "User Authentication Failed");
		  errorCollection.put("UserEmailUpdateSuccess", "User Email Update Success");
		  errorCollection.put("UserPasswordUpdateSuccess", "User Password Update Success");
		  errorCollection.put("UserUpdateSuccess", "User Update Success");
		  errorCollection.put("UserNotUpdate", "User Detail is not updated");
		  errorCollection.put("UserNotExist", "User is not exist");
		  //errorCollection.put("ResetPwdSuccess", "Reset Pwd Success");
		  errorCollection.put("ResetPwdSuccess", "Reset Password Success");
		  //errorCollection.put("ResetPwdServerError", "ResetPwdServerError");
		  errorCollection.put("ResetPwdServerError", "Reset Password Server Error");
		  errorCollection.put("BackupFailed", "Backup Failed");
		  errorCollection.put("BackupSuccess", "Backup Success");
		  errorCollection.put("NoBackupFound", "No Backup Found");
		  errorCollection.put("YesBackupFound", "Backup Found");
		  errorCollection.put("RestoreError", "Restore Error");
		  errorCollection.put("RestoreSuccess", "Restore Success");
		  errorCollection.put("UserAddressInvalid", "Invalid User Address");
		  errorCollection.put("UserRemoveSuccess", "User Remove Success");
		  errorCollection.put("UnsecureRequest", "Unsecure Request");
		  errorCollection.put("AccountLocked", "Account Locked");
		  errorCollection.put("NoDeviceIsRegisteredToUser", "No Device Is Registered To User");
		  errorCollection.put("NoDriverFoundForUser", "No Driver Found For User");
		  errorMap = Collections.unmodifiableMap(errorCollection);
    }
}
