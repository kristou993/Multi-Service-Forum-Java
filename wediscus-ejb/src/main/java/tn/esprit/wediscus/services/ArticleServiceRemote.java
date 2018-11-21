package tn.esprit.wediscus.services;

import java.util.Collection;
import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Remote;

import tn.esprit.wediscus.entity.Article;
import tn.esprit.wediscus.entity.User;

@Local
public interface ArticleServiceRemote {
	
	public boolean addArticle(Article article);
	public boolean deleteArticle(Article article);
	public Collection<Article> findArticlebytitle(String title);
	public Collection<Article> findArticlebyType(String typeArticle);
	public Collection<Article> findArticlebyDate(int year,int mois,int jour);
	public Collection<Article> findAllArticle();
	public boolean updateArticle(Article article);
	public Collection<Article> findArticlebyUser(String username);
	public void sendMail (String mail);
	 
	

}
