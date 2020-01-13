package com.strandls.user.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.strandls.user.pojo.User;
import com.strandls.user.service.MailService;
import com.strandls.user.util.MailThread;
import com.strandls.user.util.MailUtil;
import com.strandls.user.util.PropertyFileUtil;
import com.strandls.user.util.TemplateUtil;

import freemarker.template.Configuration;

public class MailServiceImpl implements MailService {
	
	@Inject
	private Configuration configuration;

	@Override
	public void sendActivationMail(User user) {
		TemplateUtil templates = new TemplateUtil(configuration);		
		String verificationUrl = PropertyFileUtil.fetchProperty("biodiv-api.properties", "verficationUrl");		
		Map<String, String> model = new HashMap<>();
		model.put("username", user.getUserName());
		model.put("url", verificationUrl);
		String content = templates.getTemplateAsString("activation.ftl", model);
		
		String[] bccUsers = PropertyFileUtil.fetchProperty("biodiv-api.properties", "mail.bcc").split(",");
		MailThread mail = new MailThread(new String[] {user.getEmail()}, bccUsers, "", content, true);
		Thread thread = new Thread(mail);
		thread.start();
	}

	@Override
	public void sendWelcomeMail(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendResetPasswordMail(User user) {
		// TODO Auto-generated method stub
		
	}

}
