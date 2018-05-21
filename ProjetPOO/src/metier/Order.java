package metier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Classe définissant un order.
 */
@Entity
@Table(name = "ORD")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
    private Integer id;

    @Column
    private Integer m;

    @Column
    private Integer nbProducts;

    @OneToMany()
    private List<ProdQty> prodQtys;

    public Order() {        
        prodQtys = new ArrayList();
    }

    
    /**
     * Constructeur par id de la commande Order (id sera généré dans le futur tout seul)
     * @param id
     */
    public Order(Integer id) {
        this.id = id;
        prodQtys = new ArrayList();
    }

    /**
     * Constructeur par id et produits de la commande Order
     * @param id
     * @param prodQtys
     */
    public Order(Integer id, Integer m, Integer nbProducts, ArrayList<ProdQty> prodQtys) {
        this.id = id;
        this.m = m;
        this.nbProducts = nbProducts;
        this.prodQtys = new ArrayList<>(prodQtys);
    }
    
    public Order(Integer m, Integer nbProducts, ArrayList<ProdQty> prodQtys) {
        this.m = m;
        this.nbProducts = nbProducts;
        this.prodQtys = new ArrayList<>(prodQtys);
    }

    /**
     * getId
     * @return l'identifiant de la commande
     */
    public int getId() {
        return id;
    }

    /**
     * getProdQtys
     * @return le hashmap des produits dans la commande
     */
    public List<ProdQty> getProdQtys() {
        return prodQtys;
    }

    /**
     * setProdQtys
     * @param prodQtys les produits contenus dans un hashmap
     */
    public void setProdQtys(List<ProdQty> prodQtys) {
        this.prodQtys = prodQtys;
    }
    
    /**
     * addProduct
     * Ajoute un produit dans le hashmap. S'il est déjà présent, le nombre de produits du produit que l'on souhaitait ajouter est ajouté au nombre de produits déjà présent
     * @param product le produit ajouté
     * @param qt sa quantité
     */
    /* A REFAIRE
    public void addProduct(Product product, int qt) {
        if (!this.prodQtys.containsKey(product)) {
            this.prodQtys.put(product, qt);
        }
        else {
            int oldQt = (int) this.prodQtys.get(product);
            this.prodQtys.put(product, oldQt + qt);
        }
    }
    */
    
    /**
     * isItemInOrder
     * @param product le produit à rechercher
     * @return true si le produit est présent dans la commande, false sinon
     */
    /* A REFAIRE
    public boolean isItemInOrder(Product product) {
        return this.prodQtys.containsKey(product);
    }
    */
    
    /**
     * removeProduct
     * Supprime un produit du hashmap
     * @param product le produit à supprimer
     */
    /* A REFAIRE
    public void removeProduct(Product product) {
        if (this.prodQtys.containsKey(product)) {
            this.prodQtys.remove(product);
        }
    }*/

    @Override
    public String toString() {
        return "Order{" + "id=" + id+ ", m=" + m + ", nbProducts=" + nbProducts + ", prodQtys=" + prodQtys + '}';
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