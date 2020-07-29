/**
 * 
 */
package com.strandls.user.service;

import java.util.List;

import com.strandls.user.dto.FirebaseDTO;
import com.strandls.user.pojo.FirebaseTokens;
import com.strandls.user.pojo.Follow;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserIbp;

/**
 * @author Abhishek Rudra
 *
 */
public interface UserService {

	public User fetchUser(Long userId);

	public UserIbp fetchUserIbp(Long userId);

	public List<UserIbp> fetchUserIbpBulk(List<Long> userIds);

	public User getUserByEmailOrMobile(String data);

	public User getUserByEmail(String userEmail);

	public User getUserByMobile(String mobileNumber);

	public User updateUser(User user);

	public Follow fetchByFollowId(Long id);

	public List<User> fetchRecipients(String objectType, Long objectId);

	public Follow fetchByFollowObject(String objectType, Long objectId, Long authorId);

	public List<Follow> fetchFollowByUser(Long authorId);

	public Follow updateFollow(String objectType, Long objectId, Long userId);

	public Follow unFollow(String type, Long objectId, Long userId);

	public List<User> getNames(String name);

	public FirebaseTokens saveToken(Long userId, String token);

	public void sendPushNotifications(FirebaseDTO firebaseDTO);
}
