package metier;

/**
 * Classe définissant une location.
 * @author Sébastien
 */
public class Location {
    
    //Attributs
    
    private Integer id;
    private Integer abscisse;
    private Integer ordonnee;
    private String name;
    
    //Constructeurs

    public Location() {
        this.abscisse = 0;
        this.ordonnee = 0;
        this.name = "Nouvelle Location";
    }

    public Location(Integer id, Integer abscisse, Integer ordonnee, String name) {
        this();
        this.id = id;
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
        this.name = name;
    }

    //Accesseurs
    public Integer getId() {
        return id;
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
    
    public static void main(String[] args) {
        Location loc1 = new Location();
        loc1.toString();
    }
    
    @Override
    public String toString() {
        return "metier.Location[ identifiant=" + this.id + ", abscisse=" + this.abscisse
                + ", ordonnee=" + this.ordonnee + ", nom=" + this.name + " ]";
    }
    
}
