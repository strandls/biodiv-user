/**
 * 
 */
package com.strandls.user.service;

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

	public UserPermissions getUserPermissions(Long userId);
	
	public User updateUser(User user);
}
