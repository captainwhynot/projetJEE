package dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Customer;

@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
public class CustomerDao {
	
	public SessionFactory sessionFactory;
	
	public CustomerDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean saveCustomer(Customer customer) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		int save = 0;
		if (checkCustomer(customer)){
			save = (Integer) session.save(customer);
		}
		else {
			System.out.println("The customer already exists in the database.");
		}
		
		tx.commit();
		session.close();
		
		return (save > 0);
	}
	
	public boolean checkCustomer(Customer customer) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM Customer WHERE email = '"+ customer.getEmail() +"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Customer.class);		
		Customer result = (Customer) query.uniqueResult();

	    tx.commit();
	    session.close();

	    return (result == null);
	}
	
	public List<Customer> getCustomerList(){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM Customer;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Customer.class);
		List<Customer> customerList = query.list();
		
		tx.commit();
		session.close();
		
		return customerList;
	}
	
	public Customer getCustomer(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM Customer WHERE id='"+id+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Customer.class);
		Customer customer = (Customer) query.uniqueResult();
		
		tx.commit();
		session.close();
		
		return customer;
	}
	
	public boolean getCustomerConnexion(String email, String password) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM Customer WHERE email='"+email+"' AND password ='"+password+"';";
		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		
		tx.commit();
		session.close();
		
		return (rowCount > 0);
	}
	
	public boolean setFidelityPoint(Customer customer, int points) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "UPDATE Customer SET fidelityPoint = fidelityPoint"+ (points>0 ? "+":"") + points + " WHERE id="+customer.getId()+";";
		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		
		tx.commit();
		session.close();

		return (rowCount > 0);
	}

	public boolean deleteCustomer(Customer customer) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "DELETE FROM Customer WHERE id="+customer.getId()+";";
		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		
		tx.commit();
		session.close();

		return (rowCount > 0);
	}
}
