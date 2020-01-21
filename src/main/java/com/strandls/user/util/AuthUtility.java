package com.strandls.user.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.definition.CommonProfileDefinition;
import org.pac4j.core.profile.jwt.JwtClaims;

import com.strandls.user.pojo.Role;
import com.strandls.user.pojo.User;

public class AuthUtility {

	public static CommonProfile createUserProfile(User user) {
		if (user == null)
			return null;
		try {
			Set<Role> roles = user.getRoles();
			Set<String> strRoles = new LinkedHashSet<>();
			List<String> authorities = new ArrayList<>();
			
			for (Role r: roles) {
				strRoles.add(r.getAuthority());
			}

			return createUserProfile(user.getId(), user.getUserName(), user.getEmail(), strRoles);
		} catch (Exception e) {
			throw e;
		}
	}

	public static CommonProfile createUserProfile(Long userId, String username, String email,
			Set<String> authorities) {
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

}
