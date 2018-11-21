package tn.esprit.wediscus.services;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import tn.esprit.wediscus.entity.Comment;

@Local
public interface CommentServiceRemote {
	public boolean addComment(Comment comment);
	public boolean deleteComment(Comment comment);
	public boolean updateComment(Comment comment);
	public List<Comment> getAllComment();
	public List<Comment> GetCommentByUser(String username);
	public boolean AddCommentWithMail(Comment comment);
	
	

}
