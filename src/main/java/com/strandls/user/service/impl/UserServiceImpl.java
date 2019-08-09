/**
 * 
 */
package com.strandls.user.service.impl;

import com.google.inject.Inject;
import com.strandls.user.dao.UserDao;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserIbp;
import com.strandls.user.service.UserService;

/**
 * @author Abhishek Rudra
 *
 */
public class UserServiceImpl implements UserService{

	@Inject
	private UserDao userDao;
	
	@Override
	public User fetchUser(Long userId) {
		User user= userDao.findById(userId);
		return user;
	}

	@Override
	public UserIbp fetchUserIbp(Long userId) {
		User user = userDao.findById(userId);
		UserIbp ibp = new UserIbp(user.getId(),user.getName(), user.getProfilePic());
		return ibp;
	}

}
