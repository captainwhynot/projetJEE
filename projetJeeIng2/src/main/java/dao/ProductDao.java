package dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Basket;
import entity.Product;

@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
public class ProductDao {
	public SessionFactory sessionFactory;
	
	public ProductDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean addProduct(Product product) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		int save = (Integer) session.save(product);
		
		tx.commit();
		session.close();
		
		return (save > 0);
	}
	
	public boolean modifyProduct(Product product, String name, double price, int stock) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		try {
			String sql = "UPDATE Product SET name='"+name+"', price="+price+", stock="+stock+" WHERE id="+product.getId()+";";
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
		
		Product product = session.get(Product.class, id);
		
		session.close();
		
		return product;
	}
	
	public boolean deleteProduct(int id) {
	    Session session = sessionFactory.openSession();
	    Transaction tx = session.beginTransaction();

	    try {
	        Product product = session.get(Product.class, id);
	        if (product != null) {
	            List<Basket> baskets = product.getBaskets();

		        session.delete(product);
		        for (Basket basket : baskets) {
		            session.delete(basket);
		        }
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
