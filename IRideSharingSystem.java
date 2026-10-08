
/**
 * The interface of the Ride-Sharing System.
 */
public interface IRideSharingSystem {
	// Loads riders from a CSV file.
	// Returns true if loading succeeds; false otherwise.
	boolean loadRidersFromCSV(String ridersFilePath);

	// Loads drivers from a CSV file.
	// Returns true if loading succeeds; false otherwise.
	boolean loadDriversFromCSV(String driversFilePath);

	// Loads rides from a CSV file. All referenced riders and the assigned driver must already exist. It must enforce conflict rules
	// Returns true if loading succeeds; false otherwise.
	boolean loadRidesFromCSV(String ridesFilePath);

    //Adds a rider to the system after enforcing uniqueness by ID. Returns true if added; false if duplicate ID or invalid
     boolean addRider(IRider rider);

    /**
     * Adds a driver to the system after enforcing uniqueness by ID and by
     * vehicle plate number (no two drivers may share the same plate).
     * Returns true if added; false if duplicate ID, duplicate plate, or invalid
     */
     boolean addDriver(IDriver driver);

    //Searches for a rider by ID.
    IRider searchRiderById(int riderId);

    //Searches for a rider by email.
    IRider searchRiderByEmail(String email);

    //Searches for all riders whose full name exactly matches the given name.
    LinkedList<IRider> searchRidersByName(String fullName);

    //Searches for all riders from the specified home city.
    LinkedList<IRider> searchRidersByHomeCity(String homeCity);

    //Returns all riders stored in the system.
    LinkedList<IRider> getAllRiders();

    //Searches for a driver by ID.
    IDriver searchDriverById(int driverId);

    //Searches for a driver by vehicle plate.
    IDriver searchDriverByVehiclePlate(String vehiclePlate);

    //Searches for all drivers with the specified vehicle type.
    LinkedList<IDriver> searchDriversByVehicleType(VehicleType vehicleType);

    //Returns all drivers stored in the system.
    LinkedList<IDriver> getAllDrivers();

    /**
     * Removes a rider and performs cascade deletion of associated rides:
     * - All private rides involving the rider are deleted.
     * - The rider is removed from any shared rides.
     * - Shared rides with no remaining participants are deleted.
     * It returns true if the rider was found and removed; false otherwise
     */
    boolean removeRider(int riderId);

    /**
     * Removes a driver and performs cascade deletion of their assigned rides:
     * - All private rides assigned to the driver are deleted.
     * - All shared rides assigned to the driver are deleted.
     * It returns true if the driver was found and removed; false otherwise
     */
    boolean removeDriver(int driverId);

    /**
     * Schedules a private ride for one rider with one driver.
     * Requirements:
     * - The rider must exist.
     * - The driver must exist.
     * - The ride must not conflict with the rider's current schedule.
     * - The ride must not conflict with the driver's current schedule.
     * It returns true if scheduled; false otherwise
     */
    boolean schedulePrivateRide(String pickupLocation, IDateTime pickupTime, IDateTime dropoffTime, String dropoffLocation, int riderId, int driverId);

    /**
     * Schedules a shared ride for multiple riders with one driver.
     * Requirements:
     * - All riders must exist.
     * - The driver must exist.
     * - The ride must not conflict with any listed rider's schedule.
     * - The ride must not conflict with the driver's current schedule.
     * It returns true if scheduled; false otherwise
     */
    boolean scheduleSharedRide(String pickupLocation, IDateTime pickupTime, IDateTime dropoffTime, String dropoffLocation, int[] riderIds, int driverId);

    //Searches for all rides whose pickup location matches the given location (may return multiple if pickup locations repeat).
    LinkedList<IRide> searchRidesByPickupLocation(String pickupLocation);

    //Searches for all rides that involve a rider with the given name.
    LinkedList<IRide> searchRidesByRiderName(String riderName);

    //Returns all riders registered on a shared ride identified by its pickup location.
    LinkedList<IRider> getSharedRideParticipants(String pickupLocation);

    //Returns all rides (private and shared) alphabetically ordered by pickup location.
    LinkedList<IRide> getAllRidesAlphabetically();
}
