import conn.HibernateUtil;
import dao.CustomerDao;

public class Main {
	public static void main(String[] args) {
		CustomerDao dao = new CustomerDao(HibernateUtil.getSessionFactory());
	}
}
