package dao;

import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.CreditCard;

@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
public class CreditCardDao {
	public SessionFactory sessionFactory;
		
	public CreditCardDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean saveCreditCard(CreditCard card) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		int save = 0;
		try {
			deleteExpiredCard();
			if (this.getCreditCard(card.getCardNumber()) == null) {
				save = (Integer) session.save(card);
				tx.commit();
			}
			else {
				System.out.println("The credit card already exists in the database.");
			}
			return (save > 0);
		} catch (Exception e) {
	        return false;
		} finally {
			session.close();
		}
	}
	
	public void deleteExpiredCard() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "DELETE FROM CreditCard WHERE expirationDate < :currentDate";
	    SQLQuery query = session.createSQLQuery(sql);
	    query.setParameter("currentDate", new Date());
		query.executeUpdate();

		tx.commit();
		session.close();
	}

	public CreditCard getCreditCard(int cardNumber) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM CreditCard WHERE cardNumber='"+cardNumber+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(CreditCard.class);
		CreditCard card = (CreditCard) query.uniqueResult();
		
		tx.commit();
		session.close();
		
		if (card != null && card.getExpirationDate().after(new Date())) {
			return card;
		}
		else {
			return null;
		}
	}
	
	public boolean checkCreditCard(int cardNumber, int cvv, Date date) {
	    Session session = sessionFactory.openSession();
	    Transaction tx = session.beginTransaction();

	    try {
	        String sql = "SELECT * FROM CreditCard WHERE cardNumber = :cardNumber AND cvv = :cvv AND expirationDate = :expirationDate";
	        SQLQuery query = session.createSQLQuery(sql).addEntity(CreditCard.class);
	        query.setParameter("cardNumber", cardNumber);
	        query.setParameter("cvv", cvv);
	        query.setParameter("expirationDate", date);

	        List<CreditCard> creditCard = query.list();

	        tx.commit();
	    	System.out.println(!creditCard.isEmpty());
	        return !creditCard.isEmpty();
	    } catch (Exception e) {
	        tx.rollback();
	        e.printStackTrace();
	        return false;
	    } finally {
	        session.close();
	    }
	}
	
	public boolean checkBalance(int cardNumber, double price) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT credit FROM CreditCard WHERE cardNumber="+cardNumber+";";
		SQLQuery query = session.createSQLQuery(sql);
		double credit = (double) query.uniqueResult();
		
		tx.commit();
		session.close();
		
		return (credit - price > 0);
	}
	
	public boolean setCredit(int cardNumber, int credit) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "UPDATE CreditCard SET credit = credit"+(credit>0 ? "+":"")+ credit +" WHERE cardNumber="+cardNumber+";";
		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		
		tx.commit();
		session.close();

		return (rowCount > 0);
	}
	
	public boolean deleteCreditCard(int cardNumber) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "DELETE FROM CreditCard WHERE cardNumber="+cardNumber+";";
		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		
		tx.commit();
		session.close();

		return (rowCount > 0);
	}
}
