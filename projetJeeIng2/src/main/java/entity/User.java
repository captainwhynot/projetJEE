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

/**
 * The User class represents a user entity with common attributes such as email,
 * password, username, user type, profile picture, and associations with other
 * entities like Moderator, Customer, Administrator, and Product.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorOptions(force = true)
public class User {

	/**
	 * The unique identifier for the user.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/**
	 * The email address of the user.
	 */
	private String email;

	/**
	 * The password associated with the user's account.
	 */
	private String password;

	/**
	 * The username chosen by the user.
	 */
	private String username;

	/**
	 * The type of user (e.g., Customer, Moderator, Administrator).
	 */
	private String typeUser;

	/**
	 * The path to the profile picture of the user.
	 */
	private String profilePicture;

	/**
	 * The moderator associated with the user (if applicable).
	 */
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Moderator moderator;

	/**
	 * The customer associated with the user (if applicable).
	 */
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Customer customer;

	/**
	 * The administrator associated with the user (if applicable).
	 */
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Administrator admin;

	/**
	 * The list of products associated with the user.
	 */
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Product> products;

	/**
	 * Default constructor for the User class.
	 */
	public User() {
	}

	/**
	 * Parameterized constructor to create a User with the specified email,
	 * password, username, and user type.
	 *
	 * @param email    The email address of the user.
	 * @param password The password associated with the user's account.
	 * @param username The username chosen by the user.
	 * @param typeUser The type of user (e.g., Customer, Moderator, Administrator).
	 */
	public User(String email, String password, String username, String typeUser) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.typeUser = typeUser;
		this.profilePicture = "img/profilePicture.png";
	}

	/**
	 * Gets the unique identifier for the user.
	 *
	 * @return The user's unique identifier.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the email address of the user.
	 *
	 * @return The user's email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email address for the user.
	 *
	 * @param email The new email address for the user.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the password associated with the user's account.
	 *
	 * @return The user's password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password for the user's account.
	 *
	 * @param password The new password for the user's account.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the username chosen by the user.
	 *
	 * @return The user's username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username for the user.
	 *
	 * @param username The new username for the user.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the type of user (e.g., Customer, Moderator, Administrator).
	 *
	 * @return The type of user.
	 */
	public String getTypeUser() {
		return typeUser;
	}

	/**
	 * Gets the list of products associated with the user.
	 *
	 * @return The list of products associated with the user.
	 */
	public List<Product> getProducts() {
		return products;
	}
	/**
	 * Gets the path to the profile picture of the user.
	 *
	 * @return The path to the user's profile picture.
	 */
	public String getProfilePicture() {
		return profilePicture;
	}

	/**
	 * Sets the path to the profile picture for the user.
	 *
	 * @param profilePicture The new path to the user's profile picture.
	 */
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
}
