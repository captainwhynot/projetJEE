package dao;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Administrator;

@SuppressWarnings({"deprecation", "rawtypes"})
public class AdministratorDao {
	public SessionFactory sessionFactory;
	
	public AdministratorDao(SessionFactory sf) {
		sessionFactory = sf;
	}

	public Administrator getAdministrator(String email) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM Administrator a JOIN User u ON a.id = u.id WHERE u.email='"+email+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Administrator.class);
		Administrator admin = (Administrator) query.uniqueResult();
		
		tx.commit();
		session.close();
		
		return admin;
	}
}