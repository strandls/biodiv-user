/**
 * 
 */
package com.strandls.user.controller;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.pac4j.core.profile.CommonProfile;

import com.strandls.authentication_utility.filter.ValidateUser;
import com.strandls.authentication_utility.util.AuthUtil;
import com.strandls.user.ApiConstants;
import com.strandls.user.converter.UserConverter;
import com.strandls.user.pojo.FirebaseTokens;
import com.strandls.user.pojo.Follow;
import com.strandls.user.pojo.Recipients;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserGroupMembersCount;
import com.strandls.user.pojo.UserIbp;
import com.strandls.user.pojo.UserPermissions;
import com.strandls.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Abhishek Rudra
 *
 */

@Api("User Service")
@Path(ApiConstants.V1 + ApiConstants.USER)
public class UserController {

	@Inject
	private UserService userService;

	@GET
	@Path(ApiConstants.PING)
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Dummy API Ping", notes = "Checks validity of war file at deployment", response = String.class)
	public Response ping() throws Exception {
		return Response.status(Status.OK).entity("PONG").build();
	}

	@GET
	@Path("/{userId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Find User by User ID", notes = "Returns User details", response = User.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Traits not found", response = String.class) })

	public Response getUser(@PathParam("userId") String userId) {

		try {

			Long uId = Long.parseLong(userId);
			User user = userService.fetchUser(uId);
			return Response.status(Status.OK).entity(user).build();
		} catch (Exception e) {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@GET
	@Path(ApiConstants.IBP + "/{userId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Find User by User ID for ibp", notes = "Returns User details", response = UserIbp.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "User not found", response = String.class) })

	public Response getUserIbp(@PathParam("userId") String userId) {
		try {
			Long id = Long.parseLong(userId);
			UserIbp ibp = userService.fetchUserIbp(id);
			return Response.status(Status.OK).entity(ibp).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path(ApiConstants.ME)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Find the Current user Details", notes = "Returns the Current User Details", response = User.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "User not found", response = String.class) })

	public Response getCurretUser(@Context HttpServletRequest request) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long uId = Long.parseLong(profile.getId());
			User user = userService.fetchUser(uId);
			return Response.status(Status.OK).entity(user).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.PERMISSIONS + "/{objectType}/{objectId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Finds all the allowed Permissions", notes = "Returns All permission of the User", response = UserPermissions.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to fetch the User Permission", response = String.class) })

	public Response getAllUserPermission(@Context HttpServletRequest request,
			@PathParam("objectType") String objectType, @PathParam("objectId") String objectId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Long objId = Long.parseLong(objectId);
			if (objectType.equalsIgnoreCase("observation"))
				objectType = "species.participation.Observation";
			UserPermissions permission = userService.getUserPermissions(userId, objectType, objId);

			return Response.status(Status.OK).entity(permission).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.PERMISSIONS)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser

	@ApiOperation(value = "Finds all the allowed userGroup Permissions", notes = "Returns All permission of the User", response = UserPermissions.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to fetch the User Permission", response = String.class) })

	public Response getUserGroupPermissions(@Context HttpServletRequest request) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			UserPermissions permission = userService.getUserPermissions(userId, null, null);

			return Response.status(Status.OK).entity(permission).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@GET
	@Path(ApiConstants.FOLLOW + "/{followId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Find follow by followid", notes = "Return follows", response = Follow.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Follow not Found", response = String.class) })

	public Response getByFollowID(@PathParam("followId") String followId) {

		try {
			Long id = Long.parseLong(followId);
			Follow follow = userService.fetchByFollowId(id);
			return Response.status(Status.OK).entity(follow).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path(ApiConstants.OBJECTFOLLOW + "/{objectType}/{objectId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser
	@ApiOperation(value = "Find follow by objectId", notes = "Return follows", response = Follow.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Follow not Found", response = String.class) })

	public Response getFollowByObject(@Context HttpServletRequest request, @PathParam("objectType") String objectType,
			@PathParam("objectId") String objectId) {
		try {

			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long objId = Long.parseLong(objectId);
			Long authId = Long.parseLong(profile.getId());
			if (objectType.equalsIgnoreCase("observation"))
				objectType = "species.participation.Observation";
			Follow follow = userService.fetchByFollowObject(objectType, objId, authId);
			return Response.status(Status.OK).entity(follow).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path(ApiConstants.USERFOLLOW + "/{userId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser
	@ApiOperation(value = "Find follow by userID", notes = "Return list follows", response = Follow.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Follow not Found", response = String.class) })

	public Response getFollowbyUser(@Context HttpServletRequest request) {

		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long id = Long.parseLong(profile.getId());
			List<Follow> follows = userService.fetchFollowByUser(id);
			return Response.status(Status.OK).entity(follows).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}

	}

	@POST
	@Path(ApiConstants.FOLLOW)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser

	@ApiOperation(value = "Marks follow for a User", notes = "Returnt the follow details", response = Follow.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to mark follow", response = String.class) })

	public Response updateFollow(@Context HttpServletRequest request, @FormParam("object") String object,
			@FormParam("objectId") String objectId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			if (object.equalsIgnoreCase("observation"))
				object = "species.participation.Observation";
			Long objId = Long.parseLong(objectId);
			Follow result = userService.updateFollow(object, objId, userId);

			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.UNFOLLOW + "/{type}/{objectId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser

	@ApiOperation(value = "Marks unfollow for a User", notes = "Returnt the follow details", response = Follow.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to mark unfollow", response = String.class) })

	public Response unfollow(@Context HttpServletRequest request, @PathParam("type") String type,
			@PathParam("objectId") String objectId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Long objId = Long.parseLong(objectId);
			if (type.equalsIgnoreCase("observation"))
				type = "species.participation.Observation";
			Follow result = userService.unFollow(type, objId, userId);

			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.GROUPMEMBER + "/{usergroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)

	@ValidateUser
	@ApiOperation(value = "check if user is a member of the userGroup", notes = "Return Boolean if user is a member", response = Boolean.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to return the data", response = String.class) })

	public Response checkMemberRoleUG(@Context HttpServletRequest request,
			@PathParam("usergroupId") String usergroupId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Long ugId = Long.parseLong(usergroupId);
			Boolean result = userService.checkUserGroupMember(userId, ugId);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.AUTOCOMPLETE)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Names autocomplete", notes = "Returns list of names", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to return the data", response = String.class) })
	public Response autocomplete(@QueryParam("name") String name) {
		try {
			Set<UserIbp> users = UserConverter.convertToIbpSet(userService.getNames(name));
			return Response.ok().entity(users).build();
		} catch (Exception ex) {
			return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.RECIPIENTS)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Fetches recipients", notes = "Returns list of recipients", response = Recipients.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to return the data", response = String.class) })
	public Response getRecipients(@FormParam("objectType") String objectType, @FormParam("objectId") Long objectId) {
		try {
			List<Recipients> users = UserConverter
					.convertToRecipientList(userService.fetchRecipients(objectType, objectId));
			System.out.println("***** Total Recipients #: " + users.size() + " *****");
			for (Recipients recipient : users) {
				System.out.println("***** Recipient #: " + recipient.getId() + " *****");
			}
			return Response.ok().entity(users).build();
		} catch (Exception ex) {
			return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.SAVE_TOKEN)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ValidateUser
	@ApiOperation(value = "Save Token", notes = "Associates token with a user", response = FirebaseTokens.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to return the data", response = String.class) })
	public Response saveToken(@Context HttpServletRequest request, @FormParam("token") String token) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			FirebaseTokens savedToken = userService.saveToken(userId, token);
			return Response.ok().entity(savedToken).build();
		} catch (Exception ex) {
			return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.GROUPMEMBER + ApiConstants.COUNT)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Calculate the userGroupId with member counts", notes = "Returns the userGroupId with member counts", response = UserGroupMembersCount.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to fetch the information", response = String.class) })

	public Response getMemberCounts() {
		try {
			List<UserGroupMembersCount> result = userService.getUserGroupMemberCount();
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	@POST
	@Path(ApiConstants.SEND_NOTIFICATION)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Push Notifications", notes = "Send generalized push notifications to all users")
	public Response sendGeneralNotification(@FormParam("title") String title, @FormParam("body") String body) {
		try {
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}		
	}

}
