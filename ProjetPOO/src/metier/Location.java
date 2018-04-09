package metier;

/**
 * Classe définissant une location.
 * @author Sébastien
 */
public class Location {
    
    //Attributs
    
    private Integer id;
    private double abscisse;
    private double ordonnee;
    private String name;
    
    //Constructeurs

    public Location() {
        this.id = -1;
        this.abscisse = 0.0;
        this.ordonnee = 0.0;
        this.name = "Nouvelle Location";
    }

    public Location(double abscisse, double ordonnee, String name) {
        this();
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
        this.name = name;
    }

    //Accesseurs
    
    public double getAbscisse() {
        return abscisse;
    }

    public void setAbscisse(double abscisse) {
        this.abscisse = abscisse;
    }

    public double getOrdonnee() {
        return ordonnee;
    }

    public void setOrdonnee(double ordonnee) {
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
