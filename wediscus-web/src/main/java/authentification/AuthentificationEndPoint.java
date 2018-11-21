package authentification;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;

import tn.esprit.wediscus.entity.User;
import tn.esprit.wediscus.services.TokenServiceRemote;
import tn.esprit.wediscus.services.UserServiceLocal;
import tn.esprit.wediscus.services.UserServiceRemote;

@Path("authentication")
@RequestScoped
public class AuthentificationEndPoint {
	
	@EJB
	UserServiceLocal userService;
	@EJB
	TokenServiceRemote tokenService;
	
	@Context
	SecurityContext securityContext;
	
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.WILDCARD)
    
    public Response authenticateUser(User u) throws Exception {
    	// Authenticate the user using the credentials provided
        if(authenticate(u) == false){	
        	System.out.println("Auth failed, Exiting with FORBIDDEN status");
        	return Response.status(Status.FORBIDDEN).entity("Not allow here son!").build();
        }
        System.out.println("Authentification passed!");
        User user = userService.findUser(u);
        String token = issueToken(user);
        System.out.println("Our token is now : "+token);

        return Response.ok(token).header("Authorization", token).build();
    }

    private boolean authenticate(User u) throws Exception{
    	if(userService.login(u) == false)
    		return false;
    	return true;
    }

    private String issueToken(User u) throws UnsupportedEncodingException {
    	
    	Random random = new SecureRandom();
        String token = new BigInteger(130, random).toString(32);
        tokenService.setToken(token, u);
		return "Bearer "+token;
    }
}