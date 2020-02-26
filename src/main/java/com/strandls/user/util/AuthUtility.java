package com.strandls.user.util;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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
}
