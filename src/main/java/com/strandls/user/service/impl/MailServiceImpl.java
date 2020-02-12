package com.strandls.user.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.strandls.user.pojo.User;
import com.strandls.user.service.MailService;
import com.strandls.user.util.AppUtil;
import com.strandls.user.util.MailThread;
import com.strandls.user.util.MessageUtil;
import com.strandls.user.util.PropertyFileUtil;
import com.strandls.user.util.TemplateUtil;

import freemarker.template.Configuration;

public class MailServiceImpl implements MailService {
	
	@Inject
	private Configuration configuration;

	@Override
	public void sendActivationMail(HttpServletRequest request, User user, String otp) {
		TemplateUtil templates = new TemplateUtil(configuration);
		String verificationUrl = PropertyFileUtil.fetchProperty("config.properties", "verficationUrl");
		
		Map<String, String> model = new HashMap<>();
		model.put("username", user.getUserName());
		model.put("url", verificationUrl);
		model.put("otp", otp);
		String content = templates.getTemplateAsString("activation.ftl", model);
		
		String[] bccUsers = PropertyFileUtil.fetchProperty("config.properties", "mail_bcc").split(",");
		MailThread mail = new MailThread(new String[] {user.getEmail()}, bccUsers, "Activate your account with India Biodiversity Portal", content, true);
		Thread thread = new Thread(mail);
		thread.start();
	}

	@Override
	public void sendWelcomeMail(HttpServletRequest request, User user) {
		MessageUtil messages = new MessageUtil();
		Properties config = PropertyFileUtil.fetchProperty("config.properties");
		TemplateUtil templates = new TemplateUtil(configuration);
		Map<String, String> model = new HashMap<>();
		model.put("username", AppUtil.capitalize(user.getUserName()));
		String profileUrl = null;
		try {
			Map<String, String> linkParams = new HashMap<String, String>();
			linkParams.put("id", String.valueOf(user.getId()));
			profileUrl = AppUtil.buildURI(request, "/user/show", linkParams, false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		model.put("userProfileUrl", profileUrl);
		model.put("serverUrl", config.getProperty("serverUrl"));
		model.put("siteName", config.getProperty("siteName"));
		model.put("facebookUrl", config.getProperty("facebookUrl"));
		model.put("twitterUrl", config.getProperty("twitterUrl"));
		model.put("feedbackFormUrl", config.getProperty("feedbackFormUrl"));
		model.put("mailDefaultFrom", config.getProperty("mail_sender_email"));
		model.put("welcomeEmailIntro", messages.getMessage("activationEmail.intro"));
		model.put("welcomeEmailObservation", messages.getMessage("activationEmail.observation"));
		model.put("welcomeEmailMap", messages.getMessage("activationEmail.map"));
		model.put("welcomeEmailChecklist", messages.getMessage("activationEmail.checklist"));
		model.put("welcomeEmailSpecies", messages.getMessage("activationEmail.species"));
		model.put("welcomeEmailGroups", messages.getMessage("activationEmail.groups"));
		model.put("welcomeEmailDocuments", messages.getMessage("activationEmail.documents"));
		String content = templates.getTemplateAsString("welcome.ftl", model);
		
		String[] bccUsers = config.getProperty("mail_bcc").split(",");		
		MailThread mail = new MailThread(new String[] {user.getEmail()}, bccUsers, "Welcome to India Biodiversity Portal", content, true);
		Thread thread = new Thread(mail);
		thread.start();
	}

	@Override
	public void sendResetPasswordMail(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendForgotPasswordMail(HttpServletRequest request, User user, String otp) {
		TemplateUtil templates = new TemplateUtil(configuration);
		
		Map<String, String> model = new HashMap<>();
		model.put("username", user.getUserName());
		model.put("otp", otp);
		String content = templates.getTemplateAsString("reset-password.ftl", model);
		
		String[] bccUsers = PropertyFileUtil.fetchProperty("config.properties", "mail_bcc").split(",");
		MailThread mail = new MailThread(new String[] {user.getEmail()}, bccUsers, "Reset password with India Biodiversity Portal", content, true);
		Thread thread = new Thread(mail);
		thread.start();
	}

}
