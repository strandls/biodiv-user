package com.strandls.user.service;

import javax.servlet.http.HttpServletRequest;

import com.strandls.user.pojo.User;

public interface MailService {
	
	void sendActivationMail(HttpServletRequest request, User user, String otp);
	void sendWelcomeMail(HttpServletRequest request, User user);
	void sendResetPasswordMail(User user);
	void sendForgotPasswordMail(HttpServletRequest request, User user, String otp);

}
