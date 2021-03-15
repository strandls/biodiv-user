package com.strandls.user.util;

public class SMSThread implements Runnable {

	private SNSUtil snsUtil;
	private String phoneNumber;
	private String message;

	public SMSThread(SNSUtil snsUtil, String phoneNumber, String message) {
		this.snsUtil = snsUtil;
		this.phoneNumber = phoneNumber;
		this.message = message;
	}

	@Override
	public void run() {
		snsUtil.sendSMS(message, phoneNumber);
	}

}
