package dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Administrator;

@SuppressWarnings({ "rawtypes", "deprecation", "unchecked"})
public class AdministratorDao {
	public SessionFactory sessionFactory;
	
	public AdministratorDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean saveAdmin(Administrator admin) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		try {
			if (checkAdmin(admin)) {
				session.save(admin);
				tx.commit();
				return true;
			}
			else {
		        return false;
			}
		} catch (Exception e) {
	        return false;
		} finally {
			session.close();
		}
	}
	
	public boolean checkAdmin(Administrator admin) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		try {
			String sql = "SELECT * FROM Administrator a JOIN User u ON a.id = u.id WHERE u.email = '"+ admin.getEmail() +"';";
			SQLQuery query = session.createSQLQuery(sql).addEntity(Administrator.class);		
			List<Administrator> adminList = query.list();
	
		    tx.commit();
		    return (adminList.isEmpty());
		} catch (Exception e) {
	        return false;
		} finally {
			session.close();
		}
	}
}