package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Customer;
import entity.Administrator;

public class CustomerDao {
	
	public SessionFactory sessionFactory;
	
	public CustomerDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean saveCustomer (Customer customer) {
		boolean b = false;		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int i=(Integer)session.save(customer);
		if(i>0) {b=true;}
		
		tx.commit();
		session.close();
		return b;
	}
	
	
	
	//a mettre dans admin
	public List<Customer> getAllCustomer(){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String sql = "SELECT * FROM Customer;";
		
		SQLQuery query = session.createSQLQuery(sql).addEntity(Customer.class);
		List<Customer> liste = query.list();
		for(Customer u:liste) {
			System.out.println("test : "+u);
		}
		tx.commit();
		session.close();
		
		return liste;
	}
	
	public Customer getCustomerById(int id) {
		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String sql = "SELECT * FROM Customer WHERE id='"+id+"';";
		
		SQLQuery query = session.createSQLQuery(sql).addEntity(Customer.class);
		Customer customer = (Customer) query.uniqueResult();
		
		
		tx.commit();
		session.close();
		
		return customer;
	}
	
	public boolean getCustomerConnexion(String username, String password) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String sql = "SELECT * FROM Customer WHERE username='"+username+"' AND password ='"+password+"';";
		SQLQuery query = session.createSQLQuery(sql);
		//int rowCount = query.();
		tx.commit();
		session.close();

		if (query == null) {
		    return false;
		} else {
		    return true;
		}
	}
	
	public boolean setFidelityPoint (Customer customer, int points) {
				
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String sql = "UPDATE Customer SET fidelityPoint = fidelityPoint"+ (points>0 ? "+":"") + points + " WHERE id="+customer.getId()+";";

		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		tx.commit();
		session.close();

		if (rowCount > 0) {
		    return true;
		} else {
		    return false;
		}
		
	}
	
	
	
	
	
}
