/**
	* Represents a shared ride (carpool) involving multiple riders.
*/
public interface ISharedRide extends IRide {

// Returns the list of shared ride participants.
LinkedList<IRider> getParticipants();

// Adds a rider to the shared ride.
boolean addParticipant(IRider rider);

// Removes a rider from the shared ride by ID.
boolean removeParticipantById(int riderId);

// Returns true if the shared ride has no participants.
boolean isEmpty();
}
