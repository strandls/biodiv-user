/**
 * 
 */
package com.strandls.user.service.impl;

import java.util.List;

import com.google.inject.Inject;
import com.strandls.user.dao.SpeciesPermissionDao;
import com.strandls.user.dao.UserDao;
import com.strandls.user.dao.UserGroupMemberRoleDao;
import com.strandls.user.pojo.SpeciesPermission;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserGroupMemberRole;
import com.strandls.user.pojo.UserIbp;
import com.strandls.user.pojo.UserPermissions;
import com.strandls.user.service.UserService;

/**
 * @author Abhishek Rudra
 *
 */
public class UserServiceImpl implements UserService {

	@Inject
	private UserDao userDao;

	@Inject
	private SpeciesPermissionDao speciesPermissionDao;

	@Inject
	private UserGroupMemberRoleDao userGroupMemberDao;

	@Override
	public User fetchUser(Long userId) {
		User user = userDao.findById(userId);
		return user;
	}

	@Override
	public UserIbp fetchUserIbp(Long userId) {
		User user = userDao.findById(userId);
		UserIbp ibp = new UserIbp(user.getId(), user.getName(), user.getProfilePic());
		return ibp;
	}

	@Override
	public User getUserByEmail(String userEmail) {
		return userDao.findByUserEmail(userEmail);
	}

	@Override
	public UserPermissions getUserPermissions(Long userId) {
		List<SpeciesPermission> allowedTaxonList = speciesPermissionDao.findByUserId(userId);
		List<UserGroupMemberRole> userMemberRole = userGroupMemberDao.getUserGroup(userId);
		List<UserGroupMemberRole> userFeatureRole = userGroupMemberDao.findUserGroupbyUserIdRole(userId);
		UserPermissions permissions = new UserPermissions(allowedTaxonList, userMemberRole, userFeatureRole);
		return permissions;
	}

}
