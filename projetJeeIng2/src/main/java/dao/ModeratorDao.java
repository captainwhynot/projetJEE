package dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Moderator;

@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
public class ModeratorDao {
	public SessionFactory sessionFactory;
	
	public ModeratorDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean addModerator(Moderator moderator) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		int save = 0;
		if (checkModerator(moderator)) {
			save= (Integer) session.save(moderator);
		}
		else {
			System.out.println("The moderator already exists in the database.");
		}
		
		tx.commit();
		session.close();
		
		return (save > 0);
	}
	
	public boolean checkModerator(Moderator moderator) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM Moderator WHERE email = '"+ moderator.getEmail() +"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Moderator.class);		
		Moderator result = (Moderator) query.uniqueResult();

	    tx.commit();
	    session.close();

	    return (result == null);
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
	
	public Moderator getModerator(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM Moderator WHERE id='"+id+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Moderator.class);
		Moderator moderator = (Moderator) query.uniqueResult();
		
		tx.commit();
		session.close();
		
		return moderator;
	}
	
	public boolean deleteModerator(Moderator moderator) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "DELETE FROM Moderator WHERE id="+moderator.getId()+";";
		SQLQuery query = session.createSQLQuery(sql);
		int rowCount = query.executeUpdate();
		
		tx.commit();
		session.close();

		return (rowCount > 0);
	}
	
	public boolean modifyRight(Moderator moderator, String right, boolean bool) {
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
