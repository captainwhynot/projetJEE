import java.util.List;

import org.hibernate.SessionFactory;

import conn.HibernateUtil;
import dao.*;
import entity.*;

public class Main {
	public static void main(String[] args) {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		AdministratorDao adminDao = new AdministratorDao(sessionFactory);
		Administrator admin = new Administrator("mailAdmin", "password", "admin");
		//adminDao.saveAdmin(admin);


		ModeratorDao modoDao = new ModeratorDao(sessionFactory);
		Moderator modo = new Moderator("mailModo", "password", "modo");
		System.out.println(modoDao.addModerator(modo));
		System.out.println(modoDao.modifyRight(modo, "addProduct", true));
		//System.out.println(modoDao.deleteModerator(modo));
		
		
		CustomerDao customerDao = new CustomerDao(sessionFactory);
		Customer customer = new Customer("mailCustomer", "password", "customer");
		customerDao.saveCustomer(customer);
		Customer cust = customerDao.getCustomerById(1);
		List<Customer> liste = customerDao.getAllCustomer();
		
		System.out.println(cust.getUsername());
		System.out.println(liste.get(0).getUsername());
		System.out.println(customerDao.setFidelityPoint(cust, 10));
		System.out.println(customerDao.setFidelityPoint(cust, -5));
		
		ProductDao productDao = new ProductDao(sessionFactory);
		Product product = new Product("poster", 15.99, 5, modo.getId());
		System.out.println(productDao.addProduct(product));
		System.out.println(productDao.modifyProduct(product, product.getName(), 50.15, product.getStock()));
		
		
	}
}
