package rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;

import annotation.Secured;
import tn.esprit.wediscus.entity.Article;
import tn.esprit.wediscus.entity.Role;
import tn.esprit.wediscus.entity.User;
import tn.esprit.wediscus.services.ArticleServiceRemote;
import tn.esprit.wediscus.services.UserServiceRemote;
@Path("article")
@RequestScoped
public class ArticleController {
	
	@EJB
	ArticleServiceRemote metier;
	
	@EJB
	UserServiceRemote userService;
	@Context
	SecurityContext securityContext;
	
	@POST
	//@Secured({Role.ADMIN})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response addArticle(Article article)
	
	{
		//User user = userService.getConnectedUser(securityContext);
		//article.setUser(user);
		if(metier.addArticle(article))
		return Response.status(Status.CREATED).entity("DONE").build();
		
			return Response.status(Status.NOT_FOUND).build();
		
	}
	
	@GET
	//@Secured({Role.ADMIN})
	@Produces({MediaType.APPLICATION_JSON})
	public Response GetAllArticle()
	{
		
		return Response.status(Status.OK).entity(metier.findAllArticle()).build();
	}
	
	@PUT
	//@Secured({Role.ADMIN})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response UpdateArticle(Article article) {
		if(metier.updateArticle(article))
			return Response.status(Status.OK).build();
			
				return Response.status(Status.NOT_FOUND).build();
		
	}
	
	@DELETE
	//@Secured({Role.ADMIN})
	
	
	@Consumes({MediaType.APPLICATION_JSON})
	public Response DeleteArticle(Article article) {
		
		
		if (metier.deleteArticle(article))
			return Response.status(Status.OK).build();
			
				return Response.status(Status.NOT_FOUND).build();
		
		
	}
	
	@GET
	@Path("/title/{title}")
	@Secured({Role.ADMIN})
	@Produces({MediaType.APPLICATION_JSON})
	public Response GetArticleByTitle(@PathParam("title")String title)
	{
		
		return Response.status(Status.OK).entity(metier.findArticlebytitle(title)).build();
	}
	
	@GET
	@Path("/type/{type}")
	@Secured({Role.ADMIN})
	@Produces({MediaType.APPLICATION_JSON})
	public Response GetArticleByType(@PathParam("type")String type)
	{
		
		return Response.status(Status.OK).entity(metier.findArticlebyType(type)).build();
	}
	

	@GET
	@Path("/date")
	@Secured({Role.ADMIN})
	@Produces({MediaType.APPLICATION_JSON})
	public Response GetArticleByDate(@QueryParam("jour")int jour,
			@QueryParam("mois")int mois,
			@QueryParam("annee") int annee) throws ParseException
	{
		/*Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, annee);
		c.set(Calendar.MONTH, mois-1);
		c.set(Calendar.DAY_OF_MONTH, jour);
		c.set(Calendar.HOUR, 2);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);*/
	
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// Date date1 = simpleDateFormat.parse(date);
		return Response.status(Status.OK).entity(metier.findArticlebyDate(annee,mois,jour)).build();
	}


	@GET
	@Path("/user/{user}")
	@Secured({Role.ADMIN})
	@Produces({MediaType.APPLICATION_JSON})
	public Response GetArticleByUser(@PathParam("user")String user)
	{
		
		return Response.status(Status.OK).entity(metier.findArticlebyUser(user)).build();
	}
	

	
	
	
	
	

}
