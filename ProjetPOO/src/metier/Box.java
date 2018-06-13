package metier;

import algo.IntraTrolleyInfos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Classe définissant un colis.
 */
@Entity
public class Box implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Correspond à l'id de la ligne dans la bdd.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    /**
     * Correspond à l'id dans le fichier instance.
     */
    @Column
    private Integer idBox;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private List<ProdQty> prodQtys;

    @Column
    private static int weightMax;

    @Column
    private static int volumeMax;

    @Column
    private int weight;

    @Column
    private int volume;

    @JoinColumn(referencedColumnName = "ID")
    @ManyToOne
    private Order order;

    @JoinColumn(name = "NINSTANCE", referencedColumnName = "ID")
	@ManyToOne
	private Instance ninstance;

    public Box() {
        prodQtys = new ArrayList();
    }

    public Box(Integer id, List<ProdQty> prodQtys, int weight, int volume, Order order, Instance ninstance) {
        this.idBox = id;
        this.prodQtys = prodQtys;
        this.weight = weight;
        this.volume = volume;
        this.order = order;
        this.ninstance = ninstance;
    }

    public Box(Integer id, int weightMax, int volumeMax, Order order, int weight, int volume, Instance ninstance) {
        this.idBox = id;
        this.volumeMax = volumeMax;
        this.weightMax = weightMax;
        this.volume = volume;
        this.weight = weight;
        prodQtys = new ArrayList();
        this.order = order;
        this.ninstance = ninstance;
    }

    public int getId() {
        return id;
    }

    public int getIdBox() {
        return idBox;
    }

    public List<ProdQty> getProdQtys() {
        return prodQtys;
    }

    public Order getOrder() {
        return order;
    }

    public void setIdBox(Integer idBox) {
        this.idBox = idBox;
    }

    public void setProdQtys(List<ProdQty> prodQtys) {
        this.prodQtys = prodQtys;
    }

    public void setWeightMax(int weightMax) {
        this.weightMax = weightMax;
    }

    public void setVolumeMax(int volumeMax) {
        this.volumeMax = volumeMax;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
    
    public void clear() {
        this.prodQtys.clear();
        this.volume = 0;
        this.weight = 0;
    }

    public void addProduct(ProdQty pq) {
        addProduct(pq.getProduct(), pq.getQuantity());
    }
    
    public void addProduct(Product p, int qt) {
        this.weight += p.getWeight() * qt;
        this.volume += p.getVolume() * qt;

        for(ProdQty pq : prodQtys){
            if(pq.getProduct().equals(p)){
                pq.setQuantity(pq.getQuantity() + qt);
                return;
            }
        }
        ProdQty newPq = new ProdQty(p, qt);
        prodQtys.add(newPq);
    }

    public boolean addProducts(List<ProdQty> prodQtys){
        if (prodQtys == null) return false;
        for(ProdQty pq : this.prodQtys){
            this.addProduct(pq);
        }
        return true;
    }
    
    /**
     * Fonction permettant d'ajouter un produit dans une box, et qui retourne un booléen nécessaire à l'algorithme de ClarkeAndWright.
     * @param pq
     * @return boolean
     */
    public boolean addProductClarkeAndWright(ProdQty pq) {
        Product p = pq.getProduct();
        int qt = pq.getQuantity();
        
        if (pq == null) return false;
        
        this.weight += p.getWeight() * qt;
        this.volume += p.getVolume() * qt;
        for(ProdQty prq : prodQtys){
            if(prq.getProduct().equals(p)){
                prq.setQuantity(prq.getQuantity() + qt);
                return true;
            }
        }
            
        ProdQty newPq = new ProdQty(p, qt);
        prodQtys.add(newPq);
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.idBox);
        hash = 71 * hash + this.weight;
        hash = 71 * hash + this.volume;
        hash = 71 * hash + Objects.hashCode(this.order);
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
        final Box other = (Box) obj;
        if (this.weight != other.weight) {
            return false;
        }
        if (this.volume != other.volume) {
            return false;
        }
        if (!Objects.equals(this.idBox, other.idBox)) {
            return false;
        }
        if (!Objects.equals(this.order, other.order)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Box{" + "id=" + idBox + ", prodQtys=" + prodQtys + ", weightMax=" + weightMax + ", volumeMax=" + volumeMax + ", weight= " + weight + ", volume= " + volume + '}';
    }
}