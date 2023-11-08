package dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Administrator;

public class AdministratorDao {
	public SessionFactory sessionFactory;
	
	public AdministratorDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean saveAdmin (Administrator admin) {
		boolean b = false;		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		int i=(Integer)session.save(admin);
		if (i>0) b=true;
		
		tx.commit();
		session.close();
		
		return b;
	}
}
