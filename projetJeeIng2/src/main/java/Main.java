import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.mindrot.jbcrypt.BCrypt;

import conn.HibernateUtil;
import dao.*;
import entity.*;

@SuppressWarnings("deprecation")
public class Main{
	public static void main(String[] args) {
		//Créer un admin
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		UserDao userDao = new UserDao(sessionFactory);
		Administrator admin = new Administrator("mailAdmin", BCrypt.hashpw("password", BCrypt.gensalt(12)), "admin");
		System.out.println(userDao.saveUser(admin));

		//Créer et modifier un modo
		ModeratorDao modoDao = new ModeratorDao(sessionFactory);
		Moderator moderator = new Moderator("mailModo", BCrypt.hashpw("password", BCrypt.gensalt(12)), "modo");
		System.out.println(userDao.saveUser(moderator));
		Moderator modo = modoDao.getModerator("mailModo");
		System.out.println(modo.getEmail());
		System.out.println(modoDao.modifyRight(modo, "addProduct", true));
		
		moderator = new Moderator("nie", BCrypt.hashpw("password", BCrypt.gensalt(12)), "nie");
		System.out.println(userDao.saveUser(moderator));
		modo = modoDao.getModerator("nie");
		
		//System.out.println(modoDao.deleteModerator(modo));
		
		User user = userDao.getUser("mailAdmin");
		
		//Créer un client
		CustomerDao customerDao = new CustomerDao(sessionFactory);
		Customer customer = new Customer("mailCustomer", BCrypt.hashpw("password", BCrypt.gensalt(12)), "chris");
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
		Product product = new Product("DragonBall Tome 1", 7.95, 101, "img/products/db_1.jpg", user);
		System.out.println(productDao.addProduct(product));
		
		product = new Product("Naruto Tome 1", 7.95, 100, "img/products/naruto_1.jpg", user);
		System.out.println(productDao.addProduct(product));
		
		product = new Product("One Piece Tome 1", 7.95, 100, "img/products/op_1.jpg", user);
		System.out.println(productDao.addProduct(product));
		
		product = new Product("L'Attaque des Titans Tome 1", 5.95, 17, "img/products/snk_1.jpg", modo);
		System.out.println(productDao.addProduct(product));
		
		product = new Product("L'Attaque des Titans Tome 30", 6.55, 9, "img/products/snk_30.jpg", modo);
		System.out.println(productDao.addProduct(product));
		
		product = new Product("Vinland Saga Tome 6", 6.55, 12, "img/products/vs_6.jpg", modo);
		System.out.println(productDao.addProduct(product));
		
		product = new Product("Hunter X Hunter Tome 37", 6.99, 19, "img/products/hxh_37.jpg",modo);
		System.out.println(productDao.addProduct(product));
		
		product = new Product("Jujutsu Kaisen Tome 0", 7.99, 4, "img/products/jjk_0.jpg", modo);
		System.out.println(productDao.addProduct(product));
		
		moderator = new Moderator("UnLibraire", BCrypt.hashpw("password", BCrypt.gensalt(12)), "UnLibraire");
		System.out.println(userDao.saveUser(moderator));
		modo = modoDao.getModerator("UnLibraire");
		
		product = new Product("My Hero Academia Tome 25", 6.95, 13, "img/products/mha_25.jpg", modo);
		System.out.println(productDao.addProduct(product));
		
		product = new Product("Hunter X Hunter Tome 1", 6.90, 11, "img/products/hxh_1.jpg", modo);
		System.out.println(productDao.addProduct(product));
		
		product = new Product("Fire Punch Tome 1", 6.90, 7, "img/products/fp_1.jpg", modo);
		System.out.println(productDao.addProduct(product));
		
		product = new Product("Blue Lock Tome 1", 6.90, 8, "img/products/bl_1.jpg", modo);
		System.out.println(productDao.addProduct(product));
		
		product = new Product("Chainsaw Man Tome 14", 7.00, 22, "img/products/csm_14.jpg", modo);
		System.out.println(productDao.addProduct(product));
		//System.out.println(productDao.modifyProduct(product, product.getName(), 10.15, product.getStock(),product.getImg()));
		
		//Créer un panier
		BasketDao basketDao = new BasketDao(sessionFactory);
		Basket basket = new Basket(product, 2, cust);
				
		//Modifier et payer un panier
		System.out.println(basketDao.addOrder(basket, cust.getId(), 1));
		System.out.println(basketDao.updateQuantity(basket.getId(), 2));
		System.out.println(basketDao.confirmOrder(cust.getId()));
		if (creditCardDao.checkCreditCard(123, 111, new Date(2023 - 1900, 11 - 1, 20))) {
			System.out.println("credit card valid");
			if (creditCardDao.checkBalance(123, basketDao.totalPrice(cust.getId()))) {
				System.out.println("enough credit to pay");
				System.out.println(basketDao.finalizePaiement(cust.getId(), 123, basketDao.totalPrice(cust.getId()),"testMain"));
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
