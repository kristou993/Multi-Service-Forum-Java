package tn.esprit.wediscus.services;

import java.util.Collection;
import java.util.List;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import tn.esprit.wediscus.entity.Article;
import tn.esprit.wediscus.entity.Comment;
import tn.esprit.wediscus.entity.User;

@Stateless
public class CommentService implements CommentServiceRemote {
	
	@PersistenceContext(name="wediscus-ejb")
	EntityManager em;


	@Override
	public boolean addComment(Comment comment) {
		em.persist(comment);
		return true;
	}

	@Override
	public boolean deleteComment(Comment comment) {
	
		em.remove(em.find(Comment.class, comment.getId()));
	  return true;

	}

	@Override
	public boolean updateComment(Comment comment) {
		Comment CommentToupdate = new Comment();
		if (em.find(Comment.class, comment.getId()) != null)
		{
			CommentToupdate = em.find(Comment.class, comment.getId());
		
			CommentToupdate.setDescription(comment.getDescription());
		return true;
		}
		else
			return false;
		
	}

	@Override
	public List<Comment> getAllComment() {
		Query query = em.createQuery("SELECT C FROM Comment C");
		Collection<Comment> listFromDB = (Collection<Comment>) query.getResultList();
		if (listFromDB.size()==0)
		return null;
		else
			return (List<Comment>) listFromDB;
		
	}

	@Override
	public List<Comment> GetCommentByUser(String username) {
		Query query = em.createQuery("SELECT C FROM Comment C where C.user.username=:username");
		query.setParameter("username", username);
		Collection<Comment> listFromDB = (Collection<Comment>) query.getResultList();
		if (listFromDB.size()==0)
		return null;
		else
			return (List<Comment>) listFromDB;
		

	}

	@Override
	public boolean AddCommentWithMail(Comment comment) {
		
		String mail = comment.getUser().getEmail();
		final String username = "hostandguest4@gmail.com";
		final String password = "esprit2250";
                 boolean vr=true;
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props,new javax.mail.Authenticator() {

		
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
			message.setSubject("Notification");
			message.setText("Un nouveau commentaire a été ajouté a votre article  ");
 
			Transport.send(message);
 
			System.out.println("message sent successfully");
		}
		catch (MessagingException e) {
			System.out.println("Erreur lors de l'envoi");
			vr=false;
		}
		if (vr)
		{
			em.persist(comment);
		return vr;
		}
		
		return vr;
		
		
	}

}
