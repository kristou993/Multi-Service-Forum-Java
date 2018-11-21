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

import tn.esprit.wediscus.entity.Thread;
import tn.esprit.wediscus.services.ThreadServiceRemote;

@Path("Thread")
public class ThreadController {

	@EJB
	ThreadServiceRemote ThreadService;
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)

	public Response addThread(Thread thread){
		
	/*	if (ThreadService.addThread(thread))
		{
		return Response.status(Status.ACCEPTED).build();
		}
		else
		return Response.status(Status.BAD_REQUEST).build(); */
		System.out.print(thread);
		return ThreadService.addThread(thread) == true ? Response.status(Status.CREATED).build() : 
			Response.status(Status.CONFLICT).entity("Thread Already exists").build();
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getThreads(){
	
		List<Thread> threads = ThreadService.showThreads().stream().collect(Collectors.toList());
		return threads.isEmpty() ? Response.status(Status.NO_CONTENT).build() : 
			Response.status(Status.OK).entity(threads).build();
	}
	
	@DELETE
	
	@Consumes({MediaType.APPLICATION_JSON})
	public Response deleteThread(Thread thread) {
		
		
		if (ThreadService.deleteThread(thread))
			return Response.status(Status.OK).build();
			
				return Response.status(Status.NOT_FOUND).build();
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	public Response updateThread(Thread thread) {
		if(ThreadService.editThread(thread))
			return Response.status(Status.OK).build();
			
				return Response.status(Status.NOT_FOUND).build();
		
	}
	
	@GET
	@Path("/Thread/{id}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response findThread(@PathParam("id")int id)
	{
		
		return Response.status(Status.OK).entity(ThreadService.searchThread(id)).build();
	}
}