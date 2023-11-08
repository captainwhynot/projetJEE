package dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Basket;
import entity.Customer;
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
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "UPDATE Product SET name='"+name+"', price="+price+", stock="+stock+" WHERE id="+product.getId()+";";
		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		
		tx.commit();
		session.close();

		return (rowCount > 0);	
	}
	
	public List<Product> getProductList() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM Product;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Product.class);
		List<Product> productList = query.list();
		
		tx.commit();
		session.close();
		return productList;
	}
	
	public Product getProduct(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM Product WHERE id='"+id+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Product.class);
		Product product = (Product) query.uniqueResult();
		
		tx.commit();
		session.close();
		
		return product;
	}
	
	public boolean deleteProduct (int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "DELETE FROM Product WHERE id="+id+";";
		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		
		tx.commit();
		session.close();

		return (rowCount > 0);
	}
}
