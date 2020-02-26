package com.strandls.user.util;

public class SMSThread extends SNSUtil implements Runnable {
	
	public SMSThread(String phoneNumber, String message) {
		super(phoneNumber, message);
	}
	
	@Override
	public void run() {
		sendSMS();
	}

}
