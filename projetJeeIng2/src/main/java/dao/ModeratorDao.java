package dao;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Moderator;

public class ModeratorDao {
public SessionFactory sessionFactory;
	
	public ModeratorDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean addModerator (Moderator moderator) {
		boolean b = false;		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		int i=(Integer)session.save(moderator);
		if (i>0) b=true;
		
		tx.commit();
		session.close();
		
		return b;
	}
	
	public boolean deleteModerator (Moderator moderator) {
		boolean b = false;		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "DELETE * FROM Moderator WHERE id="+moderator.getId()+";";
		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		
		tx.commit();
		session.close();

		return (rowCount > 0);
	}
	
	public boolean modifyRight (Moderator moderator, String right, boolean bool) {
		boolean b = false;		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "UPDATE Moderator SET "+right+"='"+bool+"' WHERE id="+moderator.getId()+";";
		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		
		tx.commit();
		session.close();

		return (rowCount > 0);
	}
}
