package entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private double price;
	private int stock;
	private String img;
    @ManyToOne
    @JoinColumn(name = "sellerId")
    private Moderator moderator;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Basket> baskets;
	
	public Product() {
	}
	public Product(String name, double price, int stock, String img, Moderator moderator) {
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.img = img;
		this.moderator = moderator;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public Moderator getModerator() {
		return moderator;
	}
	
	public void setModerator(Moderator moderator) {
		this.moderator = moderator;
	}
	
	public List<Basket> getBaskets() {
        return baskets;
    }

    public void setBaskets(List<Basket> baskets) {
        this.baskets = baskets;
    }
    
	public String getImg() {
		return img;
	}
	
	public void setImg(String img) {
		this.img = img;
	}	
}
