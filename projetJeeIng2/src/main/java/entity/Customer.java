package entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class Customer extends User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int fidelityPoint;
	@OneToOne
    @JoinColumn(name = "id")
    private User user;

	public Customer() {
		super();
	}
	
	public Customer(String email, String password, String username) {
		super(email, password, username, "Customer");
		this.setFidelityPoint(0);
	}

	public int getFidelityPoint() {
		return fidelityPoint;
	}

	private void setFidelityPoint(int fidelityPoint) {
		this.fidelityPoint = fidelityPoint;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

