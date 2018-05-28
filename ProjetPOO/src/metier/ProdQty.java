package metier;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Classe associant un produit à une quantité, utilisée pour les commandes et les colis.
 */
@Entity
public class ProdQty implements Serializable, Comparable<ProdQty>{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne()
    private Product product;

    @Column
    private Integer quantity;
    
    public ProdQty() {
        id = -1;
    }

    public ProdQty(Integer id, Product product, Integer quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public ProdQty(Product product, Integer quantity) {
        this();
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.product);
        hash = 79 * hash + Objects.hashCode(this.quantity);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProdQty other = (ProdQty) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        if (!Objects.equals(this.quantity, other.quantity)) {
            return false;
        }
        return true;
    } 

    public int compareTo(ProdQty otherPq) {
        if(otherPq == null) return -1;

        int loc = this.product.getLoc().getIdLocation();
		int otherLoc = otherPq.getProduct().getLoc().getIdLocation(); 

		return (loc - otherLoc);
	}	

    @Override
    public String toString() {
        return "ProdQty{" + "id=" + id + ", product=" + product + ", quantity=" + quantity + '}';
    }
}