/**
 * 
 */
package com.strandls.user.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.strandls.user.ApiConstants;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserIbp;
import com.strandls.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

/**
 * @author Abhishek Rudra
 *
 */

@Api("User Service")
@SwaggerDefinition(tags = {
		@Tag(name = "User Serivce to get user Details", description = "Rest endpoint for User Service") })
@Path(ApiConstants.V1 + ApiConstants.USER)
public class UserController {

	@Inject
	private UserService userSerivce;

	@GET
	@Path(ApiConstants.PING)
	@Produces(MediaType.TEXT_PLAIN)

	@ApiOperation(value = "Dummy API Ping", notes = "Checks validity of war file at deployment", response = String.class)
	public Response ping() {
		return Response.status(Status.OK).entity("PONG").build();
	}

	@GET
	@Path("/{userId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Find User by User ID", notes = "Returns User details", response = User.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = User.class),
			@ApiResponse(code = 404, message = "Traits not found", response = String.class) })

	public Response getUser(@PathParam("userId") String userId) {

		try {
			Long uId = Long.parseLong(userId);
			User user = userSerivce.fetchUser(uId);
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
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = UserIbp.class),
			@ApiResponse(code = 404, message = "Traits not found", response = String.class) })

	public Response getUserIbp(@PathParam("userId") String userId) {
		try {
			Long id = Long.parseLong(userId);
			UserIbp ibp = userSerivce.fetchUserIbp(id);
			return Response.status(Status.OK).entity(ibp).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
}
