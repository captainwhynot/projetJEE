package dao;

import java.util.Date;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.CreditCard;
import entity.Customer;

public class CreditCardDao {
	public SessionFactory sessionFactory;
		
	public CreditCardDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean saveCreditCard (CreditCard card) {
		boolean b = false;		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		//CreditCard n'existe pas dans la bdd
		int i=(Integer)session.save(card);
		if(i>0) {b=true;}
		
		tx.commit();
		session.close();
		return b;
	}

	public CreditCard getCreditCard(int cardNumber) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM CreditCard WHERE cardNumber='"+cardNumber+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(CreditCard.class);
		CreditCard card = (CreditCard) query.uniqueResult();
		
		tx.commit();
		session.close();
		
		if (card.getExpirationDate().compareTo(new Date()) >= 0) {
			return card;
		}
		else {
			return null;
		}
	}
	
	/*public boolean compareDate(int id, Date paramDate) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String sql = "SELECT * FROM CreditCard WHERE cardNumber='"+id+";";
		SQLQuery query = session.createSQLQuery(sql);
		CreditCard cb = 
		
	}*/
	
	public boolean checkCreditCard(int cardNumber, int cvv, Date date) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sqlCardNumber = "SELECT * FROM CreditCard WHERE cardNumber="+cardNumber+" AND cvv="+cvv+" AND expirationDate="+date+";";
		SQLQuery queryCardNumber = session.createSQLQuery(sqlCardNumber);
		int rowCount = queryCardNumber.executeUpdate();

		tx.commit();
		session.close();

		return (rowCount > 0);
	}
	
	public boolean checkBalance(int cardNumber, double price) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sqlCardNumber = "SELECT credit FROM CreditCard WHERE cardNumber="+cardNumber+";";
		SQLQuery queryCardNumber = session.createSQLQuery(sqlCardNumber);
		double credit = (double) queryCardNumber.uniqueResult();
		
		tx.commit();
		session.close();
		System.out.println(credit + " - " + price);
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
