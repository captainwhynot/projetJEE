package dao;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Product;

public class ProductDao {
	public SessionFactory sessionFactory;
	
	public ProductDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean addProduct (Product product) {
		boolean b = false;		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		int i=(Integer)session.save(product);
		if (i>0) b=true;
		
		tx.commit();
		session.close();
		
		return b;
	}
	
	public boolean modifyProduct (Product product, String name, double price, int stock) {
		boolean b = false;		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "UPDATE Product SET name='"+name+"', price="+price+", stock="+stock+" WHERE id="+product.getId()+";";
		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		
		tx.commit();
		session.close();

		return (rowCount > 0);	
	}
	
	public boolean deleteProduct (Product product) {
		boolean b = false;		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "DELETE FROM Product WHERE id="+product.getId()+";";
		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		
		tx.commit();
		session.close();

		return (rowCount > 0);
	}
}
