/**
 * 
 */
package com.strandls.user.service;

import java.util.List;

import com.strandls.user.pojo.Follow;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserIbp;
import com.strandls.user.pojo.UserPermissions;

/**
 * @author Abhishek Rudra
 *
 */
public interface UserService {

	public User fetchUser(Long userId);

	public UserIbp fetchUserIbp(Long userId);

	public User getUserByEmailOrMobile(String data);

	public User getUserByEmail(String userEmail);

	public User getUserByMobile(String mobileNumber);

	public User updateUser(User user);

	public UserPermissions getUserPermissions(Long userId, String type, Long objectId);

	public Follow fetchByFollowId(Long id);

	public Follow fetchByFollowObject(String objectType, Long objectId, Long authorId);

	public List<Follow> fetchFollowByUser(Long authorId);

	public Follow updateFollow(String objectType, Long objectId, Long userId);

	public Follow unFollow(String type, Long objectId, Long userId);
}
