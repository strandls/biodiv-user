package com.strandls.user.auth;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.OAuthProviderType;

import com.strandls.user.util.PropertyFileUtil;

import io.swagger.annotations.Api;

@Api("Google Callback Service")
@Path("/google")
public class GoogleResource {
	
	@Context
	private UriInfo uriInfo;
	
	@GET
	@Path("/auth")
	@Produces("text/html")
	public Response authenticate() {
		try {
			OAuthClientRequest request = OAuthClientRequest
					.authorizationProvider(OAuthProviderType.GOOGLE)
					.setClientId(PropertyFileUtil.fetchProperty("biodiv-api.properties", "googleId"))
					.setResponseType("code")
					.setScope("profile email")
					.setRedirectURI(UriBuilder.fromUri(uriInfo.getBaseUri()).path("oauth2callback").build().toString())
					.buildQueryMessage();
			URI redirect = new URI(request.getLocationUri());
			return Response.seeOther(redirect).build();
 		} catch (Exception ex) {
 			return Response.status(Status.FORBIDDEN).entity(ex.getMessage()).build();
 		}
	}

}
