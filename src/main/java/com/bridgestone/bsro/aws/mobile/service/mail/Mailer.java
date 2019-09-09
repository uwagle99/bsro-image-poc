package com.bridgestone.bsro.aws.mobile.service.mail;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;


public class Mailer {
	
	public boolean sendEmail(String emailTo, String emailFrom, String emailSubject, String emailBody) throws Exception {
		
		
		try {
			AmazonSimpleEmailService amazonSimpleEmailService = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
			SendEmailRequest sendEmailRequest = new SendEmailRequest().withDestination(new Destination().withToAddresses(emailTo))
					.withMessage(new com.amazonaws.services.simpleemail.model.Message()
					.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(emailBody)))		                  
				              .withSubject(new Content().withCharset("UTF-8").withData(emailSubject)))
				          	  .withSource(emailFrom);				
			amazonSimpleEmailService.sendEmail(sendEmailRequest);		
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		

        
        return true;
	}
}
