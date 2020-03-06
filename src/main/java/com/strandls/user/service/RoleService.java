package com.strandls.user.service;

import java.util.Set;

import com.strandls.user.pojo.Role;

public interface RoleService {
	
	Role getRoleByName(String roleName);
	Set<Role> setDefaultRoles(String[] defaultRoles);

}
