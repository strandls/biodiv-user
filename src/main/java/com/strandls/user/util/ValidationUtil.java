package com.strandls.user.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

	private static final String EMAIL_PATTERN = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
	private static final String PHONE_PATTERN = "((\\+*)((0[ -]+)*|(91 )*)(\\d{12}+|\\d{10}+))|\\d{5}([- ]*)\\d{6}";
	
	public static boolean validateEmail(String email) {
		if (email == null) {
			return false;
		}
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.find();
	}
	
	public static boolean validatePhone(String phone) {
		if (phone == null) {
			return false;
		}
		Pattern pattern = Pattern.compile(PHONE_PATTERN);
		Matcher matcher = pattern.matcher(phone);
		return matcher.find();
	}
	
}
