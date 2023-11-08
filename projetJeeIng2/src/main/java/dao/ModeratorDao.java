package dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Customer;
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
	
	public List<Moderator> getModeratorList(){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM Moderator;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Moderator.class);
		List<Moderator> ModeratorList = query.list();
		
		tx.commit();
		session.close();
		
		return ModeratorList;
	}
	
	public Moderator getModeratorById(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM Moderator WHERE id='"+id+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Moderator.class);
		Moderator moderator = (Moderator) query.uniqueResult();
		
		tx.commit();
		session.close();
		
		return moderator;
	}
	
	public boolean deleteModerator (Moderator moderator) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "DELETE FROM Moderator WHERE id="+moderator.getId()+";";
		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		
		tx.commit();
		session.close();

		return (rowCount > 0);
	}
	
	public boolean modifyRight (Moderator moderator, String right, boolean bool) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "UPDATE Moderator SET "+right+"="+(bool?"1":"0")+" WHERE id="+moderator.getId()+";";
		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		
		tx.commit();
		session.close();

		return (rowCount > 0);
	}
}
