package com.bridgestone.bsro.aws.mobile.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import sun.misc.BASE64Encoder;

import com.bridgestone.bsro.aws.mobile.webservice.BSROWebServiceResponse;
import com.bridgestone.bsro.aws.mobile.webservice.BSROWebServiceResponseCode;
import com.bridgestone.bsro.aws.mobile.webservice.error.Errors;

public class ValidationUtility {

	private static java.util.Random random = null;
	
	private static final byte LETTER_AND_NUMBERS[] = {
	      65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
	      75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
	      85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
	      101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
	      111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
	      121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
	      56, 57, 50, 49
	};
	
	public static String encryptPassword(String password){
		String hash = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(password.getBytes("UTF-8"));
			byte raw[] = md.digest(); 
			hash = (new BASE64Encoder()).encode(raw);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return hash;
	}
	
	public static String convertToMD5(String userID){
		 
		StringBuffer hash = new StringBuffer();
		try {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(userID.getBytes());
		byte[] digest = md.digest();
		for (byte b : digest) {
			hash.append(String.format("%02x", b & 0xff));
		}
		System.out.println("ValidationUtility : convertToMD5 hash : : " + hash.toString());

		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash.toString();
	}
 
	
    public BSROWebServiceResponse getValidationMessage(String message){
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		errors.getGlobalErrors().add(message);
		response.setErrors(errors);
		response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
		response.setPayload(null);
		return response;
	}
    
    public static boolean isEmailValid(String email) {
		//return (email!=null && EmailValidator.getInstance().isValid(email) && email.length()<=255);
    	return (email != null && email != "" && email.length() <= 255);
	}
    
    public static boolean isPasswordValid(String passwordOne) {
		// TODO Auto-generated method stub		
		if(passwordOne!=null&&passwordOne.length() >= 8 ){
			//System.out.println("is at least 8 char.");
			int x=0;
			//check for digit
			if(passwordOne.matches("((?=.*\\d).{1,})"))
				x++;
			//check for lowercase
			if(passwordOne.matches("((?=.*[a-z]).{1,})"))
				x++;
			//check for upper case
			if(passwordOne.matches("((?=.*[A-Z]).{1,})"))
				x++;
			//check for characters !@#$%^*()_+-={}[]|/
			if(passwordOne.matches("((?=.*[@#%/_=\\-\\(\\)\\!\\+\\^\\*\\[\\]\\{\\}\\|\\$]).{1,})"))
				x++;

			if(x<3){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}
    
    public static boolean lockUserAccount(int numAttempts){
		if(numAttempts < ValidationConstants.MAX_PASSWORD_FAILURE_ATTEMPTS){
			return false;
		}
		return true;
	}
    
	public static boolean validateUserEncryptedPassword(String currentPasswd, String userEnteredPasswd){
		String userEncryptedPasswd;
		try {
			userEncryptedPasswd = ValidationUtility.encryptPassword(userEnteredPasswd);
		} catch (Exception e) {
			System.err.println("Exception thrown while encrypting password");
			return false;
		}
		if(currentPasswd!=null && userEncryptedPasswd!=null && currentPasswd.equals(userEncryptedPasswd)){
			return true;
		}
		return false;
	}
	
	  public static String generatePassword(int charLen){
		  if(charLen <= 0)
			  charLen = 8; //default to 8
		  if(random==null){
				try{
					//Provided By sun, it will cause a lot garbage collection when startup
				    //random = java.security.SecureRandom.getInstance("SHA1PRNG");
					//random = new FastRandom();
					random = new java.util.Random();//use classic one which is faster
				}catch(Exception ex){
				    System.out.println("ValidationUtility : generatePassword() : Exception : " + ex.toString());
				}
		  }
		  byte charBytes[] = new byte[charLen];
	      
	      for(int i = 0; i < charLen; i++){
	          charBytes[i] = LETTER_AND_NUMBERS[(random.nextInt(128)) % 64];
	      }

	      return new String(charBytes);
	  }
	  
	  public static String padStoreNumber(String storeNumber){
		  StringBuffer paddedNumber = new StringBuffer();
		  for(int j=0; j<6-storeNumber.length(); j++) {
			  paddedNumber.append("0");
		  }
		  paddedNumber.append(storeNumber);
		  return paddedNumber.toString();
	  }

	  public static String responseToJson(String response){
		  JSONObject responseJson = new JSONObject();
		  try {
			  response = response.trim();
			  if(response != null && response != "") //if(org.apache.commons.lang.StringUtils.isNotBlank(response))
				  responseJson.put("STATUS", response);
			  else
				  responseJson.put("STATUS", "ResponseFormatError");
		  } catch (JSONException e) {
			  System.out.println("ValidationUtility : responseToJson() : JSONException : " + e.getMessage());
		  }
		  return responseJson.toString();
	  }
	  
	  public static boolean isNullOrEmpty(String str) {
		  if (str == null || str.trim().equals("") || str.trim().equals("null")) {
			  return true;
		  } 
		  return false;
	  }
}
