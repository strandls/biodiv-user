package com.strandls.user.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {
	
	private String[] to;
	private String[] bcc;
	private String subject;
	private String text;
	private boolean isHtml;
	
	private final static String username;
	private final static String password;
	
	static {
		Properties prop = PropertyFileUtil.fetchProperty("config.properties");
		username = prop.getProperty("username");
		password = prop.getProperty("password");
	}
	
	public MailUtil() {}
	
	public MailUtil(String[] to, String[] bcc, String subject, String text, boolean isHtml) {
		this.to = to;
		this.bcc = bcc;
		this.subject = subject;
		this.text = text;
		this.isHtml = isHtml;
	}

	public void sendMail() throws MessagingException, AddressException {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		MimeMessage message = new MimeMessage(session);
		List<InternetAddress> address = new ArrayList<>();
		for (String recipient: to) {
			address.add(new InternetAddress(recipient));
		}
		List<InternetAddress> bccAddress = new ArrayList<>();
		for (String recipient: bcc) {
			bccAddress.add(new InternetAddress(recipient));
		}
		InternetAddress[] addresses = new InternetAddress[address.size()];
		InternetAddress[] bccAddresses = new InternetAddress[address.size()];
		message.addRecipients(Message.RecipientType.TO, address.toArray(addresses));
		message.addRecipients(Message.RecipientType.BCC, bccAddress.toArray(bccAddresses));
		message.setSubject(subject);
		message.setContent(text, isHtml ? "text/html" : "text/plain");
		
		Transport.send(message);		
	}

}
