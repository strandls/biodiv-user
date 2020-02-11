package com.strandls.user.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.strandls.user.controller.AuthenticationController;

public class SNSUtil {

	private static final Logger logger = LoggerFactory.getLogger(SNSUtil.class);
	
	private static final String ACCESS_ID;
	private static final String SECRET_ACCESS_KEY;
	
	private String message;
	private String phoneNumber;
	
	static {
		Properties prop = PropertyFileUtil.fetchProperty("config.properties");
		ACCESS_ID = prop.getProperty("sns.access_id");
		SECRET_ACCESS_KEY = prop.getProperty("sns.secret_access_key");
	}
	
	public SNSUtil() {}
	
	public SNSUtil(String phoneNumber, String message) {
		this.phoneNumber = phoneNumber;
		this.message = message;
	}

	@SuppressWarnings("deprecation")
	public void sendSMS() {
		AWSCredentials credentials = new BasicAWSCredentials(ACCESS_ID, SECRET_ACCESS_KEY);
        AmazonSNSClient snsClient = new AmazonSNSClient(credentials);
        
        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        attributes.put("AWS.SNS.SMS.MaxPrice", 
                new MessageAttributeValue()
                .withDataType("Number")
                .withStringValue("0.003"));
        attributes.put("AWS.SNS.SMS.SenderID", 
                new MessageAttributeValue()
                .withDataType("String")
                .withStringValue("IBPTEST"));
        attributes.put("AWS.SNS.SMS.SMSType", 
                new MessageAttributeValue()
                .withDataType("String")
                .withStringValue("Transactional"));
        
        PublishResult result = snsClient.publish(
                new PublishRequest()
                        .withMessage(message)
                        .withPhoneNumber(phoneNumber)
                        .withMessageAttributes(attributes)
        );
        
        logger.debug(result.getMessageId());
	}
	
}
