package dao;

import org.hibernate.SessionFactory;

public class AdministratorDao {
	public SessionFactory sessionFactory;
	
	public AdministratorDao(SessionFactory sf) {
		sessionFactory = sf;
	}
}