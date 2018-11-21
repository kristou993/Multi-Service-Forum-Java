package tn.esprit.wediscus.services;

import java.util.Collection;
import java.util.Date;

import tn.esprit.wediscus.entity.Post;

public interface PostServiceRemote {
	public boolean addPost(Post post);
	public boolean editPost(Post post);
	public Collection<Post> showPosts();
	public boolean deletePost(Post post);
	public boolean archivePost(Post post);
	public Post searchPost(int id);
	public Collection<Post> searchPostByContent(String content);
	public Collection<Post> searchPostByDate(Date date);
	public Collection<Post> searchPostByThread(int id);
	public Collection<Post> searchPostByRating(int rating);
	public Collection<Post> searchPostByWarningLevel(int warning);
	public Collection<Post> showClientPosts();
	public Collection<Post> checkWarnings();

}
