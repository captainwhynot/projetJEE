package dao;

import java.io.File;
import java.util.List;

import javax.servlet.http.Part;

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
		
		if (stock < 0) return false;
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
	
	public boolean updateProductImg(Product product, Part filePart, String fileName, String savePath) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			int productId = product.getId();

	        File imgDir = new File(savePath);
	        File[] files = imgDir.listFiles((dir, name) -> name.startsWith(productId + "_"));
	        
	        //Delete all the old profile picture of the user
	        if (files != null) {
	            for (File file : files) {
	                file.delete();
	            }
	        }
			//Create product folder if it does not exist
			File saveDir = new File(savePath);
	        if (!saveDir.exists()) {
	            saveDir.mkdirs();
	        }

	        //Save the image in the folder
	        fileName = product.getId() + "_"+ fileName;
			String filePath = savePath + File.separator + fileName;
			filePart.write(filePath);
	        
			String sql = "UPDATE Product SET img='img/Product/"+fileName+"' WHERE id="+product.getId()+";";
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
		
		String sql = "SELECT * FROM Product;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Product.class);
		List<Product> productList = query.list();
		
		session.close();
		
		return productList;
	}
	
	public Product getProduct(int id) {
		Session session = sessionFactory.openSession();
		Product product = session.get(Product.class, id);
		session.close();
		
		return product;
	}
	
	public boolean deleteProduct(int id, String savePath) {
	    Session session = sessionFactory.openSession();
	    Transaction tx = session.beginTransaction();

	    try {
	        File imgDir = new File(savePath);
	        File[] files = imgDir.listFiles((dir, name) -> name.startsWith(id + "_"));
	        
	        //Delete all the old image of the product
	        if (files != null) {
	            for (File file : files) {
	                file.delete();
	            }
	        }
	        Product product = session.get(Product.class, id);
	        if (product != null) {
	            List<Basket> baskets = product.getBaskets();
	            //Delete the product and the product's reference in all basket using it.
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
