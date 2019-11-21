package com.strandls.user.service;

import java.util.Map;

import org.pac4j.core.profile.CommonProfile;

import com.strandls.user.pojo.User;

public interface AuthenticationService {
	
	public CommonProfile authenticateUser(String email, String password) throws Exception;
	public Map<String, Object> buildTokens(CommonProfile profile, User user, boolean getRefreshToken);
	
}
