package com.strandls.user.service.impl;

import com.strandls.user.service.SMSService;
import com.strandls.user.util.SMSThread;

public class SMSServiceImpl implements SMSService {
	
	@Override
	public void sendSMS(String phoneNumber, String otp) {
		
		// Read message from config and replace otp
		
		SMSThread sms = new SMSThread(phoneNumber, otp);
		Thread thread = new Thread(sms);
		thread.start();
	}

}
