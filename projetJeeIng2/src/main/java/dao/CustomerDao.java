package dao;

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
	
	public boolean saveCustomer (Customer user) {
		boolean b = false;		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int i=(Integer)session.save(user);
		if(i>0) {b=true;}
		
		tx.commit();
		session.close();
		return b;
	}
	
	public boolean saveUser2 (Administrator user) {
		boolean b = false;		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int i=(Integer)session.save(user);
		if(i>0) {b=true;}
		tx.commit();
		session.close();
		return b;
	}
	
	
	public void getAllUser(){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String sql = "SELECT * FROM User;";
		
		SQLQuery query = session.createSQLQuery(sql).addEntity(Customer.class);
		List<Customer> liste = query.list();
		for(Customer u:liste) {
			System.out.println("test : "+u);
		}
		tx.commit();
		session.close();
	}
	
	public boolean getUser(String nom, String prenom) {
		boolean b = false;
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String sql = "SELECT * FROM User WHERE nom='"+nom+"' AND prenom='"+prenom+"';";
		
		SQLQuery query = session.createSQLQuery(sql).addEntity(Customer.class);
		List<Customer> liste = query.list();
		if (liste.size()==1) {b = true;}
		
		tx.commit();
		session.close();
		
		return b;
	}
	
	public boolean getUser2(String nom, String prenom) {
		boolean b = false;
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String sql = "SELECT * FROM User2 WHERE nom='"+nom+"' AND prenom='"+prenom+"';";
		
		SQLQuery query = session.createSQLQuery(sql).addEntity(Administrator.class);
		List<Customer> liste = query.list();
		if (liste.size()==1) {b = true;}
		
		tx.commit();
		session.close();
		
		return b;
	}
}
