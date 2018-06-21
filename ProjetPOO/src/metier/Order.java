package metier;

import java.util.HashMap;

/**
 * Classe définissant une commande.
 */
public class Order {
    
    private Integer id;
    private Integer m;
    private Integer nbProducts;
    private HashMap<Product, Integer> products;

    public Order(Integer id) {
        this.id = id;
        products = new HashMap();
    }

    public Order(Integer id, Integer m, Integer nbProducts, HashMap products) {
        this.id = id;
        this.m = m;
        this.nbProducts = nbProducts;
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public HashMap getProducts() {
        return products;
    }

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
}