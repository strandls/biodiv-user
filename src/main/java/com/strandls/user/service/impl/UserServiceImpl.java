/**
 * 
 */
package com.strandls.user.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.user.dao.FirebaseDao;
import com.strandls.user.dao.FollowDao;
import com.strandls.user.dao.SpeciesPermissionDao;
import com.strandls.user.dao.UserDao;
import com.strandls.user.dao.UserGroupMemberRoleDao;
import com.strandls.user.pojo.FirebaseTokens;
import com.strandls.user.pojo.Follow;
import com.strandls.user.pojo.GroupAddMember;
import com.strandls.user.pojo.Role;
import com.strandls.user.pojo.SpeciesPermission;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserGroupMemberRole;
import com.strandls.user.pojo.UserGroupMembersCount;
import com.strandls.user.pojo.UserIbp;
import com.strandls.user.pojo.UserPermissions;
import com.strandls.user.service.UserService;

/**
 * @author Abhishek Rudra
 *
 */
public class UserServiceImpl implements UserService {

	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Inject
	private UserDao userDao;

	@Inject
	private SpeciesPermissionDao speciesPermissionDao;

	@Inject
	private UserGroupMemberRoleDao userGroupMemberDao;

	@Inject
	private FirebaseDao firebaseDao;

	@Inject
	private FollowDao followDao;

	@Override
	public User fetchUser(Long userId) {
		User user = userDao.findById(userId);
		return user;
	}

	@Override
	public UserIbp fetchUserIbp(Long userId) {
		User user = userDao.findById(userId);
		Set<Role> roles = user.getRoles();
		Boolean isAdmin = false;
		for (Role role : roles) {
			if (role.getAuthority().equalsIgnoreCase("ROLE_ADMIN")) {
				isAdmin = true;
				break;
			}
		}
		UserIbp ibp = new UserIbp(user.getId(), user.getName(), user.getProfilePic(), isAdmin);
		return ibp;
	}

	@Override
	public User getUserByEmail(String userEmail) {
		return userDao.findByUserEmail(userEmail);
	}

	@Override
	public User getUserByMobile(String mobileNumber) {
		return userDao.findByUserMobile(mobileNumber);
	}

	@Override
	public UserPermissions getUserPermissions(Long userId, String type, Long objectId) {
		List<SpeciesPermission> allowedTaxonList = speciesPermissionDao.findByUserId(userId);
		List<UserGroupMemberRole> userMemberRole = userGroupMemberDao.getUserGroup(userId);
		List<UserGroupMemberRole> userFeatureRole = userGroupMemberDao.findUserGroupbyUserIdRole(userId);
		Boolean following = null;
		if (type != null || objectId != null) {
			Follow follow = fetchByFollowObject(type, objectId, userId);
			following = false;
			if (follow != null)
				following = true;
		}
		UserPermissions permissions = new UserPermissions(allowedTaxonList, userMemberRole, userFeatureRole, following);
		return permissions;
	}

	@Override
	public User updateUser(User user) {
		return userDao.update(user);
	}

	@Override
	public User getUserByEmailOrMobile(String data) {
		return userDao.findByUserEmailOrMobile(data);
	}

	@Override
	public Follow fetchByFollowId(Long id) {
		Follow follow = followDao.findById(id);
		return follow;
	}

	@Override
	public Follow fetchByFollowObject(String objectType, Long objectId, Long authorId) {
		Follow follow = followDao.findByObject(objectType, objectId, authorId);
		return follow;
	}

	@Override
	public List<Follow> fetchFollowByUser(Long authorId) {
		List<Follow> follows = followDao.findByUser(authorId);
		return follows;
	}

	@Override
	public Follow updateFollow(String objectType, Long objectId, Long userId) {
		Follow follow = followDao.findByObject(objectType, objectId, userId);
		if (follow == null) {
			follow = new Follow(null, 0L, objectId, objectType, userId, new Date());
			follow = followDao.save(follow);

		}
		return follow;
	}

	@Override
	public Follow unFollow(String type, Long objectId, Long userId) {
		Follow follow = followDao.findByObject(type, objectId, userId);
		if (follow != null) {
			follow = followDao.delete(follow);
		}
		return follow;
	}

	@Override
	public Boolean checkUserGroupMember(Long userId, Long userGroupId) {
		UserGroupMemberRole result = userGroupMemberDao.findByUserGroupIdUserId(userGroupId, userId);
		if (result != null)
			return true;
		return false;
	}

	@Override
	public List<User> getNames(String name) {
		return userDao.findNames(name);
	}

	@Override
	public List<User> fetchRecipients(String objectType, Long objectId) {
		List<Follow> followers = followDao.findByObject(objectType, objectId);
		List<User> recipients = new ArrayList<>();
		if (followers != null) {
			for (Follow follower : followers) {
				User user = userDao.findById(follower.getAuthorId());
				if (user != null) {
					recipients.add(user);
				}
			}
		}
		return recipients;
	}

	@Override
	public FirebaseTokens saveToken(Long userId, String fcmToken) {
		User user = fetchUser(userId);
		user.setSendPushNotification(true);
		updateUser(user);
		FirebaseTokens token = new FirebaseTokens(user, fcmToken);
		return firebaseDao.save(token);
	}

	@Override
	public List<UserGroupMembersCount> getUserGroupMemberCount() {
		List<UserGroupMembersCount> result = userGroupMemberDao.fetchMemberCountUserGroup();
		return result;
	}

	@Override
	public Boolean checkFounderRole(Long userId, Long userGroupId) {
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			String founder = properties.getProperty("userGroupFounder");
			in.close();
			UserGroupMemberRole result = userGroupMemberDao.findByUserGroupIdUserId(userGroupId, userId);
			if (result.getRoleId().equals(Long.parseLong(founder)))
				return true;
			return false;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean checkModeratorRole(Long userId, Long userGroupId) {
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			String founder = properties.getProperty("userGroupExpert");
			in.close();
			UserGroupMemberRole result = userGroupMemberDao.findByUserGroupIdUserId(userGroupId, userId);
			if (result.getRoleId().equals(Long.parseLong(founder)))
				return true;
			return false;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;

	}

	@Override
	public UserGroupMemberRole addMemberUG(Long userId, Long roleId, Long userGroupId) {
		UserGroupMemberRole ugMemberRole = new UserGroupMemberRole(userGroupId, roleId, userId);
		ugMemberRole = userGroupMemberDao.save(ugMemberRole);
		return ugMemberRole;
	}

	@Override
	public Boolean removeGroupMember(Long userId, Long userGroupId) {
		try {
			UserGroupMemberRole ugMember = userGroupMemberDao.findByUserGroupIdUserId(userGroupId, userId);
			if (ugMember != null) {
				userGroupMemberDao.delete(ugMember);
				List<UserGroupMemberRole> members = userGroupMemberDao.fetchByUserGroupIdRole(userGroupId);
				if (members == null || members.isEmpty()) {
					InputStream in = Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("config.properties");
					Properties properties = new Properties();
					try {
						properties.load(in);
					} catch (IOException e) {
						logger.error(e.getMessage());
					}
					Long founderId = Long.parseLong(properties.getProperty("userGroupFounder"));
					Long portalAmdinId = Long.parseLong(properties.getProperty("portalAdminId"));
					in.close();
					ugMember = new UserGroupMemberRole(userGroupId, founderId, portalAmdinId);
					userGroupMemberDao.save(ugMember);
				}
				return true;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean joinGroup(Long userId, Long userGroupId) {
		try {
			Boolean isOpenGroup = userGroupMemberDao.checksGroupType(userGroupId.toString());
			if (isOpenGroup) {
				InputStream in = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("config.properties");
				Properties properties = new Properties();
				try {
					properties.load(in);
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
				Long memberId = Long.parseLong(properties.getProperty("userGroupMember"));
				in.close();
				Boolean alreadyMember = userGroupMemberDao.checkUserAlreadyMapped(userId, userGroupId, memberId);
				if (!alreadyMember) {
					UserGroupMemberRole ugMember = new UserGroupMemberRole(userGroupId, memberId, userId);
					userGroupMemberDao.save(ugMember);
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public List<Long> addMemberDirectly(GroupAddMember addMember) {
		try {
			Long roleId = addMember.getRoleId();
			Long userGroupId = addMember.getRoleId();
			List<Long> mappedUser = new ArrayList<Long>();
			for (Long userId : addMember.getMemberList()) {
				Boolean alreadyMember = userGroupMemberDao.checkUserAlreadyMapped(userId, userGroupId, roleId);
				if (!alreadyMember) {
					UserGroupMemberRole ugMemberRole = new UserGroupMemberRole(addMember.getUserGroupId(),
							addMember.getRoleId(), userId);
					userGroupMemberDao.save(ugMemberRole);
					mappedUser.add(userId);
				}

			}
			return mappedUser;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

}
