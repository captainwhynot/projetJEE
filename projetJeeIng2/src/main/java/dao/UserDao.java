package dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.User;
import entity.User2;

public class UserDao {
	
	public SessionFactory sessionFactory;
	
	public UserDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean saveUser1 (User user) {
		boolean b = false;		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int i=(Integer)session.save(user);
		if(i>0) {b=true;}
		
		tx.commit();
		session.close();
		return b;
	}
	
	public boolean saveUser2 (User2 user) {
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
		
		SQLQuery query = session.createSQLQuery(sql).addEntity(User.class);
		List<User> liste = query.list();
		for(User u:liste) {
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
		
		SQLQuery query = session.createSQLQuery(sql).addEntity(User.class);
		List<User> liste = query.list();
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
		
		SQLQuery query = session.createSQLQuery(sql).addEntity(User2.class);
		List<User> liste = query.list();
		if (liste.size()==1) {b = true;}
		
		tx.commit();
		session.close();
		
		return b;
	}
}
