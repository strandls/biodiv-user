package com.strandls.user.auth;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.jwt.ClaimsSet;
import org.apache.oltu.oauth2.jwt.JWT;
import org.apache.oltu.oauth2.jwt.io.JWTReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.strandls.user.ApiConstants;
import com.strandls.user.pojo.User;
import com.strandls.user.service.AuthenticationService;
import com.strandls.user.service.UserService;
import com.strandls.user.util.AuthUtility;
import com.strandls.user.util.PropertyFileUtil;

import io.swagger.annotations.Api;

@Api("Google Service")
@Path(ApiConstants.GOOGLE_CALLBACK)
public class GoogleAuthorizationResource {
	
	private static final Logger logger = LoggerFactory.getLogger(GoogleAuthorizationResource.class);

	@Context
	private UriInfo uriInfo;
	
	@Inject
	private AuthenticationService authenticationService;
	
	@Inject
	private UserService userService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response authorize(@QueryParam("code") String code, @QueryParam("state") String state) {
		try {
			OAuthClientRequest request = OAuthClientRequest
					.tokenProvider(OAuthProviderType.GOOGLE)
					.setCode(code)
					.setClientId(PropertyFileUtil.fetchProperty("biodiv-api.properties", "googleId"))
					.setClientSecret(PropertyFileUtil.fetchProperty("biodiv-api.properties", "googleSecret"))
					.setRedirectURI(UriBuilder.fromUri(uriInfo.getBaseUri()).path("oauth2callback").build().toString())
					.setGrantType(GrantType.AUTHORIZATION_CODE)
					.buildBodyMessage();
			OAuthClient client = new OAuthClient(new URLConnectionClient());
			OAuthJSONAccessTokenResponse oAuthResponse = client.accessToken(request);
			
//			OAuthToken token = oAuthResponse.getOAuthToken();
			String jwtToken = oAuthResponse.getParam("id_token");
			JWT jwt = new JWTReader().read(jwtToken);
			ClaimsSet claims = jwt.getClaimsSet();
			String email = claims.getCustomField("email", String.class);
			User user = this.userService.getUserByEmail(email);
			
			// Implement this later
			if (user == null) {
				
			}
			
			Map<String, Object> tokens = this.authenticationService.buildTokens(AuthUtility.createUserProfile(user), user, true);						 
			return Response.status(Status.OK).cookie(new NewCookie("BAToken", tokens.get("access_token").toString()))
					.cookie(new NewCookie("BRToken", tokens.get("refresh_token").toString())).entity(tokens).build();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return Response.status(Status.FORBIDDEN).entity(ex.getMessage()).build();			
		}
	}
	
}
