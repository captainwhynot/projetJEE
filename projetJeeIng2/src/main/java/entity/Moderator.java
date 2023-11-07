package entity;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Moderator {

	@Id
	private String email;
	private String password;
	private String username;
	private boolean addProduct;
	private boolean modifyProduct;
	private boolean deleteProduct;
	
	public Moderator() {
	}
	
	public Moderator(String email, String password, String username) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.addProduct = false;
		this.modifyProduct = false;
		this.deleteProduct = false;
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
	public boolean isAddProduct() {
		return addProduct;
	}
	public void setAddProduct(boolean addProduct) {
		this.addProduct = addProduct;
	}
	public boolean isModifyProduct() {
		return modifyProduct;
	}
	public void setModifyProduct(boolean modifyProduct) {
		this.modifyProduct = modifyProduct;
	}
	public boolean isDeleteProduct() {
		return deleteProduct;
	}
	public void setDeleteProduct(boolean deleteProduct) {
		this.deleteProduct = deleteProduct;
	}
	
	
}

