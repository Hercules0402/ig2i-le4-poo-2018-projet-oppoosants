package metier;

public class Arc {
    private final int arcId;
    private Location locationStart;
    private Location locationEnd;
    private Integer distance;
    private boolean isShortest;

    public Arc() {
       this.arcId = -1; 
    }
    
    public Arc(Location locationStart, Location locationEnd, Integer distance, boolean isShortest) {
        this();
        this.locationStart = locationStart;
        this.locationEnd = locationEnd;
        this.distance = distance;
        this.isShortest = isShortest;
    }

    public int getArcId() {
        return arcId;
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
        return "Arc{distance=" + distance + ", isShortest=" + isShortest + '}';
    }
}