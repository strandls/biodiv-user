package com.strandls.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.definition.CommonProfileDefinition;
import org.pac4j.core.profile.jwt.JwtClaims;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.strandls.user.ApplicationConfig;
import com.strandls.user.pojo.User;
import com.strandls.user.service.AuthenticationService;
import com.strandls.user.service.UserService;
import com.strandls.user.util.JWTUtil;
import com.strandls.user.util.MessageDigestPasswordEncoder;
import com.strandls.user.util.SimpleUsernamePasswordAuthenticator;

public class AuthenticationServiceImpl implements AuthenticationService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	
	@Inject
	private UserService userService;
	
	@Inject
	private MessageDigestPasswordEncoder passwordEncoder;
	
	@Inject
	private SimpleUsernamePasswordAuthenticator usernamePasswordAuthenticator;
	
	@Override
	public CommonProfile authenticateUser(String userEmail, String password) throws Exception {
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userEmail, password);
		usernamePasswordAuthenticator.validate(credentials, null);		
		return credentials.getUserProfile();
	}

	@Override
	public Map<String, Object> buildTokens(CommonProfile profile, User user, boolean getRefreshToken) {
		Map<String, Object> response = new HashMap<>();
		try {
			String accessToken = generateAccessToken(profile, user);
			response.put("access_token", accessToken);
			response.put("token_type", "bearer");
			response.put("timeout", JWTUtil.getAccessTokenExpiryDate());
			
			if (getRefreshToken) {
				String refreshToken = generateRefreshToken(profile, user);
				response.put("refresh_token", refreshToken);
			}
		} catch (Exception ex) {
			throw ex;
		}
		return response;
	}
	
	public static String generateAccessToken(CommonProfile profile, User user) {
		JwtGenerator<CommonProfile> generator = new JwtGenerator<CommonProfile>(
				new SecretSignatureConfiguration(ApplicationConfig.JWT_SALT));
		
		Set<String> roles = new HashSet<String>();
		user.getRoles().forEach(role -> roles.add(role.getAuthority()));

		Map<String, Object> jwtClaims = new HashMap<String, Object>();
		jwtClaims.put("id", profile.getId());
		jwtClaims.put(JwtClaims.SUBJECT, profile.getId() + "");
		jwtClaims.put(Pac4jConstants.USERNAME, profile.getUsername());
		jwtClaims.put(CommonProfileDefinition.EMAIL, profile.getEmail());
		jwtClaims.put(JwtClaims.EXPIRATION_TIME, JWTUtil.getAccessTokenExpiryDate());
		jwtClaims.put(JwtClaims.ISSUED_AT, new Date());
		jwtClaims.put("roles", roles);

		String jwtToken = generator.generate(jwtClaims);
		return jwtToken;
	}
	
	public static String generateRefreshToken(CommonProfile profile, User user) {
		JwtGenerator<CommonProfile> generator = new JwtGenerator<CommonProfile>(
				new SecretSignatureConfiguration(ApplicationConfig.JWT_SALT));
		
		Set<String> roles = new HashSet<String>();
		user.getRoles().forEach(role -> roles.add(role.getAuthority()));

		Map<String, Object> jwtClaims = new HashMap<String, Object>();
		jwtClaims.put("id", profile.getId());
		jwtClaims.put(JwtClaims.SUBJECT, profile.getId() + "");
		jwtClaims.put(Pac4jConstants.USERNAME, profile.getUsername());
		jwtClaims.put(CommonProfileDefinition.EMAIL, profile.getEmail());
		jwtClaims.put(JwtClaims.EXPIRATION_TIME, JWTUtil.getAccessTokenExpiryDate());
		jwtClaims.put(JwtClaims.ISSUED_AT, new Date());
		jwtClaims.put("roles", roles);

		String jwtToken = generator.generate(jwtClaims);
		return jwtToken;
	}

}
