package metier;

import java.util.HashMap;

/**
 * Class Order
 * @author nicol
 */
public class Order {
    private int nbProduct;
    private int idOrder;
    private HashMap products;

    /**
     * Constructeur par id de la commande Order (id sera généré dans le futur tout seul)
     * @param idOrder
     */
    public Order(int idOrder) {
        this.idOrder = idOrder;
        products = new HashMap<Integer, Integer>();
        nbProduct = 0;
    }

    /**
     * Constructeur par id et produits de la commande Order
     * @param idOrder
     * @param products
     */
    public Order(int idOrder, HashMap products) {
        this.idOrder = idOrder;
        this.products = products;
        this.nbProduct = this.products.size();
    }

    /**
     * getNbProduct
     * @return le nombre de produits dans la commande
     */
    public int getNbProduct() {
        return nbProduct;
    }

    /**
     * getIdOrder
     * @return l'identifiant de la commande
     */
    public int getIdOrder() {
        return idOrder;
    }

    /**
     * getProducts
     * @return le hashmap des produits dans la commande
     */
    public HashMap getProducts() {
        return products;
    }

    /**
     * setNbProduct
     * @param nbProduct le nombre de produits
     */
    public void setNbProduct(int nbProduct) {
        this.nbProduct = nbProduct;
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
     * @param idProduct l'identifiant du produit ajouté
     * @param qt sa quantité
     */
    public void addProduct(int idProduct, int qt) {
        if (!this.products.containsKey(idProduct)) {
            this.products.put(idProduct, qt);
        }
        else {
            int oldQt = (int) this.products.get(idProduct);
            this.products.put(idProduct, oldQt + qt);
        }
    }
    
    /**
     * isItemInOrder
     * @param idProduct l'id du produit à rechercher
     * @return true si le produit est présent dans la commande, false sinon
     */
    public boolean isItemInOrder(int idProduct) {
        return this.products.containsKey(idProduct);
    }
    
    /**
     * removeProduct
     * Supprime un produit du hashmap
     * @param idProduct l'id du produit à supprimer
     */
    public void removeProduct(int idProduct) {
        if (this.products.containsKey(idProduct)) {
            this.products.remove(idProduct);
        }
    }

    @Override
    public String toString() {
        return "Order{" + "nbProduct=" + nbProduct + ", idOrder=" + idOrder + ", products=" + products + '}';
    }
    
    
}
