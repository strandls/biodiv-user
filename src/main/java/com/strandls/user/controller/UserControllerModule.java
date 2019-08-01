/**
 * 
 */
package com.strandls.user.controller;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * @author Abhishek Rudra
 *
 */
public class UserControllerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UserController.class).in(Scopes.SINGLETON);
	}
}
