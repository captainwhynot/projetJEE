//import java.util.ArrayList;

import java.util.List;
import conn.HibernateUtil;
import dao.CustomerDao;
import entity.Customer;

public class Main {
	public static void main(String[] args) {
		CustomerDao dao = new CustomerDao(HibernateUtil.getSessionFactory());
		Customer customer = new Customer("mail1", "password1", "username1");
		//dao.saveCustomer(customer);
		Customer cust = dao.getCustomerById(1);
		List<Customer> liste = dao.getAllCustomer();
		
		
		System.out.println(cust.getUsername());
		
		System.out.println(liste.get(0).getUsername());
		
		System.out.println(dao.setFidelityPoint(cust, 10));
		System.out.println(dao.setFidelityPoint(cust, -5));
		
	}
}
