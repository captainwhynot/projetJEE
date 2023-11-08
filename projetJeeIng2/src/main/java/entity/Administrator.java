package entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Administrator extends User implements Serializable {
	
	public Administrator() {
		super();
	}
	
	public Administrator(String email, String password, String username) {
		super(email, password, username);
	}
}

