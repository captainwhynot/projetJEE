package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Moderator extends User implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean addProduct;
	private boolean modifyProduct;
	private boolean deleteProduct;
	@OneToOne
    @JoinColumn(name = "id")
    private User user;
	@OneToMany(mappedBy = "moderator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
	
	public Moderator() {
		super();
	}
	
	public Moderator(String email, String password, String username) {
		super(email, password, username, "Moderator");
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}

