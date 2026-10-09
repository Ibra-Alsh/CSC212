public abstract class Ride implements IRide {
    private final int rideId;
    private String pickupLocation;
    private final IDateTime pickupTime;
    private final IDateTime dropoffTime;
    private String dropoffLocation;
    private IDriver driver;

    public Ride(int rideId, String pickupLocation, IDateTime pickupTime, IDateTime dropoffTime, String dropoffLocation, IDriver driver) {
        this.rideId = rideId;
        this.pickupLocation = pickupLocation;
        this.pickupTime = pickupTime;
        this.dropoffTime = dropoffTime;
        this.dropoffLocation = dropoffLocation;
        this.driver = driver;
    }

    public int getRideId() {
        return rideId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public IDateTime getPickupTime() {
        return pickupTime;
    }

    public IDateTime getDropoffTime() {
        return dropoffTime;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public IDriver getDriver() {
        return driver;
    }

    public void setDriver(IDriver driver) {
        this.driver = driver;
    }

    public abstract boolean hasRider(int riderId);

    @Override
    public String toString() {
        return "Ride: " + rideId + ", pickupLocation: " + pickupLocation + ", pickupTime: " + pickupTime.format() + ", dropoffTime: " + dropoffTime.format() + ", dropoffLocation: " + dropoffLocation + ", driver: " + driver;
    }

    
    public int compareTo(IRide other) {
        return this.pickupLocation.compareToIgnoreCase(other.getPickupLocation());
    }
}
