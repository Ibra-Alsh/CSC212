// A carpool ride that can carry several riders with the same driver.
public class SharedRide extends Ride implements ISharedRide {
    private LinkedList<IRider> participants;

    // the ride starts with no riders. RideSharingSystem adds them with addParticipant.
    public SharedRide(int rideId, String pickupLocation, IDateTime pickupTime, IDateTime dropoffTime,
            String dropoffLocation, IDriver driver) {
        super(rideId, pickupLocation, pickupTime, dropoffTime, dropoffLocation, driver);
        participants = new LinkedList<>();
    }

    // returns a copy of the participants so walking through it does not move this ride's own list.
    @Override
    public LinkedList<IRider> getParticipants() {
        LinkedList<IRider> copy = new LinkedList<>();
        if (participants.empty()) {
            return copy;
        }

        participants.findfirst();
        while (true) {
            copy.insert(participants.retrieve());
            if (participants.last()) {
                break;
            }
            participants.findnext();
        }
        return copy;
    }

    // adds the rider at the end of the list. Returns false if the rider is null or already on this ride.
    @Override
    public boolean addParticipant(IRider rider) {
        if (rider == null) {
            return false;
        }

        if (participants.empty()) {
            participants.insert(rider);
            return true;
        }

        // look for the same ID. If it is not found, the loop stops on the last node.
        participants.findfirst();
        while (true) {
            if (participants.retrieve().getId() == rider.getId()) {
                return false;
            }
            if (participants.last()) {
                break;
            }
            participants.findnext();
        }

        // current is the last node here, so insert puts the rider at the end.
        participants.insert(rider);
        return true;
    }

    // removes the rider with this ID. Returns false if no participant has that ID.
    @Override
    public boolean removeParticipantById(int riderId) {
        if (participants.empty()) {
            return false;
        }

        participants.findfirst();
        while (true) {
            if (participants.retrieve().getId() == riderId) {
                participants.remove();
                return true;
            }
            if (participants.last()) {
                break;
            }
            participants.findnext();
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return participants.empty();
    }

    @Override
    public boolean hasRider(int riderId) {
        if (participants.empty()) {
            return false;
        }

        participants.findfirst();
        while (true) {
            if (participants.retrieve().getId() == riderId) {
                return true;
            }
            if (participants.last()) {
                break;
            }
            participants.findnext();
        }
        return false;
    }

    @Override
    public String toString() {
        String names = "";
        if (!participants.empty()) {
            participants.findfirst();
            while (true) {
                IRider rider = participants.retrieve();
                names += rider.getName() + " (ID " + rider.getId() + ")";
                if (participants.last()) {
                    break;
                }
                names += "; ";
                participants.findnext();
            }
        }
        return "Shared " + super.toString() + ", riders: [" + names + "]";
    }
}
