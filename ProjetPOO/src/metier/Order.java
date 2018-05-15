package metier;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

/**
 * Classe définissant un order.
 */
@Entity
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
    private Integer id;

    @Column
    private Integer m;

    @Column
    private Integer nbProducts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
	@MapKey(name = "id")
    private HashMap<Product, Integer> products;

    /**
     * Constructeur par id de la commande Order (id sera généré dans le futur tout seul)
     * @param id
     */
    public Order(Integer id) {
        this.id = id;
        products = new HashMap();
    }

    /**
     * Constructeur par id et produits de la commande Order
     * @param id
     * @param products
     */
    public Order(Integer id, Integer m, Integer nbProducts, HashMap products) {
        this.id = id;
        this.m = m;
        this.nbProducts = nbProducts;
        this.products = products;
    }

    /**
     * getId
     * @return l'identifiant de la commande
     */
    public int getId() {
        return id;
    }

    /**
     * getProducts
     * @return le hashmap des produits dans la commande
     */
    public HashMap getProducts() {
        return products;
    }

    /**
     * setProducts
     * @param products les produits contenus dans un hashmap
     */
    public void setProducts(HashMap products) {
        this.products = products;
    }
    
    /**
     * addProduct
     * Ajoute un produit dans le hashmap. S'il est déjà présent, le nombre de produits du produit que l'on souhaitait ajouter est ajouté au nombre de produits déjà présent
     * @param product le produit ajouté
     * @param qt sa quantité
     */
    public void addProduct(Product product, int qt) {
        if (!this.products.containsKey(product)) {
            this.products.put(product, qt);
        }
        else {
            int oldQt = (int) this.products.get(product);
            this.products.put(product, oldQt + qt);
        }
    }
    
    /**
     * isItemInOrder
     * @param product le produit à rechercher
     * @return true si le produit est présent dans la commande, false sinon
     */
    public boolean isItemInOrder(Product product) {
        return this.products.containsKey(product);
    }
    
    /**
     * removeProduct
     * Supprime un produit du hashmap
     * @param product le produit à supprimer
     */
    public void removeProduct(Product product) {
        if (this.products.containsKey(product)) {
            this.products.remove(product);
        }
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id+ ", m=" + m + ", nbProducts=" + nbProducts + ", products=" + products + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.m);
        hash = 37 * hash + Objects.hashCode(this.nbProducts);
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
        final Order other = (Order) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.m, other.m)) {
            return false;
        }
        if (!Objects.equals(this.nbProducts, other.nbProducts)) {
            return false;
        }
        return true;
    }

        
}