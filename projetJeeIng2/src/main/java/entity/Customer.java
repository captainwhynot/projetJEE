package entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Customer extends User implements Serializable {
	
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

