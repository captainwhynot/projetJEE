package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DiscriminatorOptions;

import java.util.List;

import javax.persistence.CascadeType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorOptions(force = true)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String email;
	private String password;
	private String username;
	private String typeUser;
	private String profilePicture;
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Moderator moderator;
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Customer customer;
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Administrator admin;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
	
	public User() {
		
	}
	
	public User(String email, String password, String username, String typeUser) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.typeUser = typeUser;
		this.profilePicture = "img/profilPicture.png";
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

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
}
