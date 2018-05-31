package metier;

import java.io.Serializable;
import static java.lang.Math.pow;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Classe définissant une location.
 */
@Entity
public class Location implements Serializable, Comparable<Location> {
    private static final long serialVersionUID = 1L;

    /**
     * id corespondant à l'id de la ligne dans le bdd.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Correspond à l'id dans le fichier instance.
     */
    @Column
    private Integer idLocation;

    @Column
    private Integer abscisse;

    @Column
    private Integer ordonnee;

    @Column
    private String name;

    @JoinColumn(name = "NINSTANCE", referencedColumnName = "ID")
	@ManyToOne
	private Instance ninstance;

    private Map<Location,Integer> distances;
    
    public Location() {
        this.abscisse = 0;
        this.ordonnee = 0;
        this.name = "Nouvelle Location";
        distances = new HashMap<>();
    }

    public Location(Integer id, Integer abscisse, Integer ordonnee, String name,Instance ninstance) {
        this();
        this.idLocation = id;
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
        this.name = name;
        this.ninstance = ninstance;
    }

    public Location(Integer abscisse, Integer ordonnee, String name,Instance ninstance) {
        this();
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
        this.name = name;
        this.ninstance = ninstance;
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdLocation() {
        return idLocation;
    }
    
    public double getAbscisse() {
        return abscisse;
    }

    public void setAbscisse(Integer abscisse) {
        this.abscisse = abscisse;
    }

    public double getOrdonnee() {
        return ordonnee;
    }

    public void setOrdonnee(Integer ordonnee) {
        this.ordonnee = ordonnee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Location, Integer> getDistances() {
        return distances;
    }

    public boolean addDistance(Location loc, Integer distance){
        if(!distances.containsKey(loc)){
            distances.put(loc, distance);
            if(distances.containsKey(loc)){
                return true;
            } else {
                System.out.println("[ERREUR] addDistance, loc non ajoutée");
                return false;
            }
        } else {
            System.out.println("[ERREUR] addDistance, les distances de " + this.getName() + " contiennent déja " + loc.getName());
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.idLocation);
        hash = 67 * hash + Objects.hashCode(this.abscisse);
        hash = 67 * hash + Objects.hashCode(this.ordonnee);
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.ninstance);
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
        final Location other = (Location) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.idLocation, other.idLocation)) {
            return false;
        }
        if (!Objects.equals(this.abscisse, other.abscisse)) {
            return false;
        }
        if (!Objects.equals(this.ordonnee, other.ordonnee)) {
            return false;
        }
        if (!Objects.equals(this.ninstance, other.ninstance)) {
            return false;
        }
        return true;
    }

    public int compareTo(Location otherLoc) {
        if(otherLoc == null) return -1;
		return (this.getIdLocation() - otherLoc.getIdLocation());
	}	
     
    @Override
    public String toString() {
        return "Location{" + "idLocation=" + idLocation + ", abscisse=" + abscisse + ", ordonnee=" + ordonnee + ", name=" + name + '}';
    }
}