

import dao.CreditCardDao;
import dao.CustomerDao;
import entity.CreditCard;
import entity.Customer;

import conn.HibernateUtil;
public class Main {
	public static void main(String[] args) {
		//CustomerDao dao = new CustomerDao(HibernateUtil.getSessionFactory());
		//Customer customer = new Customer("mail2", "password2", "username2");
		//dao.saveCustomer(customer);
		//Customer cust = dao.getCustomerById(1);
		//List<Customer> liste = dao.getAllCustomer();
		
		
		//System.out.println("testSET: "+dao.setFidelityPoint(cust, 1));
		
		//dao.getCustomerConnexion("username1", "password1");
		CreditCardDao dao = new CreditCardDao(HibernateUtil.getSessionFactory());
		//CreditCard cb = new CreditCard(1,123,123,new Date());
		//cb.setCredit(10);
		//dao.saveCreditCard(cb);
		//System.out.println(dao.getSolde(123, 11));
		System.out.println(dao.setCredit(123, -10));

		
		
	}
}
