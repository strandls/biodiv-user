/**
 * 
 */
package com.strandls.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.rabbitmq.client.Channel;
import com.strandls.user.dao.FirebaseDao;
import com.strandls.user.dao.FollowDao;
import com.strandls.user.dao.SpeciesPermissionDao;
import com.strandls.user.dao.UserDao;
import com.strandls.user.dao.UserGroupMemberRoleDao;
import com.strandls.user.pojo.FirebaseTokens;
import com.strandls.user.pojo.Follow;
import com.strandls.user.pojo.Role;
import com.strandls.user.pojo.SpeciesPermission;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserGroupMemberRole;
import com.strandls.user.pojo.UserGroupMembersCount;
import com.strandls.user.pojo.UserIbp;
import com.strandls.user.pojo.UserPermissions;
import com.strandls.user.service.UserService;
import com.strandls.user.util.NotificationScheduler;

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

	@Inject
	private FirebaseDao firebaseDao;

	@Inject
	private FollowDao followDao;
	
	@Inject
	Channel channel;

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
		FirebaseTokens token = firebaseDao.getToken(userId, fcmToken);
		try {
			if (token == null) {
				User user = fetchUser(userId);
				user.setSendPushNotification(true);
				updateUser(user);
				FirebaseTokens savedToken = new FirebaseTokens(user, fcmToken);
				token = firebaseDao.save(savedToken);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return token;
	}

	@Override
	public List<UserGroupMembersCount> getUserGroupMemberCount() {
		List<UserGroupMembersCount> result = userGroupMemberDao.fetchMemberCountUserGroup();
		return result;
	}
	
	@Override
	public void sendPushNotifications(String title, String body) {
		try {
			List<FirebaseTokens> tokens = firebaseDao.findAll();
			NotificationScheduler scheduler = new NotificationScheduler(channel, title, body, tokens);
			scheduler.start();			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
