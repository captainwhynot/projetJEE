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
		int i=(Integer)session.save(card);
		if(i>0) {b=true;}
		
		tx.commit();
		session.close();
		return b;
	}
	//incomplet rajouter verif date
	public boolean getCreditCard(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String sql = "SELECT * FROM CreditCard WHERE cardNumber='"+id+";";
		SQLQuery query = session.createSQLQuery(sql);
		//int rowCount = query.();
		tx.commit();
		session.close();

		if (query == null) {
		    return false;
		} else {
		    return true;
		}
	}
	
	/*public boolean compareDate(int id, Date paramDate) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String sql = "SELECT * FROM CreditCard WHERE cardNumber='"+id+";";
		SQLQuery query = session.createSQLQuery(sql);
		CreditCard cb = 
		
	}*/
	
	public boolean getSolde(int cardNumber,double depense) {
		boolean b=false;
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String sql = "SELECT * FROM CreditCard WHERE cardNumber='"+cardNumber+"';";
		
		SQLQuery query = session.createSQLQuery(sql).addEntity(CreditCard.class);
		CreditCard cb = (CreditCard) query.uniqueResult();
		
		System.out.println("credit: "+cb.getCredit());
		System.out.println("depense: "+depense);
		if((cb.getCredit() - depense)>0) {b=true;}
		
		
		tx.commit();
		session.close();
		return b;
	}
	
	public boolean setCredit(int cb, int nvCredit) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String sql = "UPDATE CreditCard SET credit = credit"+(nvCredit>0 ? "+":"")+ nvCredit +" WHERE cardNumber="+cb+";";

		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		tx.commit();
		session.close();

		if (rowCount > 0) {
		    return true;
		} else {
		    return false;
		}
	}
}
