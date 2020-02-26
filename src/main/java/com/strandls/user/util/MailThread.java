package com.strandls.user.util;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class MailThread extends MailUtil implements Runnable {
	
	public MailThread(String[] to, String[] bcc, String subject, String text, boolean isHtml) {
		super(to, bcc, subject, text, isHtml);
	}

	@Override
	public void run() {
		try {
			sendMail();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
