package com.bridgestone.bsro.aws.mobile.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.bridgestone.bsro.aws.mobile.model.MWSUser;
import com.bridgestone.bsro.aws.mobile.model.StatusType;
import com.bridgestone.bsro.aws.mobile.service.mail.Mailer;
import com.bridgestone.bsro.aws.mobile.util.MailerUtility;
import com.bridgestone.bsro.aws.mobile.util.ValidationConstants;
import com.bridgestone.bsro.aws.mobile.util.ValidationUtility;
import com.bridgestone.bsro.aws.mobile.webservice.BSROWebServiceResponse;
import com.bridgestone.bsro.aws.mobile.webservice.BSROWebServiceResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;


import org.json.JSONObject;

public class MobileAppUserVehicleImageService {
	
	public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public Object getVehicleImageData(DynamoDBMapper dynamoDBMapper, String appName, String email, String vehicleId,String vehicleImageBucketName) 
	{
		BSROWebServiceResponse bsroWebserviceResponse = new BSROWebServiceResponse();
		String encodingPrefix = "data:image/jpg;base64,";
		String imageFileName = "";
		try {
			if(!ValidationUtility.isEmailValid(email)) 
			{
				bsroWebserviceResponse.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
				return setStatusAndMessage(bsroWebserviceResponse, ValidationConstants.USER_EMAIL_INVALID);
			}
			email = email.toLowerCase();
			MWSUser mwsUser = getMWSUser(dynamoDBMapper, email, appName);
			
			if(mwsUser == null) {
				bsroWebserviceResponse.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
				return setStatusAndMessage(bsroWebserviceResponse, ValidationConstants.USER_DOES_NOT_EXIST);
			} else {
				if ((mwsUser.getDescFlag() != null) && (mwsUser.getDescFlag().equals("B") || mwsUser.getDescFlag().equals("R")))
				{
					String md5UserID = ValidationUtility.convertToMD5(mwsUser.getUserId().toString());
 					imageFileName = md5UserID + "_" + vehicleId + ".jpg";
 					System.out.println("MobileAppUserService : getVehicleImageData() : v : " + imageFileName);
 					AmazonS3 s3Client = new AmazonS3Client();
					S3Object object = s3Client.getObject(new GetObjectRequest(vehicleImageBucketName, imageFileName));
					
					s3Client.getUrl(vehicleImageBucketName, imageFileName).toString();
					 
					InputStream objectData = object.getObjectContent();
 					System.out.println("MobileAppUserService : getVehicleImageData() : objectData : " + objectData);
					//Object jsonData = new ObjectMapper().readValue(convertStreamToString(objectData), Object.class);

 					BSROWebServiceResponse response = new BSROWebServiceResponse();
 					byte[] byteArray = IOUtils.toByteArray(objectData);
 					String imageDataURI = encodingPrefix + Base64.encodeBase64String(byteArray);
  					response.setPayload(imageDataURI);
					 response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
					return response;
					 
  				}
				else 
  				{
					bsroWebserviceResponse.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
					return setStatusAndMessage(bsroWebserviceResponse, ValidationConstants.USER_HAS_NO_BACKUP_DATA);
				}
			}
		} 
		catch(Exception e) 
		{	
			System.out.println("MobileAppUserService : getVehicleImageData() : Exception : " + e.getMessage());
			bsroWebserviceResponse.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
			bsroWebserviceResponse.setSTATUS(ValidationConstants.UNKNOWN_ERROR);
			return bsroWebserviceResponse;
		}
		 
 		
	}
	
	
	
	
	public Object saveVehicleImageData(DynamoDBMapper dynamoDBMapper, String appName, String email, String data, String vehicleId,String sourceBucketName,String destinationBucketName) {
		BSROWebServiceResponse bsroWebserviceResponse = new BSROWebServiceResponse();
		try {
			if(!ValidationUtility.isEmailValid(email)) {
				bsroWebserviceResponse.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
				return setStatusAndMessage(bsroWebserviceResponse, ValidationConstants.USER_EMAIL_INVALID);
			}
			if(ValidationUtility.isNullOrEmpty(data)) {
				bsroWebserviceResponse.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
				return setStatusAndMessage(bsroWebserviceResponse, ValidationConstants.INVALID_JSON_STRING);
			}
			
			MWSUser mwsUser = getMWSUser(dynamoDBMapper, email, appName);
			
			if(mwsUser == null) {
				bsroWebserviceResponse.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
				return setStatusAndMessage(bsroWebserviceResponse, ValidationConstants.USER_DOES_NOT_EXIST);
			} else {
				if (mwsUser.getUserId() != null) 
				{
					
					String md5HashedUserID = ValidationUtility.convertToMD5(mwsUser.getUserId().toString());
					String fileName =  md5HashedUserID + "_" + vehicleId + ".jpg";
					AmazonS3Client amazonS3Client = new AmazonS3Client();
					
					File file = Files.createTempFile(fileName, "jpg", new FileAttribute[0]).toFile();
					FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					if (!file.exists()) {
						file.createNewFile();
					}
 					
					
					String encodingPrefix = "data:image/jpg;base64,";
					System.out.println("MobileAppUserService : data : : " + data);

					//String dataUrl = "data:image/jpeg;base64,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";
					int contentStartIndex = data.indexOf(encodingPrefix) + encodingPrefix.length();
					System.out.println("MobileAppUserService : contentStartIndex : : " + contentStartIndex);

					byte[] imageData = Base64.decodeBase64(data.substring(contentStartIndex));
					
					OutputStream os = new FileOutputStream(file); 
					os.write(imageData);  
					 
 					
 					bufferedWriter.flush();
 					fileWriter.flush();
 					bufferedWriter.close();
					fileWriter.close();
					os.close();
					
					amazonS3Client.putObject(new PutObjectRequest(sourceBucketName, fileName, file));
					file.delete();
					
					mwsUser.setDescFlag("B");
					mwsUser.setLastModifiedDate(dateTimeFormat.format(new Date()).toString());
					
					dynamoDBMapper.save(mwsUser, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));
					
					String resizedImagePath = amazonS3Client.getUrl(destinationBucketName, fileName).toString();
					System.out.println("MobileAppUserService : resizedImagePath : : " + resizedImagePath);

					JSONObject successJson = new JSONObject();
					successJson.put("statusCode", BSROWebServiceResponseCode.SUCCESSFUL.toString());
					successJson.put("Message", "ImageUploadSuccess");
					successJson.put("PICTURE",resizedImagePath);
// 					successJson.put("lastModifiedDate", new SimpleDateFormat("MM-dd-yyyy h:mm a").format(dateTimeFormat.parse(mwsUser.getLastModifiedDate())));
					return new ObjectMapper().readValue(successJson.toString(), Object.class);
				}
			}
			bsroWebserviceResponse.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
			return setStatusAndMessage(bsroWebserviceResponse, ValidationConstants.USER_DOES_NOT_EXIST);
		} catch(Exception e) {
			System.out.println("MobileAppUserService : saveVehicleImageData() : Exception : " + e.getMessage());
			bsroWebserviceResponse.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
			bsroWebserviceResponse.setSTATUS(ValidationConstants.UNKNOWN_ERROR);
			return bsroWebserviceResponse;
		}
	}
	
	
	
	
	public Object deleteVehicleImageData(DynamoDBMapper dynamoDBMapper, String appName, String email, String vehicleId,String image_s3_source_bucket_name,String image_s3_destination_bucket_name) {
		BSROWebServiceResponse bsroWebserviceResponse = new BSROWebServiceResponse();
		try {
			if(!ValidationUtility.isEmailValid(email)) {
				bsroWebserviceResponse.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
				return setStatusAndMessage(bsroWebserviceResponse, ValidationConstants.USER_EMAIL_INVALID);
			}
			if(ValidationUtility.isNullOrEmpty(vehicleId)) {
				bsroWebserviceResponse.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
				return setStatusAndMessage(bsroWebserviceResponse, ValidationConstants.INVALID_JSON_STRING);
			}
			
			MWSUser mwsUser = getMWSUser(dynamoDBMapper, email, appName);
			
			if(mwsUser == null) {
				bsroWebserviceResponse.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
				return setStatusAndMessage(bsroWebserviceResponse, ValidationConstants.USER_DOES_NOT_EXIST);
			} else {
				if (mwsUser.getUserId() != null) 
				{
					String md5HashedUserID = ValidationUtility.convertToMD5(mwsUser.getUserId().toString());
					String fileName = md5HashedUserID + "_" + vehicleId + ".jpg";
					AmazonS3Client amazonS3Client = new AmazonS3Client();
 					amazonS3Client.deleteObject(new DeleteObjectRequest(image_s3_source_bucket_name, fileName));
 					amazonS3Client.deleteObject(new DeleteObjectRequest(image_s3_destination_bucket_name, fileName));
					JSONObject successJson = new JSONObject();
					successJson.put("statusCode", BSROWebServiceResponseCode.SUCCESSFUL.toString());
					successJson.put("Message", "ImageDeleteSuccess");
  					return new ObjectMapper().readValue(successJson.toString(), Object.class);
				}
			}
			bsroWebserviceResponse.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.toString());
			return setStatusAndMessage(bsroWebserviceResponse, ValidationConstants.USER_DOES_NOT_EXIST);
		} catch(Exception e) {
			System.out.println("MobileAppUserService : deleteVehicleImageData() : Exception : " + e.getMessage());
			bsroWebserviceResponse.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
			bsroWebserviceResponse.setSTATUS(ValidationConstants.UNKNOWN_ERROR);
			return bsroWebserviceResponse;
		}
	}
	
	
	
	
	private MWSUser getMWSUser(DynamoDBMapper dynamoDBMapper, String email, String appName) {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":email", new AttributeValue().withS(email));
	    eav.put(":siteName", new AttributeValue().withS(appName));

	    DynamoDBQueryExpression<MWSUser> queryExpression = new DynamoDBQueryExpression<MWSUser>()
           		.withIndexName("email-siteName-index")
    		        .withConsistentRead(false)
    		        .withKeyConditionExpression("email = :email AND siteName = :siteName")
    		        .withExpressionAttributeValues(eav);
	    
	    ArrayList<MWSUser> mwsUser = new ArrayList<MWSUser>(dynamoDBMapper.query(MWSUser.class, queryExpression));
	    
        if(mwsUser != null && !mwsUser.isEmpty() && mwsUser.size() > 0) {
        	return mwsUser.get(0);
        } else {
        	return null;
        }
	}
	
	public static String getMessage(String errorStatus){
		String msg = "";
		if(errorStatus!=null && !errorStatus.equals("")) {
			msg =  ValidationConstants.errorCollection.get(errorStatus);
		} else {
			msg = "";
		}
		return msg;
	}
	
	public static BSROWebServiceResponse setStatusAndMessage(BSROWebServiceResponse bsroWebserviceResponse, String status) {
		bsroWebserviceResponse.setSTATUS(status);
		bsroWebserviceResponse.setMessage(getMessage(status));
		return bsroWebserviceResponse;
	}
	
	static String convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
	
	 
 }