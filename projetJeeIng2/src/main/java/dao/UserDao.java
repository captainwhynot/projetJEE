package dao;

import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entity.Administrator;
import entity.Customer;
import entity.Moderator;
import entity.User;

@SuppressWarnings({ "rawtypes", "deprecation", "unchecked"})
public class UserDao {
	public SessionFactory sessionFactory;
	
	public UserDao(SessionFactory sf) {
		sessionFactory = sf;
	}
	
	public boolean saveUser(User user) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		int save = 0;
		if (checkUserMail(user)) {
			try {
				switch (user.getTypeUser()) {
					case "Administrator" :
						Administrator admin = new Administrator(user.getEmail(), user.getPassword(), user.getUsername());
						save = (Integer) session.save(admin);
						tx.commit();
						break;
					case "Customer" :
						Customer customer = new Customer(user.getEmail(), user.getPassword(), user.getUsername());
						save = (Integer) session.save(customer);
						tx.commit();
						break;
					case "Moderator" :
						Moderator moderator = new Moderator(user.getEmail(), user.getPassword(), user.getUsername());
						save = (Integer) session.save(moderator);
						tx.commit();
						break;
				}
			} catch (Exception e) {
		        return false;
			} finally {
				session.close();
			}
		}
		
		return (save > 0);
	}
	
	public boolean checkUserMail(User user) {
		Session session = sessionFactory.openSession();
		
		try {
			String sql = "SELECT * FROM User WHERE email = '"+ user.getEmail() +"';";
			SQLQuery query = session.createSQLQuery(sql).addEntity(User.class);		
			List<User> userList = query.list();
	
		    return (userList.isEmpty());
		} catch (Exception e) {
	        return false;
		} finally {
			session.close();
		}
	}
	
	public boolean checkUserLogin(String email, String password) {
		Session session = sessionFactory.openSession();
		
		String sql = "SELECT *, 0 AS clazz_ FROM User WHERE email='"+email+"' AND password ='"+password+"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(User.class);
		List<User> userList = (List<User>) query.list();
		
		session.close();
		
		return (!userList.isEmpty());
	}
	
	public User getUser(String email) {
	    Session session = sessionFactory.openSession();

	    User user = null;
	    try {
	        String sql = "SELECT *, 0 AS clazz_ FROM User WHERE email = :email";
	        SQLQuery query = session.createSQLQuery(sql).addEntity(User.class);
	        query.setParameter("email", email);

	        user = (User) query.uniqueResult();
	    } catch (Exception e) {
	        return null;
	    } finally {
	        session.close();
	    }

	    return user;
	}
	
	public User getUser(int id) {
	    Session session = sessionFactory.openSession();
		User user = session.get(User.class, id);
		session.close();

	    return user;
	}

	public boolean sendMail(String email, String object, String container) {
        String siteMail = "mangastorejee2023@gmail.com";
        String password = "huwc xvtz rxiy xbqf";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        javax.mail.Session session = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(siteMail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            
            message.setFrom(new InternetAddress(siteMail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(object);
            message.setContent(container, "text/html");

            Transport.send(message);
            return true;

        } catch (Exception e) {
            return false;
        } 
	}
	
}
	