import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import conn.HibernateUtil;
import dao.*;
import entity.*;

@SuppressWarnings("deprecation")
public class Main{
	public static void main(String[] args) {
		//Créer un admin
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		UserDao userDao = new UserDao(sessionFactory);
		Administrator admin = new Administrator("mailAdmin", "password", "admin");
		System.out.println(userDao.saveUser(admin));

		//Créer et modifier un modo
		ModeratorDao modoDao = new ModeratorDao(sessionFactory);
		Moderator moderator = new Moderator("mailModo", "password", "modo");
		System.out.println(userDao.saveUser(moderator));
		Moderator modo = modoDao.getModerator("mailModo");
		System.out.println(modo.getEmail());
		System.out.println(modoDao.modifyRight(modo, "addProduct", true));
		//System.out.println(modoDao.deleteModerator(modo));
		
		User user = userDao.getUser("mailAdmin");
		
		//Créer un client
		CustomerDao customerDao = new CustomerDao(sessionFactory);
		Customer customer = new Customer("mailCustomer", "password", "customer");
		System.out.println(userDao.saveUser(customer));
		Customer cust = customerDao.getCustomer("mailCustomer");
		List<Customer> liste = customerDao.getCustomerList();
		
		//Créer une carte bancaire
		CreditCardDao creditCardDao = new CreditCardDao(sessionFactory);
		CreditCard card = new CreditCard(123, 111, new Date(2023 - 1900, 11 - 1, 20));
		card.setCredit(1000);
		System.out.println(creditCardDao.saveCreditCard(card));
		
		//Modifier un client
		System.out.println(cust.getUsername());
		System.out.println(liste.get(0).getUsername());
		System.out.println(customerDao.setFidelityPoint(cust, 10));
		System.out.println(customerDao.setFidelityPoint(cust, -5));
		
		//Créer et modifier un produit
		ProductDao productDao = new ProductDao(sessionFactory);
		Product product = new Product("poster", 15.99, 5, "img/logo.png", user);
		System.out.println(productDao.addProduct(product));
		System.out.println(productDao.modifyProduct(product, product.getName(), 10.15, product.getStock(),product.getImg()));
		
		//Créer un panier
		BasketDao basketDao = new BasketDao(sessionFactory);
		Basket basket = new Basket(product, 2, cust);
				
		//Modifier et payer un panier
		System.out.println(basketDao.addOrder(basket));
		System.out.println(basketDao.updateQuantity(basket.getId(), 2));
		System.out.println(basketDao.confirmOrder(cust.getId()));
		if (creditCardDao.checkCreditCard(123, 111, new Date(2023 - 1900, 11 - 1, 20))) {
			System.out.println("credit card valid");
			if (creditCardDao.checkBalance(123, basketDao.totalPrice(cust.getId()))) {
				System.out.println("enough credit to pay");
				System.out.println(basketDao.finalizePaiement(cust.getId(), 123, basketDao.totalPrice(cust.getId())));
			}
		}
		//customerDao.transferIntoModerator(cust);
		//modoDao.transferIntoCustomer(modo);
		//System.out.println(basketDao.deleteOrder(basket.getId()));
		//System.out.println(productDao.deleteProduct(product.getId()));
		//System.out.println(modoDao.deleteModerator(modo));
		//System.out.println(customerDao.deleteCustomer(cust));		
		//System.out.println(creditCardDao.deleteCreditCard(card.getCardNumber()));
	}
}
