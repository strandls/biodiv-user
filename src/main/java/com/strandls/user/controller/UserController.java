/**
 * 
 */
package com.strandls.user.controller;

import java.util.ArrayList;
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
import com.strandls.user.Constants.SUCCESS_CONSTANTS;
import com.strandls.user.converter.UserConverter;
import com.strandls.user.dto.FirebaseDTO;
import com.strandls.user.pojo.FirebaseTokens;
import com.strandls.user.pojo.Follow;
import com.strandls.user.pojo.GroupAddMember;
import com.strandls.user.pojo.Recipients;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserGroupMemberRole;
import com.strandls.user.pojo.UserGroupMembersCount;
import com.strandls.user.pojo.UserIbp;
import com.strandls.user.pojo.UserPermissions;
import com.strandls.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.minidev.json.JSONArray;

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
	@Path(ApiConstants.BULK + ApiConstants.IBP)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Find User by User ID in bulk for ibp", notes = "Returns User details", response = UserIbp.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "User not found", response = String.class) })

	public Response getUserIbbpBulk(@QueryParam("userIds") String userIds) {
		try {
			List<Long> uIds = new ArrayList<Long>();
			for (String uId : userIds.split(","))
				uIds.add(Long.parseLong(uId));
			List<UserIbp> result = userService.fetchUserIbpBulk(uIds);
			return Response.status(Status.OK).entity(result).build();

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
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@ApiOperation(value = "Save Token", notes = "Associates token with a user", response = FirebaseTokens.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to return the data", response = String.class) })
	public Response saveToken(@Context HttpServletRequest request, FirebaseDTO firebaseDTO) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			FirebaseTokens savedToken = userService.saveToken(userId, firebaseDTO.getToken());
			return Response.ok().entity(savedToken).build();
		} catch (Exception ex) {
			return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
		}
	}
	
	@POST
	@Path(ApiConstants.SEND_NOTIFICATION)
	@ValidateUser
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Push Notifications", notes = "Send generalized push notifications to all users")
	public Response sendGeneralNotification(@Context HttpServletRequest request, FirebaseDTO firebaseDTO) {
		try {
			userService.sendPushNotifications(firebaseDTO.getTitle(), firebaseDTO.getBody(), firebaseDTO.getIcon());
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
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
	@ValidateUser
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Push Notifications", notes = "Send generalized push notifications to all users")
	public Response sendGeneralNotification(@Context HttpServletRequest request, FirebaseDTO firebaseDTO) {
		try {
			userService.sendPushNotifications(firebaseDTO.getTitle(), firebaseDTO.getBody(), firebaseDTO.getIcon());
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.GROUPMEMBER + ApiConstants.CHECK + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "checks the founder role", notes = "Returns boolean value", response = Boolean.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to fetch the data", response = String.class) })

	public Response checkFounderRole(@Context HttpServletRequest request,
			@PathParam("userGroupId") String userGroupId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Long ugId = Long.parseLong(userGroupId);
			Boolean result = userService.checkFounderRole(userId, ugId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.GROUPMEMBER + ApiConstants.CHECK + ApiConstants.MODERATOR + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "checks the Moderator role", notes = "Returns boolean value", response = Boolean.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to fetch the data", response = String.class) })

	public Response checkModeratorRole(@Context HttpServletRequest request,
			@PathParam("userGroupId") String userGroupId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Long ugId = Long.parseLong(userGroupId);
			Boolean result = userService.checkModeratorRole(userId, ugId);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.GROUPMEMBER)
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)

	@ApiOperation(value = "check if user is a member of the userGroup", notes = "Return Boolean if user is a member", response = Boolean.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to return the data", response = String.class) })

	public Response checkGroupMemberByUserId(@QueryParam("userGroupId") String userGroupId,
			@QueryParam("userId") String userId) {
		try {
			Long user = Long.parseLong(userId);
			Long ugId = Long.parseLong(userGroupId);
			Boolean result = userService.checkUserGroupMember(user, ugId);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.GROUP + ApiConstants.LEAVE + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)

	@ValidateUser

	@ApiOperation(value = "Leave a group", notes = "User can leave a group", response = Boolean.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to leave a group", response = String.class) })

	public Response leaveGroup(@Context HttpServletRequest request, @PathParam("userGroupId") String userGroupId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long ugId = Long.parseLong(userGroupId);
			Long userId = Long.parseLong(profile.getId());
			Boolean result = userService.removeGroupMember(userId, ugId);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@GET
	@Path(ApiConstants.GROUPMEMBER + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "find the userDetails for founder and moderator of the group", notes = "Return the list of user Details", response = User.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to fetch the list", response = String.class) })

	public Response getFounderModeratorList(@PathParam("userGroupId") String groupId) {
		try {
			Long userGroupId = Long.parseLong(groupId);
			List<User> result = userService.getFounderModerator(userGroupId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.GROUPMEMBER + ApiConstants.REMOVE)
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Remove a user from a group", notes = "Remove a user from a group", response = Boolean.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to remove user from a group", response = String.class) })

	public Response removeGroupMember(@Context HttpServletRequest request, @QueryParam("userId") String userId,
			@QueryParam("userGroupId") String userGroupId) {
		try {

			Long user = Long.parseLong(userId);
			Long ugId = Long.parseLong(userGroupId);
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long tokenUserId = Long.parseLong(profile.getId());
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Boolean isfounder = userService.checkFounderRole(tokenUserId, ugId);
			if (roles.contains("ROLE_ADMIN") || isfounder) {
				Boolean result = userService.removeGroupMember(user, ugId);
				return Response.status(Status.OK).entity(result).build();
			}
			return Response.status(Status.NOT_FOUND).entity("User dont have permission to perform the service").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.ADD + ApiConstants.GROUPMEMBER)
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "add new user to a usergroup", notes = "returns the usergroup role", response = UserGroupMemberRole.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to add a user", response = String.class) })

	public Response addMemberRoleUG(@Context HttpServletRequest request, @QueryParam("usergroupId") String userGroupId,
			@QueryParam("roleId") String roleId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Long role = Long.parseLong(roleId);
			Long ugId = Long.parseLong(userGroupId);
			UserGroupMemberRole result = userService.addMemberUG(userId, role, ugId);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.JOIN + ApiConstants.GROUP + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "User join a open group", notes = "Endpoint to join a openGroup", response = Boolean.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to join the Group", response = String.class) })

	public Response joinGroup(@Context HttpServletRequest request, @PathParam("userGroupId") String userGroupId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Long ugId = Long.parseLong(userGroupId);
			Boolean result = userService.joinGroup(userId, ugId);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.ADD + ApiConstants.GROUPMEMBER + ApiConstants.DIRECT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Adds the user directly to a Group", notes = "Adds the user directly to the group", response = Long.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to add the users", response = String.class) })

	public Response addGroupMemberDirectly(@Context HttpServletRequest request,
			@ApiParam(name = "GropAddMember") GroupAddMember groupAddMember) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			if (roles.contains("ROLE_ADMIN")) {
				List<Long> result = userService.addMemberDirectly(groupAddMember);
				return Response.status(Status.OK).entity(result).build();
			}
			return Response.status(Status.NOT_ACCEPTABLE).entity("User not allowed to perform the request").build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.GROUPMEMBER + ApiConstants.FOUNDERLIST + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "find founder list for a userGroup", notes = "return usser list for userGroupId", response = UserIbp.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to get the user list", response = String.class) })

	public Response getFounderList(@PathParam("userGroupId") String groupId) {
		try {
			Long userGroupId = Long.parseLong(groupId);
			List<UserIbp> result = userService.getFounderList(userGroupId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.GROUPMEMBER + ApiConstants.MODERATORLIST + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "find Moderator list for a userGroup", notes = "return usser list for userGroupId", response = UserIbp.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to get the user list", response = String.class) })

	public Response getModeratorList(@PathParam("userGroupId") String groupId) {
		try {
			Long userGroupId = Long.parseLong(groupId);
			List<UserIbp> result = userService.getModeratorList(userGroupId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
