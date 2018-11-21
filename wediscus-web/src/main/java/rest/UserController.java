package rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.twilio.Twilio;

import annotation.Secured;
import dto.TokenDto;
import dto.UserResetDto;
import tn.esprit.wediscus.entity.Role;
import tn.esprit.wediscus.entity.User;
import tn.esprit.wediscus.services.TokenServiceRemote;
import tn.esprit.wediscus.services.UserServiceLocal;
import tn.esprit.wediscus.services.UserServiceRemote;
import util.SmsSender;

@Path("users")
@RequestScoped
public class UserController {
	
	
	private static String code;
	private Map<String, Object> resetMap = new HashMap<>() ;
	
	private final String BASE_URL = "http://localhost:18080/wediscus-web/wediscusrest/";
	Client client = ClientBuilder.newClient();
	
	
	@Context
	private SecurityContext securityContext;
	
	@EJB
	UserServiceLocal userService;
	
	@EJB
	TokenServiceRemote tokenService;
	
	 
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("add")
	public Response addUser(User u){
		System.out.print(u);
		return userService.addUser(u) == true ? Response.status(Status.CREATED).build() : 
			Response.status(Status.CONFLICT).entity("User Already exists").build();
	}

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("login")
	public Response testSecurityMethod(User user) throws URISyntaxException{
		
		WebTarget userEndpointTarget =  client.target(URI.create(BASE_URL)).path("authentication");
		Response response = userEndpointTarget.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON));
		return Response.ok().entity(response.getHeaderString("Authorization")).build();
		
	}
	
	@Secured({Role.ADMIN, Role.MODERATOR})
	@GET
	@Path("/user/secured-test")
	public Response testMethod(){
		User connected = userService.getConnectedUser(securityContext);
		System.out.println(connected);
		return Response.ok().entity("Connected user : "+connected).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(){
		List<User> users = userService.findAll().stream().collect(Collectors.toList());
		return users.isEmpty() ? Response.status(Status.NO_CONTENT).build() : 
			Response.status(Status.OK).entity(users).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response getUserById(@PathParam("id") int id){
		User users = userService.findUser(id);
		return users == null ? Response.status(Status.NO_CONTENT).build() : 
			Response.status(Status.OK).entity(users).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("reset-user")
	public Response resetPassword(User u){	
		try {
			User foundUser = userService.findUser(u);
			if(foundUser == null)
				throw new Exception();
			int randomPIN = (int) (Math.random() * 9000) + 1000;
	        code = "" + randomPIN;
	        
	         Twilio.setUsername(SmsSender.ACCOUNT_SID);
        	 Twilio.setPassword(SmsSender.AUTH_TOKEN);
			 SmsSender.SendSMS(foundUser.getPhoneNumber(), 
					 "Your sms code is : "+code);
			 tokenService.disableToken(foundUser);
			
		}catch (NoResultException e) {
			code = null;
			return Response.noContent().entity("User not found!").build();
		}catch (Exception e) {
			code = null;
			return Response.noContent().entity("User not found!").build();
		}
		 
		return Response.ok().entity(new String("Your sms code is : "+code)).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("change-pass")
	public Response changePass(UserResetDto userResetDto){
		if(code == userResetDto.getVerification()){
			code = null;
			User newUser = userService.findUser(userResetDto.toUser());
			if(userService.updateUser(newUser) == true){
				//resetMap.remove(code);
				return Response.ok().entity("Verify code found, you're on!").build();
			}
			else
				return Response.noContent().entity("Error while resetting your account!").build();
		}
		return Response.noContent().entity("Invalid verification code!").build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("check-token")
	public Response checkToken(TokenDto token){
		System.out.println(token);
		User user = null ;
		try{
			String tokenForUser = token.getToken().substring("Bearer".length())
					.trim();
			 user = tokenService.getUser(tokenForUser);
		}catch(Exception e){
			return Response.status(Status.UNAUTHORIZED).entity("Invalid verification code!").build();
		}
		return Response.ok().entity(user.getId()).build();		
	}
}
