package dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Customer;
import entity.Moderator;
import entity.Product;
import entity.User;

@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
public class ModeratorDao {
	public SessionFactory sessionFactory;
	
	public ModeratorDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean checkModerator(Moderator moderator) {
		Session session = sessionFactory.openSession();		
		try {
			String sql = "SELECT * FROM Moderator m JOIN User u ON m.id = u.id WHERE u.email = '"+ moderator.getEmail() +"';";
			SQLQuery query = session.createSQLQuery(sql).addEntity(Moderator.class);		
			List<Moderator> moderatorList = query.list();
	
		    return (moderatorList.isEmpty());
		} catch (Exception e) {
	        return false;
		} finally {
			session.close();
		}
	}

	public List<Moderator> getModeratorList(){
		Session session = sessionFactory.openSession();
		
		String sql = "SELECT * FROM Moderator m JOIN User u ON m.id = u.id;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Moderator.class);
		List<Moderator> ModeratorList = query.list();
		
		session.close();
		
		return ModeratorList;
	}
	
	public Moderator getModerator(String email) {
		Session session = sessionFactory.openSession();
		
		String sql = "SELECT * FROM Moderator m JOIN User u ON m.id = u.id WHERE u.email = '" + email + "';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Moderator.class);
		Moderator moderator = (Moderator) query.uniqueResult();
		
		session.close();
		
		return moderator;
	}
	
	public Moderator getModerator(int id) {
		Session session = sessionFactory.openSession();
		
		String sql = "SELECT * FROM Moderator m JOIN User u ON m.id = u.id WHERE u.id = '" + id + "';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Moderator.class);
		Moderator moderator = (Moderator) query.uniqueResult();
		
		session.close();
		
		return moderator;
	}
	
	public boolean transferIntoCustomer(Moderator moderator) {
		UserDao userDao = new UserDao(sessionFactory);
		Customer customer = new Customer(moderator.getEmail(), moderator.getPassword(), moderator.getUsername());
		customer.setId(moderator.getId());
	    boolean delete = deleteModerator(moderator);
	    boolean save = userDao.saveUser(customer);
		return (delete && save);
	}
	
	public boolean deleteModerator(Moderator moderator) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		try {
	        User user = moderator.getUser();
	        moderator.setUser(null); 
	        
	        List<Product> products = user.getProducts();

	        session.delete(moderator);
	        for (Product product : products) {
	            session.delete(product);
	        }
	        if (user != null) {
	            session.delete(user);
	        }
	        
	        tx.commit();
	        return true;
	    } catch (Exception e) {
	        return false;
	    } finally {
	        session.close();
	    }
	}
	
	public boolean modifyRight(Moderator moderator, String right, boolean bool) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		try {
			String sql = "UPDATE Moderator SET "+right+"="+(bool?"1":"0")+" WHERE id="+moderator.getId()+";";
			SQLQuery query = session.createSQLQuery(sql);
			int rowCount = query.executeUpdate();
			
			tx.commit();
			return (rowCount > 0);
		} catch (Exception e) {
	        return false;
	    } finally {
	        session.close();
	    }

	}
}
