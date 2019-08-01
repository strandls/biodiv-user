/**
 * 
 */
package com.strandls.user.service.impl;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.strandls.user.service.UserService;

/**
 * @author Abhishek Rudra
 *
 */
public class UserServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UserService.class).to(UserServiceImpl.class).in(Scopes.SINGLETON);
	}
}
