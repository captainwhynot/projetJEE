package dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Basket;
import entity.Customer;
import entity.Moderator;
import entity.User;

@SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
/**
 * Data Access Object (DAO) for managing Customer entities in the database.
 * This class provides methods to interact with Customer entities, including retrieval,
 * fidelity point management, and customer-related operations.
 */
public class CustomerDao {

    /**
     * The Hibernate SessionFactory for database interactions.
     */
	public SessionFactory sessionFactory;

    /**
     * Constructs a CustomerDao instance with the specified Hibernate SessionFactory.
     *
     * @param sf The Hibernate SessionFactory.
     */
	public CustomerDao(SessionFactory sf) {
		sessionFactory = sf;
	}

    /**
     * Retrieves a list of all Customer entities.
     *
     * @return List of Customer entities.
     */
	public List<Customer> getCustomerList(){
		Session session = sessionFactory.openSession();
		
		String sql = "SELECT * FROM Customer c JOIN User u ON c.id = u.id;";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Customer.class);
		List<Customer> customerList = query.list();
		
		session.close();
		
		return customerList;
	}

    /**
     * Retrieves a Customer entity by email.
     *
     * @param email The email of the Customer.
     * @return The Customer entity, or null if not found.
     */
	public Customer getCustomer(String email) {
		Session session = sessionFactory.openSession();
		
		String sql = "SELECT * FROM Customer c JOIN User u ON c.id = u.id WHERE u.email='"+email+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Customer.class);
		Customer customer = (Customer) query.uniqueResult();
		
		session.close();
		
		return customer;
	}

    /**
     * Retrieves a Customer entity by ID.
     *
     * @param id The ID of the Customer.
     * @return The Customer entity, or null if not found.
     */
	public Customer getCustomer(int id) {
		Session session = sessionFactory.openSession();
		
		String sql = "SELECT * FROM Customer c JOIN User u ON c.id = u.id WHERE u.id='"+id+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(Customer.class);
		Customer customer = (Customer) query.uniqueResult();
		
		session.close();
		
		return customer;
	}

    /**
     * Sets the fidelity points for a Customer.
     *
     * @param customer The Customer entity.
     * @param points   The fidelity points to set.
     * @return True if fidelity points are successfully updated; false otherwise.
     */
	public boolean setFidelityPoint(Customer customer, int points) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		try {
			String sql = "UPDATE Customer SET fidelityPoint = fidelityPoint"+ (points>=0 ? "+":"") + points + " WHERE id="+customer.getId()+";";
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

    /**
     * Transfers a Customer into a Moderator role.
     *
     * @param customer The Customer to transfer.
     * @return True if the transfer is successful; false otherwise.
     */
	public boolean transferIntoModerator(Customer customer) {
		UserDao userDao = new UserDao(sessionFactory);
		Moderator moderator = new Moderator(customer.getEmail(), customer.getPassword(), customer.getUsername());
	    boolean delete = deleteCustomer(customer);
	    boolean save = userDao.saveUser(moderator);
		return (delete && save);
	}

    /**
     * Deletes a Customer entity, associated User, and related Baskets from the database.
     *
     * @param customer The Customer entity to delete.
     * @return True if the deletion is successful; false otherwise.
     */
	public boolean deleteCustomer(Customer customer) {
	    Session session = sessionFactory.openSession();
	    Transaction tx = session.beginTransaction();

	    try {
	        User user = customer.getUser();
	        List<Basket> baskets = customer.getBaskets();
	        //Delete the customer & the user & the customer's basket
	        session.delete(customer);
	        if (user != null) {
	            session.delete(user);
	        }
	        for (Basket basket : baskets) {
	            session.delete(basket);
	        }

	        tx.commit();
	        return true;
	    } catch (Exception e) {
	        return false;
	    } finally {
	        session.close();
	    }
	}

}
