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
/**
 * Data Access Object (DAO) for managing Product entities in the database.
 * This class provides methods to interact with Product entities, including addition,
 * modification, retrieval, and deletion operations.
 */
public class ProductDao {

    /**
     * The Hibernate SessionFactory for database interactions.
     */
	public SessionFactory sessionFactory;

    /**
     * Constructs a ProductDao instance with the specified Hibernate SessionFactory.
     *
     * @param sf The Hibernate SessionFactory.
     */
	public ProductDao(SessionFactory sf) {
		sessionFactory = sf;
	}

    /**
     * Adds a new Product to the database.
     *
     * @param product The Product entity to add.
     * @return True if the addition is successful; false otherwise.
     */
	public boolean addProduct(Product product) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		int save = (Integer) session.save(product);
		
		tx.commit();
		session.close();
		
		return (save > 0);
	}

    /**
     * Modifies an existing Product in the database.
     *
     * @param product The Product entity to modify.
     * @param name    The new name for the product.
     * @param price   The new price for the product.
     * @param stock   The new stock quantity for the product.
     * @return True if the modification is successful; false otherwise.
     */
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

    /**
     * Updates the image of a Product in the database.
     *
     * @param product   The Product entity to update the image for.
     * @param filePart  The Part representing the new image file.
     * @param fileName  The name of the new image file.
     * @param savePath  The path where the images are saved.
     * @return True if the update is successful; false otherwise.
     */
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

    /**
     * Retrieves a list of all Product entities.
     *
     * @return List of Product entities.
     */
	public List<Product> getProductList() {
		Session session = sessionFactory.openSession();
		
		String sql = "SELECT * FROM Product;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Product.class);
		List<Product> productList = query.list();
		
		session.close();
		
		return productList;
	}

    /**
     * Retrieves a Product entity by ID.
     *
     * @param id The ID of the Product.
     * @return The Product entity, or null if not found.
     */
	public Product getProduct(int id) {
		Session session = sessionFactory.openSession();
		Product product = session.get(Product.class, id);
		session.close();
		
		return product;
	}

    /**
     * Deletes a Product entity and its associated image files from the database.
     *
     * @param id       The ID of the Product to delete.
     * @param savePath The path where the images are saved.
     * @return True if the deletion is successful; false otherwise.
     */
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
