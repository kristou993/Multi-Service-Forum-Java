package tn.esprit.wediscus.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.esprit.wediscus.entity.Category;
import tn.esprit.wediscus.entity.Post;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tn.esprit.wediscus.entity.Category;

@Stateless
public class PostService implements PostServiceRemote{

	@PersistenceContext(unitName="wediscus-ejb")
	EntityManager em;
	@Override
	public boolean addPost(Post post) {

	if(showPosts().contains(post))
			return false;
		em.persist(post);
		return true;
		
	}

	@Override
	public boolean editPost(Post post) {

	
		Post postToUpdate = new Post();
		if (em.find(Post.class, post.getId()) != null)
		{
			postToUpdate = em.find(Post.class, post.getId());
		
		postToUpdate.setContent(post.getContent());
		postToUpdate.setDate(post.getDate());
		postToUpdate.setRating(post.getRating());
		postToUpdate.setThread(post.getThread());
		return true;
		}
		else
			return false;
		
	}

	@Override
	public Collection<Post> showPosts(){
		Query query = em.createQuery("SELECT p FROM Post p");
		Collection<Post> listFromDB = (Collection<Post>) query.getResultList();
		
		return listFromDB == null ? null : listFromDB;
	}

	@Override
	public boolean deletePost(Post post) {
		int id = post.getId();
		if(showPosts().contains(post))
		{
	/*		Query query = em.createQuery("DELETE FROM Post p WHERE p.id = :post");
			query.setParameter("id", post.getId());
			query.executeUpdate();
				return true;
		}
		return false; */
		em.remove(em.find(Post.class, id));
		return true;
		}
		return false;
		
	}
	
	@Override
	public boolean archivePost(Post post) {
		Post postToArchive = new Post();
		if (em.find(Post.class, post.getId()) != null)
		{
			postToArchive = em.find(Post.class, post.getId());
		
		postToArchive.setState(0);
		
		return true;
		}
		else
			return false;
		
	}
	

	@Override
	public Post searchPost(int id) {
		Post post = em.find(Post.class, id);
		return post;
	}
	
	@Override
	public Collection<Post> searchPostByContent(String content) {
		Query query = em.createQuery("SELECT p FROM Post p WHERE p.content = :content");
		query.setParameter("content", content);
		Collection<Post> listFromDB = (Collection<Post>) query.getResultList();
		return listFromDB == null ? null : listFromDB;
	}
	
	@Override
	public Collection<Post> searchPostByDate(Date date) {
		Query query = em.createQuery("SELECT p FROM Post p WHERE p.date = :date");
		query.setParameter("date", date);
		Collection<Post> listFromDB = (Collection<Post>) query.getResultList();
		return listFromDB == null ? null : listFromDB;
	}
	
	@Override
	public Collection<Post> searchPostByThread(int id) {
		Query query = em.createQuery("SELECT p FROM Post p WHERE p.thread_id = :id");
		query.setParameter("id", id);
		Collection<Post> listFromDB = (Collection<Post>) query.getResultList();
		return listFromDB == null ? null : listFromDB;
	}
	
	@Override
	public Collection<Post> searchPostByRating(int rating) {
		Query query = em.createQuery("SELECT p FROM Post p WHERE p.rating = :rating");
		query.setParameter("rating", rating);
		Collection<Post> listFromDB = (Collection<Post>) query.getResultList();
		return listFromDB == null ? null : listFromDB;
	}
	@Override
	public Collection<Post> searchPostByWarningLevel(int warning) {
		Query query = em.createQuery("SELECT p FROM Post p WHERE p.warning = :warning");
		query.setParameter("warning", warning);
		Collection<Post> listFromDB = (Collection<Post>) query.getResultList();
		return listFromDB == null ? null : listFromDB;
	}
	
	@Override
	public Collection<Post> showClientPosts() {
		Query query = em.createQuery("SELECT p FROM Post p WHERE p.state = 1");
		
		Collection<Post> listFromDB = (Collection<Post>) query.getResultList();
		return listFromDB == null ? null : listFromDB;
	}
	
	@Override
	public Collection<Post> checkWarnings() {
		Query query = em.createQuery("SELECT p FROM Post p");
		
		Collection<Post> listFromDB = (Collection<Post>) query.getResultList();
		
		for(Post p : listFromDB)
		{
			if(p.getWarning()>4)
			{
				p.setState(0);
			}
		
		}
			return listFromDB == null ? null : listFromDB;
	}

}
