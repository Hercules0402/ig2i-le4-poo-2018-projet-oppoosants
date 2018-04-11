package metier;

/**
 * Représente le graphe sous-jacent à la zone de pickeing.
 */
public class Graph {
    /**
     * Identifiant du dépôt de départ.
     */
    private int departingDepot;

    /**
     * Identifiant du dépôt d'arrivée.
     */
    private int arrivalDepot;

    /**
     * Représente le nombre de localisations (sans les dépôts).
     */
    private int nbLocations;

    /**
     * Représente le nombre de produits.
     */
    private int nbProducts;

    /**
     * Représente le nombre d'intersections.
     */
    private int nbVerticesIntersections;

    /**
     * Tableau représentant la liste des arcs.
     */
    private int[][] arcs;

    /**
     * Tableau représentant la liste des distances (associé à la liste des arcs).
     */
    private int[][] distances;

    /**
     * Constructeur par données.
     * @param nbVerticesIntersections TODO
     * @param arcs TODO
     * @param distances TODO
     */
    public Graph(int nbVerticesIntersections, int[][] arcs, int[][] distances) {
        this.nbLocations = distances.length;
        this.nbVerticesIntersections = nbVerticesIntersections;
        this.nbProducts = this.nbLocations - this.nbVerticesIntersections - 2;
        this.departingDepot = 0;
        this.arrivalDepot = this.nbLocations - 1;
        this.arcs = arcs;
        this.distances = distances;
    }

    public int getDepartingDepot() {
        return departingDepot;
    }

    public int getArrivalDepot() {
        return arrivalDepot;
    }

    public int getNbLocations() {
        return nbLocations;
    }

    public int getNbProducts() {
        return nbProducts;
    }

    public int getNbVerticesIntersections() {
        return nbVerticesIntersections;
    }

    public int[][] getArcs() {
        return arcs;
    }

    public int[][] getDistances() {
        return distances;
    }

    @Override
    public String toString() {
        return "Graph{" + "departingDepot=" + departingDepot + ", arrivalDepot="
                + arrivalDepot + ", nbLocations=" + nbLocations + ", nbProducts="
                + nbProducts + ", nbVerticesIntersections="
                + nbVerticesIntersections + '}';
    }
}