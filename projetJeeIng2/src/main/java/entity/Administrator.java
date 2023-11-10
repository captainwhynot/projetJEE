package entity;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class Administrator extends User implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public Administrator() {
		super();
	}
	
	public Administrator(String email, String password, String username) {
		super(email, password, username);
	}
}

