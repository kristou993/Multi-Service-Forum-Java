package tn.esprit.wediscus.services;

import java.util.Collection;

import javax.ejb.Local;

import tn.esprit.wediscus.entity.Thread;

@Local
public interface ThreadServiceRemote {

	public boolean addThread(Thread thread);
	public boolean editThread(Thread thread);
	public Collection<Thread> showThreads();
	public boolean deleteThread(Thread thread);
	public boolean archiveThread(Thread thread);
	public Thread searchThread(int id);
}
