/**
 * 
 */
package com.strandls.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.strandls.user.dao.FirebaseDao;
import com.strandls.user.dao.FollowDao;
import com.strandls.user.dao.UserDao;
import com.strandls.user.pojo.FirebaseTokens;
import com.strandls.user.pojo.Follow;
import com.strandls.user.pojo.Role;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserIbp;
import com.strandls.user.service.UserService;
import com.strandls.user.util.AuthUtility;

/**
 * @author Abhishek Rudra
 *
 */
public class UserServiceImpl implements UserService {

	@Inject
	private UserDao userDao;

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
	public User updateUser(Boolean isAdmin, User inputUser) {
	
		Long userId = inputUser.getId();

		User user = fetchUser(userId);

		if (isAdmin) {
			user = updateEmailAndMobile(user, inputUser);
			user = updateRolesAndPermission(user, inputUser);
		}
		user = updateOtherDetails(user, inputUser);

		return updateUser(user);
	}

	private User updateOtherDetails(User user, User inputUser) {
		if (inputUser.getUserName() != null && !user.getUserName().equals(inputUser.getUserName()))
			user.setUserName(inputUser.getUserName());

		if (inputUser.getSexType() != null && !user.getSexType().equals(inputUser.getSexType()))
			user.setSexType(inputUser.getSexType());

		if (inputUser.getOccupation() != null && !user.getOccupation().equals(inputUser.getOccupation()))
			user.setOccupation(inputUser.getOccupation());

		if (inputUser.getInstitution() != null && !user.getInstitution().equals(inputUser.getInstitution()))
			user.setInstitution(inputUser.getInstitution());

		if (inputUser.getLocation() != null && !user.getLocation().equals(inputUser.getLocation()))
			user.setLocation(inputUser.getLocation());

		if (inputUser.getLatitude() != null && !user.getLatitude().equals(inputUser.getLatitude()))
			user.setLatitude(inputUser.getLatitude());

		if (inputUser.getLongitude() != null && !user.getLongitude().equals(inputUser.getLongitude()))
			user.setLongitude(inputUser.getLongitude());

		if (inputUser.getAboutMe() != null && !user.getAboutMe().equals(inputUser.getAboutMe()))
			user.setAboutMe(inputUser.getAboutMe());

		 // TODO  : Species group and habitat id
		
		if (inputUser.getIdentificationMail() != null && !user.getIdentificationMail().equals(inputUser.getIdentificationMail()))
			user.setIdentificationMail(inputUser.getIdentificationMail());
		
		if(inputUser.getWebsite() != null && user.getWebsite().equals(inputUser.getWebsite()))
			user.setWebsite(inputUser.getWebsite());

		if (inputUser.getSendNotification() != null
				&& !user.getSendNotification().equals(inputUser.getSendNotification()))
			user.setSendNotification(inputUser.getSendNotification());

		if (inputUser.getHideEmial() != null && !user.getHideEmial().equals(inputUser.getHideEmial()))
			user.setHideEmial(inputUser.getHideEmial());

		if (inputUser.getSendDigest() != null && !user.getSendDigest().equals(inputUser.getSendDigest()))
			user.setSendDigest(inputUser.getSendDigest());

		return user;
	}

	private User updateRolesAndPermission(User user, User inputUser) {
		if (inputUser.getRoles() == null)
			return user;

		if (!inputUser.getRoles().containsAll(user.getRoles()) || !user.getRoles().containsAll(inputUser.getRoles()))
			user.setRoles(inputUser.getRoles());
		return user;
	}

	private User updateEmailAndMobile(User user, User inputUser) {

		if (inputUser.getEmail() != null && !user.getEmail().equals(inputUser.getEmail()))
			user.setEmail(inputUser.getEmail());

		if (inputUser.getMobileNumber() != null && !user.getMobileNumber().equals(inputUser.getMobileNumber()))
			user.setMobileNumber(inputUser.getMobileNumber());

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
	public List<UserIbp> fetchUserIbpBulk(List<Long> userIds) {
		List<UserIbp> result = new ArrayList<UserIbp>();
		for (Long userId : userIds) {
			result.add(fetchUserIbp(userId));
		}
		return result;
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

}
