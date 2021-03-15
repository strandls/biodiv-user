package com.strandls.user.util;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

public class SNSUtil {

	private static final Logger logger = LoggerFactory.getLogger(SNSUtil.class);

	@Inject
	private AmazonSNSClient snsClient;

	public void sendSMS(String message, String phoneNumber) {

		Map<String, MessageAttributeValue> attributes = new HashMap<>();
		attributes.put("AWS.SNS.SMS.MaxPrice",
				new MessageAttributeValue().withDataType("Number").withStringValue("0.003"));
		attributes.put("AWS.SNS.SMS.SenderID",
				new MessageAttributeValue().withDataType("String").withStringValue("IBPTEST"));
		attributes.put("AWS.SNS.SMS.SMSType",
				new MessageAttributeValue().withDataType("String").withStringValue("Transactional"));

		PublishResult result = snsClient.publish(new PublishRequest().withMessage(message).withPhoneNumber(phoneNumber)
				.withMessageAttributes(attributes));

		logger.debug(result.getMessageId());
	}

}
