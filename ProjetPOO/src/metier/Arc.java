package metier;

/**
 *
 * @author nicol
 */
public class Arc {
    private final int arcId;
    private Location location_a;
    private Location location_b;
    private double distance;
    private boolean isShortest;

    public Arc(int arcId, Location location_a, Location location_b, double distance, boolean isShortest) {
        this.arcId = arcId;
        this.location_a = location_a;
        this.location_b = location_b;
        this.distance = distance;
        this.isShortest = isShortest;
    }

    public int getArcId() {
        return arcId;
    }

    public Location getLocation_a() {
        return location_a;
    }

    public Location getLocation_b() {
        return location_b;
    }

    public double getDistance() {
        return distance;
    }

    public boolean isIsShortest() {
        return isShortest;
    }

    public void setLocation_a(Location location_a) {
        this.location_a = location_a;
    }

    public void setLocation_b(Location location_b) {
        this.location_b = location_b;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setIsShortest(boolean isShortest) {
        this.isShortest = isShortest;
    }
    
    
}
