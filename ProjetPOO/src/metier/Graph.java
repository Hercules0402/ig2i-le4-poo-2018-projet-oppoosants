package metier;

/**
 * Représente le graphe sous-jacent à la zone de pickeing.
 */
public class Graph {

    private int departingDepot;
    private int arrivalDepot;
    private int nbLocations;
    private int nbProducts;
    private int nbVerticesIntersections;
    private int[][] arcs;
    private int[][] distances;

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