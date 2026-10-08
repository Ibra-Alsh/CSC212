/**
	* Represents a private ride involving exactly one rider.
*/
public interface IPrivateRide extends IRide {

// Returns the rider assigned to this private ride.
IRider getRider();

// Sets the rider assigned to this private ride.
void setRider(IRider rider);
}
