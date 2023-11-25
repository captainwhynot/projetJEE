package dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Customer;
import entity.Moderator;
import entity.Product;
import entity.User;

@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
/**
 * Data Access Object (DAO) for managing Moderator entities in the database.
 * This class provides methods to interact with Moderator entities, including retrieval,
 * role transfers, and moderator-related operations.
 */
public class ModeratorDao {

    /**
     * The Hibernate SessionFactory for database interactions.
     */
	public SessionFactory sessionFactory;

    /**
     * Constructs a ModeratorDao instance with the specified Hibernate SessionFactory.
     *
     * @param sf The Hibernate SessionFactory.
     */
	public ModeratorDao(SessionFactory sf) {
		sessionFactory = sf;
	}

    /**
     * Retrieves a list of all Moderator entities.
     *
     * @return List of Moderator entities.
     */
	public List<Moderator> getModeratorList(){
		Session session = sessionFactory.openSession();
		
		String sql = "SELECT * FROM Moderator m JOIN User u ON m.id = u.id;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Moderator.class);
		List<Moderator> ModeratorList = query.list();
		
		session.close();
		
		return ModeratorList;
	}

    /**
     * Retrieves a Moderator entity by email.
     *
     * @param email The email of the Moderator.
     * @return The Moderator entity, or null if not found.
     */
	public Moderator getModerator(String email) {
		Session session = sessionFactory.openSession();
		
		String sql = "SELECT * FROM Moderator m JOIN User u ON m.id = u.id WHERE u.email = '" + email + "';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Moderator.class);
		Moderator moderator = (Moderator) query.uniqueResult();
		
		session.close();
		
		return moderator;
	}

    /**
     * Retrieves a Moderator entity by ID.
     *
     * @param id The ID of the Moderator.
     * @return The Moderator entity, or null if not found.
     */
	public Moderator getModerator(int id) {
		Session session = sessionFactory.openSession();
		
		String sql = "SELECT * FROM Moderator m JOIN User u ON m.id = u.id WHERE u.id = '" + id + "';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Moderator.class);
		Moderator moderator = (Moderator) query.uniqueResult();
		
		session.close();
		
		return moderator;
	}

    /**
     * Transfers a Moderator into a Customer role.
     *
     * @param moderator The Moderator to transfer.
     * @return True if the transfer is successful; false otherwise.
     */
	public boolean transferIntoCustomer(Moderator moderator) {
		UserDao userDao = new UserDao(sessionFactory);
		Customer customer = new Customer(moderator.getEmail(), moderator.getPassword(), moderator.getUsername());
	    boolean delete = deleteModerator(moderator);
	    boolean save = userDao.saveUser(customer);
		return (delete && save);
	}

    /**
     * Deletes a Moderator entity, associated User, and related Products from the database.
     *
     * @param moderator The Moderator entity to delete.
     * @return True if the deletion is successful; false otherwise.
     */
	public boolean deleteModerator(Moderator moderator) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		try {
	        User user = moderator.getUser();
	        List<Product> products = user.getProducts();
	        // Delete the moderator & the user & the moderator's products
	        session.delete(moderator);
	        for (Product product : products) {
	            session.delete(product);
	        }
	        if (user != null) {
	            session.delete(user);
	        }
	        
	        tx.commit();
	        return true;
	    } catch (Exception e) {
	        return false;
	    } finally {
	        session.close();
	    }
	}

    /**
     * Modifies the rights of a Moderator.
     *
     * @param moderator The Moderator entity to modify.
     * @param right     The right to modify (e.g., "canModifyProducts").
     * @param bool      The new value of the right (true or false).
     * @return True if the modification is successful; false otherwise.
     */
	public boolean modifyRight(Moderator moderator, String right, boolean bool) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		try {
			String sql = "UPDATE Moderator SET "+right+"="+(bool?"1":"0")+" WHERE id="+moderator.getId()+";";
			SQLQuery query = session.createSQLQuery(sql);
			int rowCount = query.executeUpdate();
			
			tx.commit();
			return (rowCount > 0);
		} catch (Exception e) {
	        return false;
	    } finally {
	        session.close();
	    }

	}
}
