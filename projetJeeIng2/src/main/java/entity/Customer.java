package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Customer {
	
	private String email;
	private String password;
	private String username;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int fidelityPoint;
	
	public Customer() {
	}

	public Customer(String email, String password, String username) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.setFidelityPoint(0);
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
	
	public int getFidelityPoint() {
		return fidelityPoint;
	}

	private void setFidelityPoint(int fidelityPoint) {
		this.fidelityPoint = fidelityPoint;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}

