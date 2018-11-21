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

import tn.esprit.wediscus.entity.Post;
import tn.esprit.wediscus.services.PostServiceRemote;

@Path("Post")
public class PostController {

	@EJB
	private PostServiceRemote postService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)

	public Response addPost(Post post) {

		/*
		 * if (PostService.addPost(post)) { return
		 * Response.status(Status.ACCEPTED).build(); } else return
		 * Response.status(Status.BAD_REQUEST).build();
		 */
		System.out.print(post);
		return postService.addPost(post) == true ? Response.status(Status.CREATED).build()
				: Response.status(Status.CONFLICT).entity("Post Already exists").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	
	public Response getPosts() {

		List<Post> posts = postService.showPosts().stream().collect(Collectors.toList());
		return posts.isEmpty() ? Response.status(Status.NO_CONTENT).build()
				: Response.status(Status.OK).entity(posts).build();
	}

	@DELETE

	@Consumes({ MediaType.APPLICATION_JSON })
	public Response deletePost(Post post) {

		if (postService.deletePost(post))
			return Response.status(Status.OK).build();

		return Response.status(Status.NOT_FOUND).build();
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response updatePost(Post post) {
		if (postService.editPost(post))
			return Response.status(Status.OK).build();

		return Response.status(Status.NOT_FOUND).build();

	}

	@GET
	@Path("/post/{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response findPost(@PathParam("id") int id) {

		return Response.status(Status.OK).entity(postService.searchPost(id)).build();
	}
}
