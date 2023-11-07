import conn.HibernateUtil;
import dao.UserDao;

public class Main {
	public static void main(String[] args) {
		UserDao dao= new UserDao(HibernateUtil.getSessionFactory());
		dao.getUser("tyaty","tyty");
	}
}
