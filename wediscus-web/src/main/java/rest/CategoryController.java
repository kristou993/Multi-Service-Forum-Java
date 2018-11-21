package rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.wediscus.entity.Category;
import tn.esprit.wediscus.services.CategoryServiceRemote;

@Path("Category")
public class CategoryController {

	@EJB
	CategoryServiceRemote CategoryService;
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)

	public Response addCategory(Category category){
		
	/*	if (CategoryService.addCategory(category))
		{
		return Response.status(Status.ACCEPTED).build();
		}
		else
		return Response.status(Status.BAD_REQUEST).build(); */
		System.out.print(category);
		return CategoryService.addCategory(category) == true ? Response.status(Status.CREATED).build() : 
			Response.status(Status.CONFLICT).entity("Category Already exists").build();
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategorys(){
	
		List<Category> categories = CategoryService.showCategories().stream().collect(Collectors.toList());
		return categories.isEmpty() ? Response.status(Status.NO_CONTENT).build() : 
			Response.status(Status.OK).entity(categories).build();
	}
	
	@DELETE
	
	@Consumes({MediaType.APPLICATION_JSON})
	public Response deleteCategory(Category category) {
		
		
		if (CategoryService.deleteCategory(category))
			return Response.status(Status.OK).build();
			
				return Response.status(Status.NOT_FOUND).build();
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	public Response updateCategory(Category category) {
		if(CategoryService.editCategory(category))
			return Response.status(Status.OK).build();
			
				return Response.status(Status.NOT_FOUND).build();
		
	}
	
	@GET
	@Path("/Category/{id}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response findCategory(@PathParam("id")int id)
	{
		
		return Response.status(Status.OK).entity(CategoryService.searchCategory(id)).build();
	}
}