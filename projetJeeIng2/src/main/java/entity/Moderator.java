package entity;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class Moderator extends User implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean addProduct;
	private boolean modifyProduct;
	private boolean deleteProduct;
	
	public Moderator() {
		super();
	}
	
	public Moderator(String email, String password, String username) {
		super(email, password, username);
		this.addProduct = false;
		this.modifyProduct = false;
		this.deleteProduct = false;
	}
	
	public boolean canAddProduct() {
		return addProduct;
	}
	
	public void setAddProduct(boolean addProduct) {
		this.addProduct = addProduct;
	}
	
	public boolean canModifyProduct() {
		return modifyProduct;
	}
	
	public void setModifyProduct(boolean modifyProduct) {
		this.modifyProduct = modifyProduct;
	}
	
	public boolean canDeleteProduct() {
		return deleteProduct;
	}
	
	public void setDeleteProduct(boolean deleteProduct) {
		this.deleteProduct = deleteProduct;
	}
	
	
}

