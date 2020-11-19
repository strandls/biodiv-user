package com.strandls.user.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.pac4j.core.profile.CommonProfile;

import com.strandls.user.dto.UserDTO;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.requests.UserPasswordChange;

public interface AuthenticationService {
	
	public Map<String, Object> authenticateUser(String email, String password) throws Exception;
	public Map<String, Object> buildTokens(CommonProfile profile, User user, boolean getRefreshToken);
	public Map<String, Object> addUser(HttpServletRequest request, UserDTO user, String type) throws Exception;
	public Map<String, Object> validateUser(HttpServletRequest request, Long id, String otp);
	public Map<String, Object> regenerateOTP(HttpServletRequest request, Long id, int action);
	public Map<String, Object> forgotPassword(HttpServletRequest request, String verificationId);
	public Map<String, Object> resetPassword(HttpServletRequest request, Long id, String otp, String password);
	public Map<String, Object> changePassword(HttpServletRequest request, UserPasswordChange inputUser);
	public String deleteUser(HttpServletRequest request, Long userId);
	
}
