package tn.esprit.wediscus.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.esprit.wediscus.entity.Post;
import tn.esprit.wediscus.entity.Thread;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tn.esprit.wediscus.entity.Category;

@Stateless
public class ThreadService implements ThreadServiceRemote{

	@PersistenceContext(unitName="wediscus-ejb")
	EntityManager em;
	@Override
	public boolean addThread(Thread thread) {

		if(showThreads().contains(thread))
			return false;
		em.persist(thread);
		return true;
		
	}

	@Override
	public boolean editThread(Thread thread) {

		Thread threadToUpdate = searchThread(thread.getId());
		if(threadToUpdate.equals(null))
			return false;
		em.getTransaction().begin();
		threadToUpdate = thread;
		em.getTransaction().commit();
		return true;
		
		
	} 

	@Override
	public Collection<Thread> showThreads() {

		Query query = em.createQuery("SELECT thread FROM Thread thread");
		Collection<Thread> listFromDB = (Collection<Thread>) query.getResultList();
		return listFromDB == null ? null : listFromDB;
		
	}

	@Override
	public boolean deleteThread(Thread thread) {
		if(showThreads().contains(thread))
		{
		em.remove(thread);
		return true;
		}
		return false;
	}
	
	@Override
	public boolean archiveThread(Thread thread) {
		Post threadToArchive = new Post();
		if (em.find(Post.class, thread.getId()) != null)
		{
			threadToArchive = em.find(Post.class, thread.getId());
		
		threadToArchive.setState(0);
		
		return true;
		}
		else
			return false;
		
	}

	@Override
	public Thread searchThread(int id) {

		Thread thread = em.find(Thread.class, id);
		return thread;
		
	}

}
