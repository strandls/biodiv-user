package com.strandls.user.controller;

import java.io.StringWriter;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.strandls.user.util.MailThread;
import com.strandls.user.util.MailUtil;
import com.strandls.user.util.MessageDigestPasswordEncoder;
import com.strandls.user.util.PropertyFileUtil;
import com.strandls.user.util.TemplateUtil;
import com.strandls.user.ApiConstants;
import com.strandls.user.ApplicationConfig;
import com.strandls.user.dto.UserDTO;
import com.strandls.user.pojo.Language;
import com.strandls.user.pojo.Role;
import com.strandls.user.pojo.User;
import com.strandls.user.service.AuthenticationService;
import com.strandls.user.service.LanguageService;
import com.strandls.user.service.RoleService;
import com.strandls.user.service.UserService;
import com.strandls.user.util.ValidationUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Authentication Service")
@Path(ApiConstants.V1 + ApiConstants.AUTHENTICATE)
public class AuthenticationController {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Inject
	private AuthenticationService authenticationService;

	@Inject
	private UserService userService;
	
	@Inject
	private RoleService roleService;
	
	@Inject 
	private LanguageService languageService;
	
	@Inject
	private Configuration configuration;

	@GET
	@Path(ApiConstants.PING)
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Ping", notes = "Pong", response = String.class)
	public Response getTestResponse() {
		return Response.status(Status.OK).entity("Pong").build();
	}

	@POST
	@Path(ApiConstants.LOGIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ApiOperation(value = "Authenticates User by Credentials", notes = "Returns Tokens", response = Map.class)
	@ApiResponses(value = {
			@ApiResponse(code = 403, message = "Could not authenticate user", response = String.class) })
	public Response authenticate(@FormParam("username") String userEmail, @FormParam("password") String password) {
		try {
			CommonProfile profile = this.authenticationService.authenticateUser(userEmail, password);
			Map<String, Object> tokens = this.authenticationService.buildTokens(profile,
					this.userService.fetchUser(Long.parseLong(profile.getId())), true);
			return Response.status(Status.OK).cookie(new NewCookie("BAToken", tokens.get("access_token").toString()))
					.cookie(new NewCookie("BRToken", tokens.get("refresh_token").toString())).entity(tokens).build();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return Response.status(Status.FORBIDDEN).entity(ex.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.REFRESH_TOKENS)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Generates new set of tokens based on the refresh token", notes = "Returns New Set of Tokens", response = Map.class)
	@ApiResponses(value = { @ApiResponse(code = 403, message = "Invalid refresh token", response = String.class) })
	public Response generateNewTokens(@QueryParam("refreshToken") String refreshToken) {
		CommonProfile profile = ApplicationConfig.jwtAuthenticator.validateToken(refreshToken);
		if (profile == null) {
			logger.debug("Invalid response token");
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Invalid refresh token").build();
		}
		try {
			// Retrieve the claims from JWT and call the buildTokens method to generate the
			// tokens
			Map<String, Object> tokens = this.authenticationService.buildTokens(profile,
					this.userService.fetchUser(Long.parseLong(profile.getId())), true);
			return Response.status(Status.OK).cookie(new NewCookie("BAToken", tokens.get("access_token").toString()))
					.cookie(new NewCookie("BRToken", tokens.get("refresh_token").toString())).entity(tokens).build();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return Response.status(Status.FORBIDDEN).entity(ex.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.VALIDATE_TOKEN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Validates access token", notes = "Returns if token is valid or not", response = Boolean.class)
	@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized access token", response = String.class),
			@ApiResponse(code = 406, message = "Invalid access token", response = String.class) })
	public Response validateToken(@QueryParam("accessToken") String accessToken) {
		if (accessToken == null || accessToken.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		try {
			CommonProfile profile = ApplicationConfig.jwtAuthenticator.validateToken(accessToken);
			boolean validToken = profile != null;
			return Response.status(validToken ? Status.OK : Status.UNAUTHORIZED).entity(String.valueOf(validToken))
					.build();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return Response.status(Status.FORBIDDEN).entity("Invalid Access Token").build();
		}
	}
	
	@POST
	@Path(ApiConstants.SIGNUP)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response signUp(@Context HttpServletRequest request, UserDTO userDTO) {
		try {
			String username = userDTO.getUsername();
			String email = userDTO.getEmail().toLowerCase().trim();
			String password = userDTO.getPassword();
			String confirmPassword = userDTO.getConfirmPassword();
			String location = userDTO.getLocation();
			Double latitude = userDTO.getLatitude();
			Double longitude = userDTO.getLongitude();
			if (username == null || username.isEmpty()) {
				return Response.status(Status.BAD_REQUEST).entity("Username cannot be empty").build();	
			}
			if (email == null || !ValidationUtil.validateEmail(email)) {
				return Response.status(Status.BAD_REQUEST).entity("Email empty or not valid").build();
			}
			if (!password.equals(confirmPassword) || password.length() < 8) {
				return Response.status(Status.BAD_REQUEST).entity("Password must be longer than 8 characters").build();
			}
			if (location == null) {
				return Response.status(Status.BAD_REQUEST).entity("Location cannot be null").build();
			}
			if (latitude == null) {
				return Response.status(Status.BAD_REQUEST).entity("Latitude cannot be null").build();
			}
			if (longitude == null) {
				return Response.status(Status.BAD_REQUEST).entity("Longitude cannot be null").build();
			}
			User user = authenticationService.addUser(request, userDTO);
			
			return Response.status(Status.OK).entity("1").build();
		} catch (Exception ex) {
			return Response.status(Status.BAD_REQUEST).entity("Could not create user").build();		
		}
	}
	
	@GET
	@Path("/validate")
	public Response validateAccount(@QueryParam("token") String token) {
		return Response.status(Status.OK).build();
	}

}
