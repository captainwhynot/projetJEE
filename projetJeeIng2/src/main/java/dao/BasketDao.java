package dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Basket;
import entity.User;

@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
/**
 * Data Access Object (DAO) for managing Basket entities in the database.
 * This class provides methods to interact with Basket entities, including order processing and retrieval.
 */
public class BasketDao {
	
	/**
     * The Hibernate SessionFactory for database interactions.
     */
	public SessionFactory sessionFactory;

    /**
     * Constructs a BasketDao instance with the specified Hibernate SessionFactory.
     *
     * @param sf The Hibernate SessionFactory.
     */
	public BasketDao(SessionFactory sf) {
		sessionFactory = sf;
	}

    /**
     * Adds an order to the basket or updates the quantity if the product is already in the basket.
     *
     * @param basket     The Basket entity to add or update.
     * @param customerId The ID of the customer placing the order.
     * @param quantity   The quantity of the product to be added or updated.
     * @return True if the order is successfully added or updated; false otherwise.
     */
	public boolean addOrder(Basket basket, int customerId, int quantity) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		try {
			String sql = "SELECT * FROM Basket WHERE productId = " + basket.getProduct().getId() + " AND purchaseDate IS NULL AND customerId = "+ customerId +";";
			SQLQuery query = session.createSQLQuery(sql).addEntity(Basket.class);
			Basket oldBasket = (Basket) query.getSingleResult();
			//If the product is already in the basket, add quantity if there is enough stock
			return checkStock(oldBasket.getId(), oldBasket.getQuantity()+quantity) && updateQuantity(oldBasket.getId(), quantity);
		} catch (NoResultException e) {
	        // Handle the case where no result is found (basketId is null)
	        int save = (Integer) session.save(basket);
	        tx.commit();
	        tx = session.beginTransaction();
        	//If the product is out of stock, add it with quantity null
	        if (!checkStock(basket.getId(), quantity)) {
	        	updateQuantity(basket.getId(), -basket.getQuantity());
	        }
	        tx.commit();
	        return (save > 0);
	    }catch (Exception e) {
	        return false;
		} finally {
			session.close();
		}
	}

    /**
     * Updates the quantity of a product in the basket.
     *
     * @param id       The ID of the Basket entity.
     * @param quantity The new quantity to set.
     * @return True if the quantity is successfully updated; false otherwise.
     */
	public boolean updateQuantity(int id, int quantity) {
		//Add product only if there is stock left.
		if (checkStock(id, quantity)) {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			
			String sql = "UPDATE Basket SET quantity=quantity" + (quantity>=0?"+":"") + quantity +" WHERE id="+id+";";
			SQLQuery query = session.createSQLQuery(sql);
			int rowCount = query.executeUpdate();
			
			tx.commit();
			session.close();
			
			return (rowCount > 0);
		}
		else {
			return false;
		}
	}

    /**
     * Retrieves a list of Basket entities for a specific customer with open orders.
     *
     * @param customerId The ID of the customer.
     * @return List of Basket entities representing open orders for the customer.
     */
	public List<Basket> getBasketList(int customerId) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM Basket WHERE customerId = "+ customerId +" AND purchaseDate IS NULL;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Basket.class);
		List<Basket> basketList = query.list();
		
		tx.commit();
		session.close();
		
		return basketList;
	}

    /**
     * Retrieves a Basket entity by its ID.
     *
     * @param id The ID of the Basket entity.
     * @return The Basket entity, or null if not found.
     */
	public Basket getBasket(int id) {
		Session session = sessionFactory.openSession();
		Basket basket = session.get(Basket.class, id);
		session.close();
		
		return basket;
	}

    /**
     * Confirms and retrieves a list of open orders for a specific customer, checking stock availability.
     *
     * @param customerId The ID of the customer.
     * @return List of Basket entities representing confirmed orders.
     */
	public List<Basket> confirmOrder(int customerId) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		//Check the stock for the final order.
		String sql = "SELECT * FROM Basket WHERE customerId = "+customerId+" AND purchaseDate IS NULL;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Basket.class);;
		List<Basket> basketList = query.list();
		
		for (Basket basket : basketList) {
			if (!checkStock(basket.getId(), basket.getQuantity())) {
				String sqlRemove = "UPDATE Basket SET quantity = 0 WHERE id="+basket.getId()+";";
				SQLQuery queryRemove = session.createSQLQuery(sqlRemove);
				int rowCount = queryRemove.executeUpdate();
				
				//if there is an error when updating the out of stock order, return null.
				if (rowCount <= 0) return null;
			}
		}
		
		sql = "SELECT * FROM Basket WHERE customerId = "+customerId+" AND purchaseDate IS NULL AND quantity > 0;";
		query = session.createSQLQuery(sql).addEntity(Basket.class);;
		basketList = query.list();
		
		tx.commit();
		session.close();
		
		return basketList;
	}
	
	public boolean finalizePaiement(int customerId, int cardNumber, double price, String mailContainer) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

	    String sqlFidelityPoint = "SELECT fidelityPoint FROM Customer WHERE id = " + customerId + ";";
	    SQLQuery queryFidelityPoint = session.createSQLQuery(sqlFidelityPoint);
	    int fidelityPoint = (int) queryFidelityPoint.uniqueResult();
	    //Use all the fidelity points available
	    double fidelityPointToUse = Math.min(fidelityPoint, price);

	    String sqlUpdateFidelityPoint = "UPDATE Customer SET fidelityPoint = fidelityPoint - " + fidelityPointToUse + " + " + price/10 + " WHERE id = " + customerId + ";";
	    SQLQuery queryUpdateFidelityPoint = session.createSQLQuery(sqlUpdateFidelityPoint);
	    int numberFidelityPoint = queryUpdateFidelityPoint.executeUpdate();

		//Remove the price from credit card's credit
		String sqlCardSolde = "UPDATE CreditCard SET credit = credit - " + (price - fidelityPointToUse) + " WHERE cardNumber = " + cardNumber + ";";
		SQLQuery queryCardSolde = session.createSQLQuery(sqlCardSolde);
		int numberRowSolde = queryCardSolde.executeUpdate();

		//Put the order in the history
		String sqlPaidBasket = "UPDATE Basket SET purchaseDate = :currentDate WHERE customerId = " + customerId + " AND quantity > 0 AND purchaseDate IS NULL;";
		SQLQuery queryPaidBasket = session.createSQLQuery(sqlPaidBasket);
		queryPaidBasket.setParameter("currentDate", new Date());
		int numberRowBasket = queryPaidBasket.executeUpdate();

		int numberRowProduct = 0;
		List<Basket> basketList = this.getBasketList(customerId);
		for (Basket basket : basketList) {
			//Update each product's stock
			String sqlUpdateProduct = "UPDATE Product SET stock = stock - " + basket.getQuantity() + " WHERE id = " + basket.getProduct().getId() + " ";
			SQLQuery queryUpdateProduct = session.createSQLQuery(sqlUpdateProduct);
			numberRowProduct = queryUpdateProduct.executeUpdate();
		}
		
		tx.commit();
		session.close();

		UserDao userDao = new UserDao(sessionFactory);
		User user = userDao.getUser(customerId);
		userDao.sendMail(user.getEmail(), "MANGASTORE : Paiement recapitulation", mailContainer);
		
		return (numberRowSolde > 0 && numberRowBasket > 0 && numberFidelityPoint > 0 && numberRowProduct > 0);
	}
	
	public boolean checkStock(int id, int quantity) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sqlStock = "SELECT stock FROM Product p JOIN Basket b ON p.id = b.productId WHERE b.id = "+id+";";
		SQLQuery queryStock = session.createSQLQuery(sqlStock);
		Integer stock = (Integer) queryStock.uniqueResult();
		
		tx.commit();
		session.close();
		
		if (stock != null) return (stock >= quantity);
		else return false;
	}
	
	public double totalPrice(int customerId) {
		double totalPrice = 0;
		Session session = sessionFactory.openSession();
		//Check the price for the final order.
		String sql = "SELECT * FROM Basket WHERE customerId = "+customerId+" AND quantity > 0 AND purchaseDate IS NULL;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Basket.class);
		List<Basket> basketList = query.list();
		
		for (Basket basket : basketList) {
			String sqlProduct = "SELECT price FROM Product WHERE id="+basket.getProduct().getId()+";";
			SQLQuery queryProduct = session.createSQLQuery(sqlProduct);
			double price = (double) queryProduct.uniqueResult();
			
			totalPrice += price * basket.getQuantity();
		}
		session.close();
		
		return totalPrice;
	}
	
	public boolean deleteOrder(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
	        Basket basket = session.get(Basket.class, id);
	        session.delete(basket);
			tx.commit();
	        return true;
	    } catch (Exception e) {
	        return false;
	    } finally {
	        session.close();
	    }
	}
}
