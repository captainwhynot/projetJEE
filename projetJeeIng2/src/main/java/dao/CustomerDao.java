package dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Basket;
import entity.Customer;
import entity.Moderator;
import entity.User;

@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
public class CustomerDao {
	
	public SessionFactory sessionFactory;
	
	public CustomerDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean checkCustomer(Customer customer) {
		Session session = sessionFactory.openSession();
		try {
			String sql = "SELECT * FROM Customer c JOIN User u ON c.id = u.id WHERE u.email = '"+ customer.getEmail() +"';";
			SQLQuery query = session.createSQLQuery(sql).addEntity(Customer.class);		
			List<Customer> customerList = query.list();
			
		    return (customerList.isEmpty());
	    } catch (Exception e) {
	        return false;
	    } finally {
	        session.close();
	    }
	}
	
	public List<Customer> getCustomerList(){
		Session session = sessionFactory.openSession();
		
		String sql = "SELECT * FROM Customer c JOIN User u ON c.id = u.id;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Customer.class);
		List<Customer> customerList = query.list();
		
		session.close();
		
		return customerList;
	}
	
	public Customer getCustomer(String email) {
		Session session = sessionFactory.openSession();
		
		String sql = "SELECT * FROM Customer c JOIN User u ON c.id = u.id WHERE u.email='"+email+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Customer.class);
		Customer customer = (Customer) query.uniqueResult();
		
		session.close();
		
		return customer;
	}
	
	public Customer getCustomer(int id) {
		Session session = sessionFactory.openSession();
		
		String sql = "SELECT * FROM Customer c JOIN User u ON c.id = u.id WHERE u.id='"+id+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Customer.class);
		Customer customer = (Customer) query.uniqueResult();
		
		session.close();
		
		return customer;
	}
	
	public boolean setFidelityPoint(Customer customer, int points) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		try {
			String sql = "UPDATE Customer SET fidelityPoint = fidelityPoint"+ (points>=0 ? "+":"") + points + " WHERE id="+customer.getId()+";";
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

	public boolean transferIntoModerator(Customer customer) {
		UserDao userDao = new UserDao(sessionFactory);
		Moderator moderator = new Moderator(customer.getEmail(), customer.getPassword(), customer.getUsername());
	    boolean delete = deleteCustomer(customer);
	    boolean save = userDao.saveUser(moderator);
		return (delete && save);
	}
	
	public boolean deleteCustomer(Customer customer) {
	    Session session = sessionFactory.openSession();
	    Transaction tx = session.beginTransaction();

	    try {
	        User user = customer.getUser();
	        customer.setUser(null);
	        List<Basket> baskets = customer.getBaskets();
	        //Delete the customer & the user & the customer's basket
	        session.delete(customer);
	        if (user != null) {
	            session.delete(user);
	        }
	        for (Basket basket : baskets) {
	            session.delete(basket);
	        }

	        tx.commit();
	        return true;
	    } catch (Exception e) {
	        return false;
	    } finally {
	        session.close();
	    }
	}

}
