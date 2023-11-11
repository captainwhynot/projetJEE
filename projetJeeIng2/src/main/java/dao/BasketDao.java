package dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Basket;

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
		
		String sql = "SELECT * FROM Basket WHERE customerId = "+ customerId +";";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Basket.class);
		List<Basket> basketList = query.list();
		
		tx.commit();
		session.close();
		
		return basketList;
	}
	
	public Basket getBasket(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM Basket WHERE id='"+id+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Basket.class);
		Basket basket = (Basket) query.uniqueResult();
		
		tx.commit();
		session.close();
		
		return basket;
	}
	
	public boolean confirmOrder(int customerId) {
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
				
				//if there is an error when updating the out of stock order, return false.
				if (rowCount <= 0) return false;
				else System.out.println("The order number n°" + basket.getId() + " has been removed due to rupture of stock.");
			}
		}
		
		tx.commit();
		session.close();
		
		return true;
	}
	
	public boolean finalizePaiement(int customerId, int cardNumber, double price) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sqlCardSolde = "UPDATE CreditCard SET credit = credit - " + price + " WHERE cardNumber = " + cardNumber + ";";
		SQLQuery queryCardSolde = session.createSQLQuery(sqlCardSolde);
		int numberRowSolde = queryCardSolde.executeUpdate();
		
		String sqlPaidBasket = "UPDATE Basket SET bought = 1 WHERE customerId = " + customerId + " AND quantity > 0;";
		SQLQuery queryPaidBasket = session.createSQLQuery(sqlPaidBasket);
		int numberRowBasket = queryPaidBasket.executeUpdate();
		
		String sqlFidelityPoint = "UPDATE Customer SET fidelityPoint = fidelityPoint + " + price/10 + " WHERE id = " + customerId + ";";
		SQLQuery queryFidelityPoint = session.createSQLQuery(sqlFidelityPoint);
		int numberFidelityPoint = queryFidelityPoint.executeUpdate();

		tx.commit();
		session.close();
		
		return (numberRowSolde > 0 && numberRowBasket > 0 && numberFidelityPoint > 0);
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
		Transaction tx = session.beginTransaction();

		//Check the price for the final order.
		String sql = "SELECT * FROM Basket WHERE customerId = "+customerId+" AND quantity > 0 AND bought = 0;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Basket.class);
		List<Basket> basketList = query.list();
		
		for (Basket basket : basketList) {
			String sqlProduct = "SELECT price FROM Product WHERE id="+basket.getProductId()+";";
			SQLQuery queryProduct = session.createSQLQuery(sqlProduct);
			double price = (double) queryProduct.uniqueResult();
			
			totalPrice += price * basket.getQuantity();
		}

		tx.commit();
		session.close();
		
		return totalPrice;
	}
	
	public boolean deleteOrder(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "DELETE FROM Basket WHERE id="+id+";";
		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		
		tx.commit();
		session.close();

		return (rowCount > 0);
	}
}
