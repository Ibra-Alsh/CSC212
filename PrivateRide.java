// A ride booked by exactly one rider.
public class PrivateRide extends Ride implements IPrivateRide {
    private IRider rider;

    public PrivateRide(int rideId, String pickupLocation, IDateTime pickupTime, IDateTime dropoffTime,
            String dropoffLocation, IDriver driver, IRider rider) {
        super(rideId, pickupLocation, pickupTime, dropoffTime, dropoffLocation, driver);
        this.rider = rider;
    }

    @Override
    public IRider getRider() {
        return rider;
    }

    @Override
    public void setRider(IRider rider) {
        this.rider = rider;
    }

    // a private ride has one rider, so only that rider's ID can match.
    @Override
    public boolean hasRider(int riderId) {
        return rider != null && rider.getId() == riderId;
    }

    @Override
    public String toString() {
        String riderInfo = (rider == null) ? "none" : rider.getName() + " (ID " + rider.getId() + ")";
        return "Private " + super.toString() + ", rider: " + riderInfo;
    }
}
