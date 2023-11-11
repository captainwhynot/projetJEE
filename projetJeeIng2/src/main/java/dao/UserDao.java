package dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Administrator;
import entity.Customer;
import entity.Moderator;
import entity.User;

@SuppressWarnings({ "rawtypes", "deprecation", "unchecked"})
public class UserDao {
	public SessionFactory sessionFactory;
	
	public UserDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean saveUser(User user) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		int save = 0;
		if (checkUserMail(user)) {
			try {
				switch (user.getTypeUser()) {
					case "Administrator" :
						Administrator admin = new Administrator(user.getEmail(), user.getPassword(), user.getUsername());
						save = (Integer) session.save(admin);
						tx.commit();
						break;
					case "Customer" :
						Customer customer = new Customer(user.getEmail(), user.getPassword(), user.getUsername());
						save = (Integer) session.save(customer);
						tx.commit();
						break;
					case "Moderator" :
						Moderator moderator = new Moderator(user.getEmail(), user.getPassword(), user.getUsername());
						save = (Integer) session.save(moderator);
						tx.commit();
						break;
				}
			} catch (Exception e) {
		        return false;
			} finally {
				session.close();
			}
		}
		
		return (save > 0);
	}
	
	public boolean checkUserMail(User user) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		try {
			String sql = "SELECT * FROM User WHERE email = '"+ user.getEmail() +"';";
			SQLQuery query = session.createSQLQuery(sql).addEntity(User.class);		
			List<User> userList = query.list();
	
		    tx.commit();
		    return (userList.isEmpty());
		} catch (Exception e) {
	        return false;
		} finally {
			session.close();
		}
	}
	
	public boolean checkUserLogin(String email, String password) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM User WHERE email='"+email+"' AND password ='"+password+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(User.class);
		List<User> userList = (List<User>) query.list();
		
		tx.commit();
		session.close();
		
		return (!userList.isEmpty());
	}
	
	public User getUser(String email) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM User WHERE email='"+email+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(User.class);
		User user = (User) query.uniqueResult();
		
		tx.commit();
		session.close();
		
		return user;
	}
}
	