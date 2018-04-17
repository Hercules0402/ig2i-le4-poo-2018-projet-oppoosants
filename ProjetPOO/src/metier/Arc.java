package metier;

/**
 * Classe définissant un arc.
 */
public class Arc {
    private final Integer id;
    private Location locationStart;
    private Location locationEnd;
    private Integer distance;
    private boolean isShortest;

    public Arc() {
       this.id = -1; 
    }
    
    public Arc(Location locationStart, Location locationEnd, Integer distance, boolean isShortest) {
        this();
        this.locationStart = locationStart;
        this.locationEnd = locationEnd;
        this.distance = distance;
        this.isShortest = isShortest;
    }

    public Integer getId() {
        return id;
    }

    public Location getLocationStart() {
        return locationStart;
    }

    public Location getLocationEnd() {
        return locationEnd;
    }

    public double getDistance() {
        return distance;
    }

    public boolean isIsShortest() {
        return isShortest;
    }

    public void setLocationStart(Location locationStart) {
        this.locationStart = locationStart;
    }

    public void setLocationEnd(Location locationEnd) {
        this.locationEnd = locationEnd;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public void setIsShortest(boolean isShortest) {
        this.isShortest = isShortest;
    }

    @Override
    public String toString() {
        return "Arc{" + "locationStart=" + locationStart.getName() + ", locationEnd=" + locationEnd.getName() + ", distance=" + distance + ", isShortest=" + isShortest + '}';
    }
}