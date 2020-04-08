package com.strandls.user.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.rabbitmq.client.Channel;
import com.strandls.mail_utility.model.EnumModel.FIELDS;
import com.strandls.mail_utility.model.EnumModel.MAIL_TYPE;
import com.strandls.mail_utility.model.EnumModel.RESET_PASSWORD;
import com.strandls.mail_utility.model.EnumModel.USER_REGISTRATION;
import com.strandls.mail_utility.model.EnumModel.WELCOME_MAIL;
import com.strandls.mail_utility.producer.RabbitMQProducer;
import com.strandls.mail_utility.util.JsonUtil;
import com.strandls.user.RabbitMqConnection;
import com.strandls.user.pojo.User;
import com.strandls.user.service.MailService;
import com.strandls.user.util.AppUtil;
import com.strandls.user.util.MessageUtil;
import com.strandls.user.util.PropertyFileUtil;

public class MailServiceImpl implements MailService {

	private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	@Inject
	private Channel channel;

	@Override
	public void sendActivationMail(HttpServletRequest request, User user, String otp) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(FIELDS.TYPE.getAction(), MAIL_TYPE.USER_REGISTRATION.getAction());
		data.put(FIELDS.TO.getAction(), new String[] { user.getEmail() });
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(USER_REGISTRATION.OTP.getAction(), otp);
		model.put(USER_REGISTRATION.USERNAME.getAction(), user.getUserName());
		model.put(USER_REGISTRATION.TYPE.getAction(), MAIL_TYPE.USER_REGISTRATION.getAction());

		data.put(FIELDS.DATA.getAction(), JsonUtil.unflattenJSON(model));
		RabbitMQProducer producer = new RabbitMQProducer(channel);
		try {
			producer.produceMail(
					RabbitMqConnection.EXCHANGE,
					RabbitMqConnection.ROUTING_KEY,
					null, JsonUtil.mapToJSON(data));
			String admins = PropertyFileUtil.fetchProperty("config.properties", "mail_bcc");
			data.put(FIELDS.TO.getAction(), admins.split(","));
			producer.produceMail(RabbitMqConnection.EXCHANGE, RabbitMqConnection.ROUTING_KEY, null,
					JsonUtil.mapToJSON(data));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void sendWelcomeMail(HttpServletRequest request, User user) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(FIELDS.TYPE.getAction(), MAIL_TYPE.WELCOME_MAIL.getAction());
		data.put(FIELDS.TO.getAction(), new String[] { user.getEmail() });
		Map<String, Object> model = new HashMap<>();		
		MessageUtil messages = new MessageUtil();
		Properties config = PropertyFileUtil.fetchProperty("config.properties");
		model.put(WELCOME_MAIL.USERNAME.getAction(), AppUtil.capitalize(user.getUserName()));
		StringBuilder profileUrl = new StringBuilder();
		profileUrl.append(config.getProperty("serverUrl")).append("/user/show/").append(String.valueOf(user.getId()));
		model.put(WELCOME_MAIL.USER_PROFILE_URL.getAction(), profileUrl.toString());
		model.put(WELCOME_MAIL.SERVER_URL.getAction(), config.getProperty("serverUrl"));
		model.put(WELCOME_MAIL.SITENAME.getAction(), config.getProperty("siteName"));
		model.put(WELCOME_MAIL.FACEBOOK_URL.getAction(), config.getProperty("facebookUrl"));
		model.put(WELCOME_MAIL.TWITTER_URL.getAction(), config.getProperty("twitterUrl"));
		model.put(WELCOME_MAIL.FEEDBACKFORM_URL.getAction(), config.getProperty("feedbackFormUrl"));
		model.put(WELCOME_MAIL.MAIL_DEFAULT_FROM.getAction(), config.getProperty("mail_sender_email"));
		model.put(WELCOME_MAIL.WELCOME_EMAIL_INTRO.getAction(), messages.getMessage("activationEmail.intro"));
		model.put(WELCOME_MAIL.WELCOME_EMAIL_OBSERVATION.getAction(), messages.getMessage("activationEmail.observation"));
		model.put(WELCOME_MAIL.WELCOME_EMAIL_MAP.getAction(), messages.getMessage("activationEmail.map"));
		model.put(WELCOME_MAIL.WELCOME_EMAIL_CHECKLIST.getAction(), messages.getMessage("activationEmail.checklist"));
		model.put(WELCOME_MAIL.WELCOME_EMAIL_SPECIES.getAction(), messages.getMessage("activationEmail.species"));
		model.put(WELCOME_MAIL.WELCOME_EMAIL_GROUPS.getAction(), messages.getMessage("activationEmail.groups"));
		model.put(WELCOME_MAIL.WELCOME_EMAIL_DOCUMENTS.getAction(), messages.getMessage("activationEmail.documents"));

		data.put(FIELDS.DATA.getAction(), JsonUtil.unflattenJSON(model));
		RabbitMQProducer producer = new RabbitMQProducer(channel);
		try {
			producer.produceMail(
					RabbitMqConnection.EXCHANGE,
					RabbitMqConnection.ROUTING_KEY,
					null, JsonUtil.mapToJSON(data));

			String admins = PropertyFileUtil.fetchProperty("config.properties", "mail_bcc");
			data.put(FIELDS.TO.getAction(), admins.split(","));
			producer.produceMail(RabbitMqConnection.EXCHANGE, RabbitMqConnection.ROUTING_KEY, null,
					JsonUtil.mapToJSON(data));
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}		
	}

	@Override
	public void sendForgotPasswordMail(HttpServletRequest request, User user, String otp) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(FIELDS.TYPE.getAction(), MAIL_TYPE.RESET_PASSWORD.getAction());
		data.put(FIELDS.TO.getAction(), new String[] { user.getEmail() });
		Map<String, Object> model = new HashMap<>();
		model.put(RESET_PASSWORD.USERNAME.getAction(), user.getUserName());
		model.put(RESET_PASSWORD.OTP.getAction(), otp);
		model.put(RESET_PASSWORD.TYPE.getAction(), MAIL_TYPE.RESET_PASSWORD.getAction());

		data.put(FIELDS.DATA.getAction(), JsonUtil.unflattenJSON(model));
		RabbitMQProducer producer = new RabbitMQProducer(channel);
		try {
			producer.produceMail(
					RabbitMqConnection.EXCHANGE,
					RabbitMqConnection.ROUTING_KEY,
					null, JsonUtil.mapToJSON(data));

			String admins = PropertyFileUtil.fetchProperty("config.properties", "mail_bcc");
			data.put(FIELDS.TO.getAction(), admins.split(","));
			producer.produceMail(RabbitMqConnection.EXCHANGE, RabbitMqConnection.ROUTING_KEY, null,
					JsonUtil.mapToJSON(data));
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}

}
