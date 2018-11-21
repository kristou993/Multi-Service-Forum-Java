package rest;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import annotation.Secured;
import tn.esprit.wediscus.entity.Role;
import tn.esprit.wediscus.entity.User;
import tn.esprit.wediscus.services.TokenServiceRemote;
import tn.esprit.wediscus.services.UserServiceRemote;



@Secured
@Priority(Priorities.AUTHENTICATION)
@Provider
public class SecurityFilter implements ContainerRequestFilter{
	
	
	@EJB
	UserServiceRemote userService;
	
	@EJB
	TokenServiceRemote tokenService;
	@Context
    private ResourceInfo resourceInfo;

	private static final String AUTHROIZATION_HEADER_PREFIX = "Bearer";
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		Response unauthorizedResponse = null;
		String tokenForUser;
		try {
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                // Extract the token from the HTTP Authorization header
				tokenForUser = authorizationHeader.substring(AUTHROIZATION_HEADER_PREFIX.length())
						.trim();
				System.out.println(tokenForUser);
            }
			else if (requestContext.getCookies().containsKey("auth_token")) {
				 tokenForUser = requestContext.getCookies().get("auth_token").getValue();
	        }
			else{
				System.out.println("NO HEADER OR COOKIES FOUND... ");
				throw new Exception();
			}
			final User user = tokenService.getUser(tokenForUser);
				

            final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return new Principal() {
                        @Override
                        public String getName() {
                            return user.getUsername();
                        }
                    };
                }

                @Override
                public boolean isUserInRole(String s) {
                    return true;
                }

                @Override
                public boolean isSecure() {
                    return currentSecurityContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Bearer";
                }
            });
			requestContext.abortWith(Response.status(Status.OK).header("Authorization", tokenForUser).build());
		} 
		catch (Exception e) {
			unauthorizedResponse = Response.status(Status.UNAUTHORIZED)
					.entity("Not authorized!")
					.build();
			requestContext.abortWith(unauthorizedResponse);
		}
		
	}

}
