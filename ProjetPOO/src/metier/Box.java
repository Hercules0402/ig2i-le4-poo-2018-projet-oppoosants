package metier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    
    @OneToMany
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

    public Box() {        
        prodQtys = new ArrayList();
    }

    public Box(Integer id, List<ProdQty> prodQtys, int weight, int volume, Order order) {
        this.id = id;
        this.prodQtys = prodQtys;
        this.weight = weight;
        this.volume = volume;
        this.order = order;
    }

    public Box(Integer id, int weightMax, int volumeMax, Order order, int weight, int volume) {
        this.id = id;
        this.volumeMax = volumeMax;
        this.weightMax = weightMax;
        this.volume = volume;
        this.weight = weight;
        prodQtys = new ArrayList();
        this.order = order;
    }

    public Box(Integer id, HashMap prodQtys, int weightMax, int volumeMax, Order order) {
        this.id = id;
        this.prodQtys = new ArrayList();
        this.volumeMax = volumeMax;
        this.weightMax = weightMax;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public List<ProdQty> getProdQtys() {
        return prodQtys;
    }

    public Order getOrder() {
        return order;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.order, other.order)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Box{" + "id=" + id + ", prodQtys=" + prodQtys + ", weightMax=" + weightMax + ", volumeMax=" + volumeMax + ", weight= " + weight + ", volume= " + volume + '}';
    }
}