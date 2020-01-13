package com.strandls.user.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.pac4j.core.profile.CommonProfile;

import com.strandls.user.dto.UserDTO;
import com.strandls.user.pojo.User;

public interface AuthenticationService {
	
	public CommonProfile authenticateUser(String email, String password) throws Exception;
	public Map<String, Object> buildTokens(CommonProfile profile, User user, boolean getRefreshToken);
	public User addUser(HttpServletRequest request, UserDTO user);
	
}
