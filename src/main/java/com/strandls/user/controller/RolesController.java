package com.strandls.user.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.strandls.user.ApiConstants;
import com.strandls.user.pojo.Role;
import com.strandls.user.service.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Role Service")
@Path(ApiConstants.V1 + ApiConstants.ROLES)
public class RolesController {

	@Inject
	private RoleService roleService;
	
	@GET
	@Path(ApiConstants.PING)
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Dummy API Ping", notes = "Checks validity of war file at deployment", response = String.class)
	public Response ping() throws Exception {
		return Response.status(Status.OK).entity("PONG").build();
	}
	
	@GET
	@Path("all")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Get all roles", notes = "Returns all the roles available", response = Role.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Roles not found", response = String.class) })
	public Response getAllRoles () {
		List<Role> roles = roleService.getAllRoles();
		return Response.status(Status.OK).entity(roles).build();
	}
}
