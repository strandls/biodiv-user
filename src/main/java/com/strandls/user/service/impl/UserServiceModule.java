/**
 * 
 */
package com.strandls.user.service.impl;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.strandls.user.service.AuthenticationService;
import com.strandls.user.service.LanguageService;
import com.strandls.user.service.MailService;
import com.strandls.user.service.RoleService;
import com.strandls.user.service.UserService;

/**
 * @author Abhishek Rudra
 *
 */
public class UserServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UserService.class).to(UserServiceImpl.class).in(Scopes.SINGLETON);
		bind(AuthenticationService.class).to(AuthenticationServiceImpl.class).in(Scopes.SINGLETON);
		bind(RoleService.class).to(RoleServiceImpl.class).in(Scopes.SINGLETON);
		bind(LanguageService.class).to(LanguageServiceImpl.class).in(Scopes.SINGLETON);
		bind(MailService.class).to(MailServiceImpl.class).in(Scopes.SINGLETON);
	}
}
