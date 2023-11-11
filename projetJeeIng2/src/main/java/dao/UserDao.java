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
		try {
			switch (user.getTypeUser()) {
				case "Administrator" :
					AdministratorDao adminDao = new AdministratorDao(sessionFactory);
					Administrator admin = new Administrator(user.getEmail(), user.getPassword(), user.getUsername());
					if (adminDao.checkAdmin(admin)) {
						save = (Integer) session.save(admin);
						tx.commit();
					}
					break;
				case "Customer" :
					CustomerDao customerDao = new CustomerDao(sessionFactory);
					Customer customer = new Customer(user.getEmail(), user.getPassword(), user.getUsername());
					if (customerDao.checkCustomer(customer)) {
						save = (Integer) session.save(customer);
						tx.commit();
					}
					break;
				case "Moderator" :
					ModeratorDao modoDao = new ModeratorDao(sessionFactory);
					Moderator moderator = new Moderator(user.getEmail(), user.getPassword(), user.getUsername());
					if (modoDao.checkModerator(moderator)) {
						save = (Integer) session.save(moderator);
						tx.commit();
					}
					break;
			}
		} catch (Exception e) {
	        return false;
		} finally {
			session.close();
		}
		
		return (save > 0);
	}
	

	public boolean getUserLogin(String email, String password) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM User WHERE email='"+email+"' AND password ='"+password+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(User.class);
		List<User> userList = query.list();
		
		tx.commit();
		session.close();
		
		return (!userList.isEmpty());
	}
}
	