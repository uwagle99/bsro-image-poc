package com.bridgestone.bsro.aws.mobile.handler;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

 import com.bridgestone.bsro.aws.mobile.service.MobileAppUserVehicleImageService;
import com.bridgestone.bsro.aws.mobile.util.AuthenticateService;
import com.bridgestone.bsro.aws.mobile.webservice.BSROWebServiceResponse;
import com.bridgestone.bsro.aws.mobile.webservice.BSROWebServiceResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;

 
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import org.apache.commons.io.IOUtils;

public class BSROMobileAppUserVehicleImageFunctionHandler implements RequestHandler<Object,Object>
{
	static final String GET_VEHICLE_IMAGE = "get-vehicle-image";
	static final String SAVE_VEHICLE_IMAGE = "save-vehicle-image";
	static final String DELETE_VEHICLE_IMAGE = "delete-vehicle-image";


	
 	
	public static Properties properties = null;
	public static DynamoDBMapper dynamoDBMapper = null;
	public static String environment;
    private static List<String> allowedEnv = Arrays.asList("dev", "local", "prod", "qa");

	static {
		InputStream inputStream = null;
		try {
			environment = System.getenv("environment").toString();
			System.out.println("static initializer > environment:"+environment);
			if (allowedEnv.contains(environment)) {
				inputStream = BSROMobileAppUserVehicleImageFunctionHandler.class.getClassLoader().getResourceAsStream(environment + "/application.properties");
				properties = new Properties();
				properties.load(inputStream);
				AmazonDynamoDBClient client = new AmazonDynamoDBClient();
				dynamoDBMapper = new DynamoDBMapper(client);
			} else {
			    System.err.println("In correct value: "+environment+" for 'environment' mentioned in lambda 'Environment variables'. Allowed values :"+allowedEnv);
            }
		} catch (IOException e) {
			System.err.println("IOException in BSROMobileAppUserVehicleImageFunctionHandler static initializer:"+e.getMessage());
		} catch (Exception e) {
			System.err.println("Exception in BSROMobileAppUserVehicleImageFunctionHandler static initializer:"+e.getMessage());
		} finally {
			try {
				if(!Objects.isNull(inputStream)) {
					inputStream.close();
				}
			} catch (Exception e) {
				System.err.println("Exception while InputStream Connection Close : " + e.getMessage());
			}
		}
	}

 
	public Object handleRequest(Object input, Context context) {
		System.out.println("FunctionName: " + context.getFunctionName());

		if (Objects.isNull(properties)) {
			System.out.print("Property file not loaded, Verify environment value passed..");
			return null;
		}

		BSROWebServiceResponse bsroWebServiceResponse = new BSROWebServiceResponse();
		HashMap<String, String> params = (HashMap<String, String>) input;
		if(params.get("resourcePath") == null)
		{
			return null;
		}
		String resourcePathParam = params.get("resourcePath").toString();
		String serviceFunction = resourcePathParam.substring(resourcePathParam.lastIndexOf("/") + 1);
		String tokenId = params.get("tokenId");
		

		try {
			
			AuthenticateService security = new AuthenticateService();
	    	if(!security.authenticateService(properties, tokenId)){
	        	bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.UNAUTHORIZED_ERROR.toString());
	        	return bsroWebServiceResponse;
	        }
	    	
			MobileAppUserVehicleImageService mobileAppUserService = new MobileAppUserVehicleImageService();
			
			String email = "";
			String appName = "";
			String password = "";
			String data = "";
			String vehicleId = "";
			String image_s3_source_bucket_name = properties.getProperty("image_s3_bucket_name") + "-" + environment;
			String image_s3_destination_bucket_name = properties.getProperty("image_s3_bucket_name") + "-" + environment + "-" + "resized";

			System.out.println("Source BucketName:"+ image_s3_source_bucket_name);
			System.out.println("Destination BucketName:"+ image_s3_destination_bucket_name);

			switch(serviceFunction) {
				case GET_VEHICLE_IMAGE:
					appName = params.get("appName");
					email = params.get("email");
					vehicleId =  params.get("vehicleId");
					return mobileAppUserService.getVehicleImageData(dynamoDBMapper, appName, email, vehicleId,image_s3_destination_bucket_name);
				case SAVE_VEHICLE_IMAGE:
					appName = params.get("appName");
					email = params.get("email");
					vehicleId =params.get("vehicleId");
					
					ObjectMapper mapper = new ObjectMapper();
					data = mapper.writeValueAsString(params.get("imageFile"));
					
					// Hence AWS API Gateway does not have compatible 'Mapping Template' for 'application/x-www-form-urlencoded', we extracted email and jsonData by string manipulation 
					String decodedData = java.net.URLDecoder.decode(data, "UTF-8");
					
					decodedData = decodedData.substring(1, decodedData.length()-1);
					data = decodedData.substring(10);
	
					return mobileAppUserService.saveVehicleImageData(dynamoDBMapper,appName,email,decodedData,vehicleId,image_s3_source_bucket_name,image_s3_destination_bucket_name);	
				case DELETE_VEHICLE_IMAGE:
					appName = params.get("appName");
					email = params.get("email");
					vehicleId =params.get("vehicleId");
 					return mobileAppUserService.deleteVehicleImageData(dynamoDBMapper,appName,email,vehicleId,image_s3_source_bucket_name,image_s3_destination_bucket_name);		
 				default:
					bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
					return bsroWebServiceResponse;
			}
		} catch(Exception e) {
			context.getLogger().log("BSROMobileAppUserFunctionHandler : handleRequest() : Exception : " + e.getMessage());
			bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.UNKNOWN_ERROR.toString());
			bsroWebServiceResponse.setMessage(e.getMessage());
			return bsroWebServiceResponse;
		}
	}
	
}