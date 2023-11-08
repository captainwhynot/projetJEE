import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import conn.HibernateUtil;
import dao.*;
import entity.*;

public class Main {
	public static void main(String[] args) {
		//Créer un admin
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		AdministratorDao adminDao = new AdministratorDao(sessionFactory);
		Administrator admin = new Administrator("mailAdmin", "password", "admin");
		adminDao.saveAdmin(admin);

		//Créer et modifier un modo
		ModeratorDao modoDao = new ModeratorDao(sessionFactory);
		Moderator modo = new Moderator("mailModo", "password", "modo");
		System.out.println(modoDao.addModerator(modo));
		System.out.println(modoDao.modifyRight(modo, "addProduct", true));
		//System.out.println(modoDao.deleteModerator(modo));
		
		//Créer un client
		CustomerDao customerDao = new CustomerDao(sessionFactory);
		Customer customer = new Customer("mailCustomer", "password", "customer");
		customerDao.saveCustomer(customer);
		Customer cust = customerDao.getCustomerById(1);
		List<Customer> liste = customerDao.getCustomerList();
		
		//Créer une carte bancaire
		CreditCardDao creditCardDao = new CreditCardDao(sessionFactory);
		CreditCard card = new CreditCard(123, 111, new Date());
		//System.out.println(creditCardDao.saveCreditCard(card));
		
		//Modifier un client
		System.out.println(cust.getUsername());
		System.out.println(liste.get(0).getUsername());
		System.out.println(customerDao.setFidelityPoint(cust, 10));
		System.out.println(customerDao.setFidelityPoint(cust, -5));
		
		//Créer et modifier un produit
		ProductDao productDao = new ProductDao(sessionFactory);
		Product product = new Product("poster", 15.99, 5, modo.getId());
		System.out.println(productDao.addProduct(product));
		System.out.println(productDao.modifyProduct(product, product.getName(), 10.15, product.getStock()));
		
		//Créer un panier
		BasketDao basketDao = new BasketDao(sessionFactory);
		Basket basket = new Basket(product.getId(), 2, customer.getId());
				
		//Modifier et payer un panier
		System.out.println(basketDao.addOrder(basket));
		System.out.println(basketDao.updateQuantity(basket.getId(), 2));
		System.out.println(basketDao.confirmOrder(customer.getId()));
		if (true/*creditCardDao.checkCreditCard(123, 111, new Date(2023,11,07))*/) {
			System.out.println("credit card valid");
			if (creditCardDao.checkBalance(123, basketDao.totalPrice(customer.getId()))) {
				System.out.println("enough credit to pay");
				System.out.println(basketDao.finalizePaiement(customer.getId(), 123, basketDao.totalPrice(customer.getId())));
			}
		}
		
		
	}
}
