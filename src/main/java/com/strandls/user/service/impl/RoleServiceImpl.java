package com.strandls.user.service.impl;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;
import com.strandls.user.dao.RoleDao;
import com.strandls.user.pojo.Role;
import com.strandls.user.service.RoleService;

public class RoleServiceImpl implements RoleService {
	
	@Inject
	private RoleDao roleDao;
	
	@Override
	public Role getRoleByName(String roleName) {
		return roleDao.findByPropertyWithCondition("authority", roleName, "=");
	}
	
	@Override
	public Set<Role> setDefaultRoles(String[] defaultRoles) {
		Set<Role> roles = new HashSet<>();
		for (String roleName : defaultRoles) {
			roles.add(getRoleByName(roleName));
		}
		return roles;
	}

}
