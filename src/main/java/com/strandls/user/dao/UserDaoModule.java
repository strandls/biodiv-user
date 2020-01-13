/**
 * 
 */
package com.strandls.user.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * @author Abhishek Rudra
 *
 */
public class UserDaoModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UserDao.class).in(Scopes.SINGLETON);
		bind(SpeciesPermissionDao.class).in(Scopes.SINGLETON);
		bind(UserGroupMemberRoleDao.class).in(Scopes.SINGLETON);
	}
}
