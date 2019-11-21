package com.strandls.user.filter;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.strandls.authentication_utility.filter.UserValidationFilter;
import com.strandls.authentication_utility.filter.ValidateUser;

public class UserFilterModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(ValidateUser.class), new UserValidationFilter());
	}

}
