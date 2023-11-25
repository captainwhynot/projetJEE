package dao;

import java.util.Date;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.CreditCard;

@SuppressWarnings({"deprecation", "rawtypes"})
/**
 * Data Access Object (DAO) for managing CreditCard entities in the database.
 * This class provides methods to interact with CreditCard entities, including saving, retrieving,
 * and performing various checks related to credit cards.
 */
public class CreditCardDao {

    /**
     * The Hibernate SessionFactory for database interactions.
     */
	public SessionFactory sessionFactory;

    /**
     * Constructs a CreditCardDao instance with the specified Hibernate SessionFactory.
     *
     * @param sf The Hibernate SessionFactory.
     */
	public CreditCardDao(SessionFactory sf) {
		sessionFactory = sf;
	}

    /**
     * Saves a CreditCard entity in the database. Deletes expired cards before saving.
     *
     * @param card The CreditCard entity to be saved.
     * @return True if the card is successfully saved; false otherwise.
     */
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
			return (save > 0);
		} catch (Exception e) {
	        return false;
		} finally {
			session.close();
		}
	}

    /**
     * Deletes expired CreditCard entities from the database.
     */
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

    /**
     * Retrieves a CreditCard entity by its card number.
     *
     * @param cardNumber The card number of the CreditCard entity.
     * @return The CreditCard entity, or null if not found or expired.
     */
	public CreditCard getCreditCard(int cardNumber) {
		Session session = sessionFactory.openSession();
		CreditCard card = session.get(CreditCard.class, cardNumber);
		session.close();
		
		if (card != null && card.getExpirationDate().after(new Date())) {
			return card;
		}
		else {
			return null;
		}
	}

    /**
     * Checks the validity of a credit card based on card number, CVV, and expiration date.
     *
     * @param cardNumber The card number of the credit card.
     * @param cvv        The CVV of the credit card.
     * @param date       The expiration date of the credit card.
     * @return True if the credit card is valid; false otherwise.
     */
	public boolean checkCreditCard(int cardNumber, int cvv, Date date) {
	    Session session = sessionFactory.openSession();
	    try {
	        String sql = "SELECT * FROM CreditCard WHERE cardNumber = :cardNumber AND cvv = :cvv AND expirationDate = :expirationDate";
	        SQLQuery query = session.createSQLQuery(sql).addEntity(CreditCard.class);
	        query.setParameter("cardNumber", cardNumber);
	        query.setParameter("cvv", cvv);
	        query.setParameter("expirationDate", date);
	        CreditCard creditCard = (CreditCard) query.getSingleResult();
	        //Check if the credit card's is expired, and if the informations are invalid, catch the exception
	        return creditCard.getExpirationDate().after(new Date());
	    } catch (Exception e) {
	        return false;
	    } finally {
	        session.close();
	    }
	}

    /**
     * Checks if the credit card has sufficient balance for a given price.
     *
     * @param cardNumber The card number of the credit card.
     * @param price      The price to be checked against the credit card balance.
     * @return True if the credit card has sufficient balance; false otherwise.
     */
	public boolean checkBalance(int cardNumber, double price) {
		Session session = sessionFactory.openSession();
		
		String sql = "SELECT credit FROM CreditCard WHERE cardNumber="+cardNumber+";";
		SQLQuery query = session.createSQLQuery(sql);
		double credit = (double) query.uniqueResult();
		
		session.close();
		
		return (credit - price > 0);
	}

    /**
     * Sets the credit balance for a credit card.
     *
     * @param cardNumber The card number of the credit card.
     * @param credit     The new credit balance to set.
     * @return True if the credit balance is successfully updated; false otherwise.
     */
	public boolean setCredit(int cardNumber, int credit) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "UPDATE CreditCard SET credit = credit"+(credit>=0 ? "+":"")+ credit +" WHERE cardNumber="+cardNumber+";";
		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		
		tx.commit();
		session.close();

		return (rowCount > 0);
	}

    /**
     * Deletes a CreditCard entity from the database by its card number.
     *
     * @param cardNumber The card number of the CreditCard entity to be deleted.
     * @return True if the CreditCard entity is successfully deleted; false otherwise.
     */
	public boolean deleteCreditCard(int cardNumber) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			CreditCard card = session.get(CreditCard.class, cardNumber);
	        session.delete(card);
			tx.commit();
	        return true;
	    } catch (Exception e) {
	        return false;
	    } finally {
	        session.close();
	    }
	}
}
