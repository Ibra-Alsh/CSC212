public class RideList implements IRideList {
    private LinkedList<IRide> rides;

    public RideList() {
        rides = new LinkedList<>();
    }

    @Override 
    public boolean addRide(IRide ride) {
        if (ride == null) {
            return false;
        }
 
        rides.findfirst();
        while (!rides.empty()) {
            IRide current = rides.retrieve();

            if (ride.getRideId() == current.getRideId()) {
                return false; // Ride with the same ID already exists
            }

            if (rides.last()) {
                break;
            }

            rides.findnext();
        }

        rides.findfirst();
        while (!rides.empty()) {
            IRide current = rides.retrieve();

            if (ride.compareTo(current) < 0) {
                rides.update(ride);
                rides.insert(current);
                return true;
            }

            if (rides.last()) {
                break;
            }

            rides.findnext();
        }

        rides.insert(ride);
        return true;
    }
    
    @Override   
    public boolean removeRideById(int rideId) {
        rides.findfirst();
        while (!rides.empty()) {
            IRide current = rides.retrieve();

            if (current.getRideId() == rideId) {
                rides.remove();
                return true;
            }

            if (rides.last()) {
                break;
            }

            rides.findnext();
        }
        return false;
    }


    // returns a copy of the already ordered list so changes don't affect the original list.
    @Override
    public LinkedList<IRide> getAllAlphabetically() {

        LinkedList<IRide> orderedRides = new LinkedList<>();

        rides.findfirst();

        while (!rides.empty()) {
            orderedRides.insert(rides.retrieve());

            if (rides.last()) {
                break;
            }

            rides.findnext();
        }
        
        return orderedRides;
    }

    @Override
    public LinkedList<IRide> findByPickupLocation(String pickupLocation) {
        LinkedList<IRide> matchingRides = new LinkedList<>();
        rides.findfirst();
        while (!rides.empty()) {
            IRide current = rides.retrieve();

            if (current.getPickupLocation().equalsIgnoreCase(pickupLocation)) {
                matchingRides.insert(current);
            }

            if (rides.last()) {
                break;
            }

            rides.findnext();
        }
        return matchingRides;
    }

    @Override 
    public LinkedList<IRide> findByRiderName(String riderFullName) {
        LinkedList<IRide> matchingRides = new LinkedList<>();

        rides.findfirst();
        while (!rides.empty()) {
            IRide current = rides.retrieve();
            boolean matches = false;

            if (current instanceof IPrivateRide) {
                IPrivateRide privateRide = (IPrivateRide) current;
                matches = privateRide.getRider().getName()
                        .equals(riderFullName);

            } else if (current instanceof ISharedRide) {
                ISharedRide sharedRide = (ISharedRide) current;
                LinkedList<IRider> participants =
                        sharedRide.getParticipants();

                participants.findfirst();
                while (!participants.empty()) {
                    if (participants.retrieve().getName()
                            .equals(riderFullName)) {
                        matches = true;
                        break;
                    }

                    if (participants.last()) {
                        break;
                    }
                    participants.findnext();
                }
            }

            if (matches) {
                matchingRides.insert(current);
            }

            if (rides.last()) {
                break;
            }
            rides.findnext();
        }

        return matchingRides;
    }
    
    @Override
    public int size() {
        int count = 0;
        rides.findfirst();

        while (!rides.empty()) {
            count++;
            if (rides.last()) {
                break;
            }
            rides.findnext();
        }
        return count;
    }
}
