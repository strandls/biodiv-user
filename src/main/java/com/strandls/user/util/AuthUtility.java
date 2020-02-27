package com.strandls.user.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.definition.CommonProfileDefinition;
import org.pac4j.core.profile.jwt.JwtClaims;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.jwt.profile.JwtGenerator;

import com.strandls.authentication_utility.util.PropertyFileUtil;
import com.strandls.user.pojo.Role;
import com.strandls.user.pojo.User;

public class AuthUtility {

	public static CommonProfile createUserProfile(User user) {
		if (user == null)
			return null;
		try {
			Set<Role> roles = user.getRoles();
			Set<String> strRoles = new LinkedHashSet<>();

			for (Role r : roles) {
				strRoles.add(r.getAuthority());
			}

			return createUserProfile(user.getId(), user.getUserName(), user.getEmail(), strRoles);
		} catch (Exception e) {
			throw e;
		}
	}

	public static CommonProfile createUserProfile(Long userId, String username, String email, Set<String> authorities) {
		CommonProfile profile = new CommonProfile();
		updateUserProfile(profile, userId, username, email, authorities);
		return profile;
	}

	public static void updateUserProfile(CommonProfile profile, Long userId, String username, String email,
			Set<String> authorities) {
		if (profile == null)
			return;
		profile.setId(userId.toString());
		profile.addAttribute("id", userId);
		profile.addAttribute(Pac4jConstants.USERNAME, username);
		profile.addAttribute(CommonProfileDefinition.EMAIL, email);
		profile.addAttribute(JwtClaims.EXPIRATION_TIME, JWTUtil.getAccessTokenExpiryDate());
		profile.addAttribute(JwtClaims.ISSUED_AT, new Date());
		profile.setRoles(authorities);
		for (Object authority : authorities) {
			profile.addRole((String) authority);
		}
	}

	public static String buildTokenWithProp(String key, String value) {
		JwtGenerator<CommonProfile> generator = new JwtGenerator<CommonProfile>(
				new SecretSignatureConfiguration(PropertyFileUtil.fetchProperty("config.properties", "jwtSalt")));
		Map<String, Object> claims = new HashMap<>();
		claims.put(key, value);
		return generator.generate(claims);
	}

	public static String verifyTokenWithProp(String token) {
		JwtAuthenticator authenticator = new JwtAuthenticator();
		authenticator.addSignatureConfiguration(
				new SecretSignatureConfiguration(PropertyFileUtil.fetchProperty("config.properties", "jwtSalt")));
		CommonProfile profile = authenticator.validateToken(token);
		return profile.getAttribute(JwtClaims.SUBJECT).toString();
	}
	
	public static JSONObject verifyGoogleToken(String token) {
		JSONObject obj = null;
		try {
			StringBuilder response = new StringBuilder();
			URL oracle = new URL("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + token);
	        URLConnection yc = oracle.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	                                    yc.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	        	response.append(inputLine);
	        }
	        in.close();
	        obj = new JSONObject(response.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return obj;
	}
	
	public static void main(String[] args) {
		System.out.println(verifyGoogleToken("eyJhbGciOiJSUzI1NiIsImtpZCI6Ijc5YzgwOWRkMTE4NmNjMjI4YzRiYWY5MzU4NTk5NTMwY2U5MmI0YzgiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiYXpwIjoiODAwMTU3NTUxNTQxLTR0c25jcTNqc2w4cXVwbm9hZHRxMjZyaDd0YWduODN2LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiODAwMTU3NTUxNTQxLTR0c25jcTNqc2w4cXVwbm9hZHRxMjZyaDd0YWduODN2LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwic3ViIjoiMTExMjcxNjc1MTk0NzU4MTk4NDQwIiwiZW1haWwiOiJzZXRodTEwMTIxOTk0QGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoiOXhRc0s3WWZYSjM1RFJQY1ZNVF9QZyIsImlhdCI6MTU4Mjc5NjE1NCwiZXhwIjoxNTgyNzk5NzU0fQ.eY29kpbDpxgpqKQWx8QCehLipFdM-mJlIItxaaiB9GfVmKqBSddbCz2ZsXxgs6zDJoVkmkEaWF0foU5XBkTDb9k_NovSzwlgCEAvgkPpB-4t0PsmK8uzMchPPnemcPbmD4iGp_3gR0W4kzisI2SsEHgR43415bkwL6FB7uGNR7Qe4HIQdic8DiPxqIEoeMb-UJRKGPKMmez3pB0PQ8GejSWTXswZktCjni1zQ_KNNw4qkaJPkezyegVhoUIquVZuXn-wzpGXV8hXi_3EudupXwTr637zOfWHjQW8P_EKnszfuZmwzHQdgc2VIW-4uiGB6ukcmAh8WukOQtxcd0ebOg"));
	}
}
