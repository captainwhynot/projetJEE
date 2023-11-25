package conn;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import entity.*;

/**
 * Hibernate utility class for managing the SessionFactory in a Java application.
 * Ensures a single SessionFactory instance is shared across the application.
 */
public class HibernateUtil {
	
	private static SessionFactory sessionFactory;
	
	/**
     * Retrieves the Hibernate SessionFactory instance. If not created, configures and builds a new instance.
     *
     * @return The Hibernate SessionFactory instance.
     */
	public static SessionFactory getSessionFactory() {
		if(sessionFactory == null) {
			Configuration configuration = new Configuration();
			Properties properties = new Properties();
			properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
			properties.put(Environment.URL, "jdbc:mysql://localhost:3306/bdd_projet_jee");
			properties.put(Environment.USER, "root");
			properties.put(Environment.PASS, "");
			properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
			properties.put(Environment.HBM2DDL_AUTO, "update");
			properties.put(Environment.SHOW_SQL, true);
			
			configuration.setProperties(properties);	
			configuration.addAnnotatedClass(User.class);
			configuration.addAnnotatedClass(Customer.class);
			configuration.addAnnotatedClass(Administrator.class);
			configuration.addAnnotatedClass(Moderator.class);
			configuration.addAnnotatedClass(Product.class);
			configuration.addAnnotatedClass(Basket.class);
			configuration.addAnnotatedClass(CreditCard.class);
			
			
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			
			sessionFactory=configuration.buildSessionFactory(serviceRegistry);
			
		}
		return sessionFactory;
	}

}