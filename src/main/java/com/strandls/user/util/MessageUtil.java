package com.strandls.user.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageUtil {

	private static final String FILENAME = "i18n/messages";

	private ResourceBundle messageBundle;

	public MessageUtil() {
		messageBundle = ResourceBundle.getBundle(FILENAME, Locale.getDefault());
	}

	public MessageUtil(Locale locale) {
		messageBundle = ResourceBundle.getBundle(FILENAME, locale);
	}

	public String getMessage(String code) {
		return messageBundle.getString(code);
	}
}