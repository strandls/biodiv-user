package com.strandls.user.service;

import com.strandls.user.pojo.User;

public interface MailService {
	
	void sendActivationMail(User user);
	void sendWelcomeMail(User user);
	void sendResetPasswordMail(User user);

}
