package dao;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Administrator;

@SuppressWarnings({ "rawtypes", "deprecation" })
public class AdministratorDao {
	public SessionFactory sessionFactory;
	
	public AdministratorDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean saveAdmin(Administrator admin) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		int save = 0;
		if (checkAdmin(admin)){
			save = (Integer) session.save(admin);
		}
		else {
			System.out.println("The administrator already exists in the database.");
		}
		tx.commit();
		session.close();
		
		return (save > 0);
	}
	
	public boolean checkAdmin(Administrator admin) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM Administrator WHERE email = '"+ admin.getEmail() +"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Administrator.class);		
		Administrator result = (Administrator) query.uniqueResult();

	    tx.commit();
	    session.close();

	    return (result == null);
	}
}
