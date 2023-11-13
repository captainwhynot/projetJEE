package dao;

import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Basket;
import entity.Customer;

@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
public class BasketDao {
public SessionFactory sessionFactory;
	
	public BasketDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean addOrder(Basket basket) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		try {
			int save = (Integer) session.save(basket);
			tx.commit();			
			return (save > 0);
		} catch (Exception e) {
	        return false;
		} finally {
			session.close();
		}
	}
	
	public boolean updateQuantity(int id, int quantity) {
		//Add product only if there is stock left.
		if (checkStock(id, quantity)) {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			
			String sql = "UPDATE Basket SET quantity=quantity" + (quantity>0?"+":"") + quantity +" WHERE id="+id+";";
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
	
	public List<Basket> getBasketList(int customerId) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM Basket WHERE customerId = "+ customerId +" AND bought=0;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Basket.class);
		List<Basket> basketList = query.list();
		
		tx.commit();
		session.close();
		
		return basketList;
	}
	
	public Basket getBasket(int id) {
		Session session = sessionFactory.openSession();
		Basket basket = session.get(Basket.class, id);
		session.close();
		
		return basket;
	}
	
	public List<Basket> confirmOrder(int customerId) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		//Check the stock for the final order.
		String sql = "SELECT * FROM Basket WHERE customerId = "+customerId+" AND bought = 0;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Basket.class);;
		List<Basket> basketList = query.list();
		
		for (Basket basket : basketList) {
			if (!checkStock(basket.getId(), basket.getQuantity())) {
				String sqlRemove = "UPDATE Basket SET quantity = 0 WHERE id="+basket.getId()+";";
				SQLQuery queryRemove = session.createSQLQuery(sqlRemove);
				int rowCount = queryRemove.executeUpdate();
				
				//if there is an error when updating the out of stock order, return null.
				if (rowCount <= 0) return null;
				else System.out.println("The order number n°" + basket.getId() + " has been removed due to rupture of stock.");
			}
		}
		
		sql = "SELECT * FROM Basket WHERE customerId = "+customerId+" AND bought = 0 AND quantity > 0;";
		query = session.createSQLQuery(sql).addEntity(Basket.class);;
		basketList = query.list();
		
		tx.commit();
		session.close();
		
		return basketList;
	}
	
	public boolean finalizePaiement(int customerId, int cardNumber, double price) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sqlCardSolde = "UPDATE CreditCard SET credit = credit - " + price + " WHERE cardNumber = " + cardNumber + ";";
		SQLQuery queryCardSolde = session.createSQLQuery(sqlCardSolde);
		int numberRowSolde = queryCardSolde.executeUpdate();

		String sqlPaidBasket = "UPDATE Basket SET bought = 1, purchaseDate = :currentDate WHERE customerId = " + customerId + " AND quantity > 0 AND bought=0;";
		SQLQuery queryPaidBasket = session.createSQLQuery(sqlPaidBasket);
		queryPaidBasket.setParameter("currentDate", new Date());
		int numberRowBasket = queryPaidBasket.executeUpdate();

		int numberRowProduct = 0;
		List<Basket> basketList = this.getBasketList(customerId);
		for (Basket basket : basketList) {
			String sqlUpdateProduct = "UPDATE Product SET stock = stock - " + basket.getQuantity() + " WHERE id = " + basket.getProduct().getId() + " ";
			SQLQuery queryUpdateProduct = session.createSQLQuery(sqlUpdateProduct);
			numberRowProduct = queryUpdateProduct.executeUpdate();
		}
		
	    String sqlFidelityPoint = "SELECT fidelityPoint FROM Customer WHERE id = " + customerId + ";";
	    SQLQuery queryFidelityPoint = session.createSQLQuery(sqlFidelityPoint);
	    int fidelityPoint = (int) queryFidelityPoint.uniqueResult();
	    
	    double fidelityPointToUse = Math.min(fidelityPoint, price);

	    String sqlUpdateFidelityPoint = "UPDATE Customer SET fidelityPoint = fidelityPoint - " + fidelityPointToUse + " + " + price/10 + " WHERE id = " + customerId + ";";
	    SQLQuery queryUpdateFidelityPoint = session.createSQLQuery(sqlUpdateFidelityPoint);
	    int numberFidelityPoint = queryUpdateFidelityPoint.executeUpdate();

		tx.commit();
		session.close();
		
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
		String sql = "SELECT * FROM Basket WHERE customerId = "+customerId+" AND quantity > 0 AND bought = 0;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Basket.class);
		List<Basket> basketList = query.list();
		
		for (Basket basket : basketList) {
			String sqlProduct = "SELECT price FROM Product WHERE id="+basket.getProduct().getId()+";";
			SQLQuery queryProduct = session.createSQLQuery(sqlProduct);
			double price = (double) queryProduct.uniqueResult();
			
			totalPrice += price * basket.getQuantity();
		}
		session.close();
		
		CustomerDao customerDao = new CustomerDao(sessionFactory);
		Customer customer = customerDao.getCustomer(customerId);
		int fidelityPoint = customer.getFidelityPoint();
		totalPrice -= fidelityPoint;
		return (totalPrice >= 0 ? totalPrice : 0);
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
