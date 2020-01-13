package com.strandls.user.util;

import java.util.Base64;
import java.util.Random;

public class AppUtil {
	
	public static String encodeString(String data) {
		return new String(Base64.getEncoder().encode(data.getBytes()));
	}
	
	public static String decodeString(String data) {
		return new String(Base64.getDecoder().decode(data.getBytes()));
	}
	
	public static String generateOTP() {
		Random random = new Random();
		return String.format("%06d", random.nextInt(1000000));
	}
	
	public static void main(String[] args) {
		System.out.println(generateOTP());
	}

}
