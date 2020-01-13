package com.strandls.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import com.strandls.user.dao.UserDao;
import com.strandls.user.dto.UserDTO;
import com.strandls.user.pojo.Language;
import com.strandls.user.pojo.Role;
import com.strandls.user.pojo.User;
import com.strandls.user.service.AuthenticationService;
import com.strandls.user.service.LanguageService;
import com.strandls.user.service.MailService;
import com.strandls.user.service.RoleService;
import com.strandls.user.service.UserService;
import com.strandls.user.util.JWTUtil;
import com.strandls.user.util.MessageDigestPasswordEncoder;
import com.strandls.user.util.PropertyFileUtil;
import com.strandls.user.util.SimpleUsernamePasswordAuthenticator;

import freemarker.template.Configuration;

public class AuthenticationServiceImpl implements AuthenticationService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	
	@Inject
	private UserService userService;
	
	@Inject
	private UserDao userDao;
	
	@Inject
	private MessageDigestPasswordEncoder passwordEncoder;

	@Inject
	private SimpleUsernamePasswordAuthenticator usernamePasswordAuthenticator;
	
	@Inject
	private LanguageService languageService;
	
	@Inject
	private RoleService roleService;
	
	@Inject
	private MailService mailService;
	
	@Inject
	private Configuration configuration;
	
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
	
	private String generateAccessToken(CommonProfile profile, User user) {
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
	
	private String generateRefreshToken(CommonProfile profile, User user) {
		JwtGenerator<CommonProfile> generator = new JwtGenerator<CommonProfile>(
				new SecretSignatureConfiguration(ApplicationConfig.JWT_SALT));
		
		Set<String> roles = new HashSet<String>();
		user.getRoles().forEach(role -> roles.add(role.getAuthority()));

		Map<String, Object> jwtClaims = new HashMap<String, Object>();
		jwtClaims.put("id", profile.getId());
		jwtClaims.put(JwtClaims.SUBJECT, profile.getId() + "");
		jwtClaims.put(Pac4jConstants.USERNAME, profile.getUsername());
		jwtClaims.put(CommonProfileDefinition.EMAIL, profile.getEmail());
		jwtClaims.put(JwtClaims.EXPIRATION_TIME, JWTUtil.getRefreshTokenExpiryDate());
		jwtClaims.put(JwtClaims.ISSUED_AT, new Date());
		jwtClaims.put("roles", roles);

		String jwtToken = generator.generate(jwtClaims);
		return jwtToken;
	}
	
	@Override
	public User addUser(HttpServletRequest request, UserDTO userDTO) {
		User user = new User();
		user.setName(userDTO.getUsername());
		user.setUserName(userDTO.getUsername());
		user.setEmail(userDTO.getEmail());
		MessageDigestPasswordEncoder passwordEncoder = new MessageDigestPasswordEncoder("MD5");
		user.setPassword(passwordEncoder.encodePassword(userDTO.getPassword(), null));
		user.setLocation(userDTO.getLocation());
		user.setLatitude(userDTO.getLatitude());
		user.setLongitude(userDTO.getLongitude());
		user.setOccupation(userDTO.getProfession());
		user.setInstitution(userDTO.getInstitution());
		user.setEnabled(false);
		user.setAccountExpired(false);
		user.setAccountLocked(true);
		user.setPasswordExpired(false);
		try {			
			Locale locale = request.getLocale();
			Language language = languageService.getLanguageByTwoLetterCode(locale.getLanguage());
			user.setLanguageId(language.getId());
			
			user.setVersion(0L);
			String[] roleNames = PropertyFileUtil.fetchProperty("biodiv-api.properties", "user.defaultRoleNames").split(",");
			Set<Role> roles = new HashSet<>();
			for (String roleName: roleNames) {
				roles.add(roleService.getRoleByName(roleName));
			}
			
			user.setRoles(roles);
			user = userDao.save(user);
			
			if (user.getEmail() != null && !user.getEmail().isEmpty()) {
				mailService.sendActivationMail(user);
			} else if (user.getMobileNumber() != null && !user.getMobileNumber().isEmpty()) {
				// Send OTP
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return user;
	}

}
