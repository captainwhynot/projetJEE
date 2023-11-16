package dao;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.Part;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

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
						break;
					case "Customer" :
						Customer customer = new Customer(user.getEmail(), user.getPassword(), user.getUsername());
						save = (Integer) session.save(customer);
						break;
					case "Moderator" :
						Moderator moderator = new Moderator(user.getEmail(), user.getPassword(), user.getUsername());
						save = (Integer) session.save(moderator);
						break;
				}
				tx.commit();
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
		
		String sql = "SELECT *, 0 AS clazz_ FROM User WHERE email = '"+ user.getEmail() +"';";
		SQLQuery query = session.createSQLQuery(sql).addEntity(User.class);		
		List<User> userList = query.list();
		
		session.close();
		return (userList.isEmpty());
	}
	
	public boolean checkUserLogin(String email, String password) {
		Session session = sessionFactory.openSession();
		boolean isAuthenticated = false;
		try {
			String sql = "SELECT *, 0 AS clazz_ FROM User WHERE email='"+email+"';";
			SQLQuery query = session.createSQLQuery(sql).addEntity(User.class);
			User user = (User) query.getSingleResult();
			isAuthenticated = BCrypt.checkpw(password, user.getPassword());
			
			System.out.println("Connecté : " + BCrypt.checkpw(password, user.getPassword()));
			System.out.println("Mot de passe fourni par l'utilisateur : " + password);
			System.out.println("Mot de passe haché récupéré de la base de données : " + user.getPassword());
			//return (!userList.isEmpty());
			return isAuthenticated;
		} catch (Exception e) {
			return false;
		} finally {
            session.close();
        }
	}
	
	public User getUser(String email) {
	    Session session = sessionFactory.openSession();

        String sql = "SELECT *, 0 AS clazz_ FROM User WHERE email = '"+email+"';";
        SQLQuery query = session.createSQLQuery(sql).addEntity(User.class);
        User user = (User) query.uniqueResult();
        
        session.close();

	    return user;
	}
	
	public User getUser(int id) {
	    Session session = sessionFactory.openSession();
		User user = session.get(User.class, id);
		session.close();

	    return user;
	}
	
	public boolean updateUser(User user, String email, String username, String password) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		try {
			String sql = "UPDATE User SET email='"+email+"', username='"+username+"', password='"+password+"' WHERE id="+user.getId()+";";
			SQLQuery query = session.createSQLQuery(sql);
			int rowCount = query.executeUpdate();
			
			tx.commit();
			return (rowCount > 0);	
		} catch (Exception e) {
	        return false;
		} finally {
			session.close();
		}
	}
	
	public boolean updateProfilePicture(User user, Part filePart, String profilePicture, String savePath) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
	        int userId = user.getId();

	        File imgDir = new File(savePath);
	        File[] files = imgDir.listFiles((dir, name) -> name.startsWith(userId + "_"));
	        
	        //Delete all the old profile picture of the user
	        if (files != null) {
	            for (File file : files) {
	                file.delete();
	            }
	        }
	        
			//Create profil folder if it does not exist
			File saveDir = new File(savePath);
	        if (!saveDir.exists()) {
	            saveDir.mkdirs();
	        }

	        //Save the profile picture in the folder
			String filePath = savePath + File.separator + profilePicture;
			filePart.write(filePath);
	        
			String sql = "UPDATE User SET profilePicture='img/Profil/"+profilePicture+"' WHERE id="+userId+";";
			SQLQuery query = session.createSQLQuery(sql);
			int rowCount = query.executeUpdate();
			
			tx.commit();
			return (rowCount > 0);	
		} catch (Exception e) {
	        return false;
		} finally {
			session.close();
		}
	}
	
	public boolean deleteProfilePicture(User user, String savePath) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
	        int userId = user.getId();
	        
	        File imgDir = new File(savePath);
	        File[] files = imgDir.listFiles((dir, name) -> name.startsWith(userId + "_"));
	        
	        //Delete all the old profile picture of the user
	        if (files != null) {
	            for (File file : files) {
	                file.delete();
	            }
	        }
	        
			String sql = "UPDATE User SET profilePicture='img/profilePicture.png' WHERE id="+userId+";";
			SQLQuery query = session.createSQLQuery(sql);
			int rowCount = query.executeUpdate();
			
			tx.commit();
			return (rowCount > 0);	
		} catch (Exception e) {
	        return false;
		} finally {
			session.close();
		}
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
	