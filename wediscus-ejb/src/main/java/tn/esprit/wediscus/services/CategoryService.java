package tn.esprit.wediscus.services;


import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.esprit.wediscus.entity.Category;
import tn.esprit.wediscus.entity.Post;

@Stateless
public class CategoryService implements CategoryServiceRemote{

	@PersistenceContext(unitName="wediscus-ejb")
	EntityManager em;
	@Override
	public boolean addCategory(Category category) {
		if(showCategories().contains(category))
			return false;
		em.persist(category);
		return true;
	}

	@Override
	public boolean editCategory(Category category) {
		
		Category catToUpdate = searchCategory(category.getId());
		if(catToUpdate.equals(null))
			return false;
		em.getTransaction().begin();
		catToUpdate = category;
		em.getTransaction().commit();
		return true;
		
	}

	@Override
	public Collection<Category> showCategories() {
		Query query = em.createQuery("SELECT category FROM Category category");
		Collection<Category> listFromDB = (Collection<Category>) query.getResultList();
		return listFromDB == null ? null : listFromDB;
		
	}

	@Override
	public boolean deleteCategory(Category category) {
		if(showCategories().contains(category))
		{
		em.remove(category);
		return true;
		}
		return false;
	}
	@Override
	public boolean archiveCategory(Category category) {
		Post categoryToArchive = new Post();
		if (em.find(Post.class, category.getId()) != null)
		{
			categoryToArchive = em.find(Post.class, category.getId());
		
		categoryToArchive.setState(0);
		
		return true;
		}
		else
			return false;
		
	}

	@Override
	public Category searchCategory(int id) {
		Category category = em.find(Category.class, id);
		return category;
		
	}

}
