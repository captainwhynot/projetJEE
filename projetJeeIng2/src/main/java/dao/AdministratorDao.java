package dao;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Administrator;

@SuppressWarnings({"deprecation", "rawtypes"})
/**
 * Data Access Object (DAO) for handling Administrator entities in the database.
 * This class provides methods to interact with the Administrator entity using Hibernate.
 */
public class AdministratorDao {
	
	/**
     * The Hibernate SessionFactory for database interactions.
     */
	public SessionFactory sessionFactory;
	
	/**
     * Constructs an AdministratorDao instance with the specified Hibernate SessionFactory.
     *
     * @param sf The Hibernate SessionFactory.
     */
	public AdministratorDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	/**
     * Retrieves an Administrator entity from the database based on the provided email.
     *
     * @param email The email of the Administrator to retrieve.
     * @return The Administrator entity, or null if not found.
     */
	public Administrator getAdministrator(String email) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String sql = "SELECT * FROM Administrator a JOIN User u ON a.id = u.id WHERE u.email='"+email+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Administrator.class);
		Administrator admin = (Administrator) query.uniqueResult();
		
		tx.commit();
		session.close();
		
		return admin;
	}
}