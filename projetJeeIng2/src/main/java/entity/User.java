package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String email;
	private String password;
	private String username;
	private String typeUser;
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Moderator moderator;
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Customer customer;
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Administrator admin;
	
	public User() {
		
	}
	
	public User(String email, String password, String username, String typeUser) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.setTypeUser(typeUser);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTypeUser() {
		return typeUser;
	}

	public void setTypeUser(String typeUser) {
		this.typeUser = typeUser;
	}
	
}
