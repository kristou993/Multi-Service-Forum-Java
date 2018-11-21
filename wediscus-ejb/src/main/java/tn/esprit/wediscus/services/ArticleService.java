package tn.esprit.wediscus.services;

import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.esprit.wediscus.entity.Article;
import tn.esprit.wediscus.entity.User;

@Stateless
public class ArticleService implements ArticleServiceRemote {
	@PersistenceContext(name="wediscus-ejb")
	EntityManager em;
	
	
	@Override
	public boolean addArticle(Article article) {
	 
			em.persist(article);
		return true;
	
	}

	@Override
	public boolean deleteArticle(Article article) {
		em.remove(em.find(Article.class, article.getId()));
     return true;
    
   
	}

	@Override
	public Collection<Article> findArticlebytitle(String title) {
		Query query = em.createQuery("SELECT A FROM Article A WHERE A.title= :title");
		query.setParameter("title", title);
		Collection<Article> listFromDB = (Collection<Article>) query.getResultList();
		if (listFromDB.size()==0)
		return null;
		else
			return listFromDB;
		
	}

	@Override
	public Collection<Article> findAllArticle() {
		Query query = em.createQuery("SELECT A FROM Article A");
		Collection<Article> listFromDB = (Collection<Article>) query.getResultList();
		if (listFromDB.size()==0)
		return null;
		else
			return listFromDB;
	}

	@Override
	public boolean updateArticle(Article article) {
		Article articleToupdate = new Article();
		if (em.find(Article.class, article.getId()) != null)
		{
		articleToupdate = em.find(Article.class, article.getId());
		
		articleToupdate.setDescription(article.getDescription());
		articleToupdate.setTitle(article.getTitle());
		articleToupdate.setDatePublication(article.getDatePublication());
		articleToupdate.setTypearticle(article.getTypearticle());
		articleToupdate.setUser(article.getUser());
		return true;
		}
		else
			return false;
		
		
	}

	@Override
	public Collection<Article> findArticlebyType(String typeArticle) {
		Query query = em.createQuery("SELECT A FROM Article A WHERE A.typearticle= :typeArticle");
		query.setParameter("typeArticle", typeArticle);
		Collection<Article> listFromDB = (Collection<Article>) query.getResultList();
		if (listFromDB.size()==0)
		return null;
		else
			return listFromDB;
		
	}

	@Override
	public Collection<Article> findArticlebyDate(int year,int mois,int jour) {
	
		Query query = em.createQuery("SELECT A FROM Article A WHERE "
				+ "YEAR(A.datePublication)= :year and MONTH(A.datePublication)=:month and "
				+ "DAY(A.datePublication)=:day");
		query.setParameter("day", jour);
		query.setParameter("month", mois);
		query.setParameter("year", year);
		Collection<Article> listFromDB = (Collection<Article>) query.getResultList();
		if (listFromDB.size()==0)
			return null;
		else
			return listFromDB;
	}

	@Override
	public Collection<Article> findArticlebyUser(String username) {
		Query query = em.createQuery("SELECT A FROM Article A WHERE A.user.username= :username");
		query.setParameter("username", username);
		Collection<Article> listFromDB = (Collection<Article>) query.getResultList();
		if (listFromDB.size()==0)
		return null;
		else
			return listFromDB;
		
	}

	@Override
	public void sendMail(String mail) {
		  final String username = "hostandguest4@gmail.com";
			final String password = "esprit2250";
	                 boolean vr=true;
	 
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
			
			Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
	                                @Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username,password);
					}
				});
	 
			try {
	 
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("hostandguest4@gmail.com"));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(mail));
				message.setSubject("Changement mot de passe");
				message.setText("Un nouveau commentaire a été ajouté a votre article  ");
	 
				Transport.send(message);
	 
				System.out.println("message sent successfully");
			}
			catch (MessagingException e) {
				System.out.println("Erreur lors de l'envoi");
			}
	}

		
	

}
