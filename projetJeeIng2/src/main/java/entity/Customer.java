package entity;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class Customer extends User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int fidelityPoint;

	public Customer() {
		super();
	}
	
	public Customer(String email, String password, String username) {
		super(email, password, username);
		this.setFidelityPoint(0);
	}

	public int getFidelityPoint() {
		return fidelityPoint;
	}

	private void setFidelityPoint(int fidelityPoint) {
		this.fidelityPoint = fidelityPoint;
	}
	
}

