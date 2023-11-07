package entity;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Administrator {
	
	@Id
	private String email;
	private String password;
	private String username;

	public Administrator() {
	}
	
	public Administrator(String email, String password, String username) {
		this.email = email;
		this.password = password;
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	private String getPassword() {
		return password;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}

