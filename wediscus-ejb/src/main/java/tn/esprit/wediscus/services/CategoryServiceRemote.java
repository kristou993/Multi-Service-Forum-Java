package tn.esprit.wediscus.services;

import java.util.Collection;

import javax.ejb.Local;
import javax.ejb.Remote;

import tn.esprit.wediscus.entity.Category;

@Local
public interface CategoryServiceRemote {
	
	public boolean addCategory(Category category);
	public boolean editCategory(Category category);
	public Collection<Category> showCategories();
	public boolean deleteCategory(Category category);
	public Category searchCategory(int id);
	public boolean archiveCategory(Category category);

}
