package com.strandls.user.util;

import javax.inject.Inject;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.CommonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.user.pojo.User;
import com.strandls.user.service.UserService;
import com.sun.jersey.api.NotFoundException;

public class SimpleUsernamePasswordAuthenticator implements Authenticator<UsernamePasswordCredentials> {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	private UserService userService;

	@Inject
	private MessageDigestPasswordEncoder passwordEncoder; 
	//MessageDigestPasswordEncoder passwordEncoder = new MessageDigestPasswordEncoder("MD5");

	@Override
	public void validate(final UsernamePasswordCredentials credentials, final WebContext context)
			throws HttpAction, CredentialsException {
		if (credentials == null) {
			throwsException("No credential");
		}
		String username = credentials.getUsername().toLowerCase();
		String password = credentials.getPassword();

		if (CommonHelper.isBlank(username)) {
			throwsException("Username cannot be blank");
		}
		if (CommonHelper.isBlank(password)) {
			throwsException("Password cannot be blank");
		}

		log.debug("Validating credentials : " + credentials);

		User user = null;
		try {
			user = (User) userService.getUserByEmail(username);
		} catch(NotFoundException e ) {
			log.error("No user with email {}", username);
		}
		if (user == null) {
			throwsException("Not a valid user");
		}
		// TODO: using null salt and MD5 algorithm. Not safe. Upgrade to BCrypt
		else if (!passwordEncoder.isPasswordValid(user.getPassword(), password, null)) {
			throwsException("Password is not valid");
		} /*
			 * else if(user.isAccountLocked() == true) {
			 * throwsException("Account is locked. Please complete the account validation by clicking the link sent in activation email sent to your email account."
			 * ); }
			 */ 
		else {
			CommonProfile profile = AuthUtility.createUserProfile(user);
			log.debug("Setting profile in the context: " + profile);
			credentials.setUserProfile(profile);
		}
	}

	protected void throwsException(final String message) throws CredentialsException {
		throw new CredentialsException(message);
	}
}
