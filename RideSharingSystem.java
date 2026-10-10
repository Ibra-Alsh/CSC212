import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Connects the rider list, driver list and ride list, and enforces the system rules:
// unique IDs and plates, existence checks, no overlapping rides, and cascade removal.
// Nothing here prints. Every search returns its result so Main can print it.
public class RideSharingSystem implements IRideSharingSystem {
    private IRiderList riders;
    private IDriverList drivers;
    private IRideList rides;
    private int nextRideId; // the system gives each new ride the next ID, starting at 1

    public RideSharingSystem() {
        riders = new RiderList();
        drivers = new DriverList();
        rides = new RideList();
        nextRideId = 1;
    }

    // ---------- loading from CSV ----------

    // each line: riderId,name,email,phoneNumber,homeCity
    // a bad or duplicate line is skipped and the rest are still loaded.
    // returns false if the file can't be read or any line was skipped.
    @Override
    public boolean loadRidersFromCSV(String ridersFilePath) {
        if (ridersFilePath == null) {
            return false;
        }

        boolean allAdded = true;
        try (BufferedReader reader = new BufferedReader(new FileReader(ridersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length != 5) {
                    allAdded = false;
                    continue;
                }

                try {
                    int id = Integer.parseInt(fields[0].trim());
                    // the CSV has email before phone, but the Rider constructor takes phone first
                    IRider rider = new Rider(id, fields[1].trim(), fields[3].trim(), fields[2].trim(), fields[4].trim());
                    if (!addRider(rider)) {
                        allAdded = false;
                    }
                } catch (IllegalArgumentException e) {
                    // bad ID number or bad phone number
                    allAdded = false;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return allAdded;
    }

    // each line: driverId,name,phoneNumber,vehiclePlate,vehicleType
    // same rules as loadRidersFromCSV.
    @Override
    public boolean loadDriversFromCSV(String driversFilePath) {
        if (driversFilePath == null) {
            return false;
        }

        boolean allAdded = true;
        try (BufferedReader reader = new BufferedReader(new FileReader(driversFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length != 5) {
                    allAdded = false;
                    continue;
                }

                try {
                    int id = Integer.parseInt(fields[0].trim());
                    VehicleType type = VehicleType.valueOf(fields[4].trim());
                    IDriver driver = new Driver(id, fields[1].trim(), fields[2].trim(), fields[3].trim(), type);
                    if (!addDriver(driver)) {
                        allAdded = false;
                    }
                } catch (IllegalArgumentException e) {
                    // bad ID, phone, plate or vehicle type
                    allAdded = false;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return allAdded;
    }

    // each line: rideType,pickupLocation,pickupDateTime,dropoffDateTime,dropoffLocation,driverId,riderIds
    // rider IDs are separated by ';'. Every line goes through schedulePrivateRide or scheduleSharedRide,
    // so the existence and conflict rules apply to loaded rides too.
    @Override
    public boolean loadRidesFromCSV(String ridesFilePath) {
        if (ridesFilePath == null) {
            return false;
        }

        boolean allAdded = true;
        try (BufferedReader reader = new BufferedReader(new FileReader(ridesFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length != 7) {
                    allAdded = false;
                    continue;
                }

                try {
                    String rideType = fields[0].trim();
                    String pickupLocation = fields[1].trim();
                    IDateTime pickupTime = parseDateTime(fields[2]);
                    IDateTime dropoffTime = parseDateTime(fields[3]);
                    String dropoffLocation = fields[4].trim();
                    int driverId = Integer.parseInt(fields[5].trim());

                    String[] idParts = fields[6].split(";");
                    int[] riderIds = new int[idParts.length];
                    for (int i = 0; i < idParts.length; i++) {
                        riderIds[i] = Integer.parseInt(idParts[i].trim());
                    }

                    boolean added;
                    if (rideType.equalsIgnoreCase("PRIVATE") && riderIds.length == 1) {
                        added = schedulePrivateRide(pickupLocation, pickupTime, dropoffTime, dropoffLocation,
                                riderIds[0], driverId);
                    } else if (rideType.equalsIgnoreCase("SHARED")) {
                        added = scheduleSharedRide(pickupLocation, pickupTime, dropoffTime, dropoffLocation,
                                riderIds, driverId);
                    } else {
                        added = false; // unknown type, or a private ride with more than one rider
                    }

                    if (!added) {
                        allAdded = false;
                    }
                } catch (IllegalArgumentException e) {
                    // bad number or bad date/time
                    allAdded = false;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return allAdded;
    }

    // turns "MM/DD/YYYY HH:MM" into a DateTime.
    // throws IllegalArgumentException if the text is not in that format or a value is out of range.
    // it is public so Main can use it when the user types a date.
    public static IDateTime parseDateTime(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Date/time is missing");
        }

        String[] dateAndTime = text.trim().split("\\s+");
        if (dateAndTime.length != 2) {
            throw new IllegalArgumentException("Date/time must look like MM/DD/YYYY HH:MM");
        }

        String[] date = dateAndTime[0].split("/");
        String[] time = dateAndTime[1].split(":");
        if (date.length != 3 || time.length != 2) {
            throw new IllegalArgumentException("Date/time must look like MM/DD/YYYY HH:MM");
        }

        int month = Integer.parseInt(date[0]);
        int day = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[2]);
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);

        if (month < 1 || month > 12 || day < 1 || day > 31 || hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Date/time value out of range: " + text);
        }
        return new DateTime(year, month, day, hour, minute);
    }

    // ---------- riders and drivers ----------

    // fails if the rider is null, or the ID or email is already used.
    @Override
    public boolean addRider(IRider rider) {
        if (rider == null) {
            return false;
        }
        if (riders.findById(rider.getId()) != null) {
            return false;
        }
        if (rider.getEmail() != null && riders.findByEmail(rider.getEmail()) != null) {
            return false;
        }
        return riders.add(rider);
    }

    // fails if the driver is null, or the ID or vehicle plate is already used.
    @Override
    public boolean addDriver(IDriver driver) {
        if (driver == null) {
            return false;
        }
        if (drivers.findById(driver.getId()) != null) {
            return false;
        }
        if (drivers.findByVehiclePlate(driver.getVehiclePlate()) != null) {
            return false;
        }
        return drivers.add(driver);
    }

    @Override
    public IRider searchRiderById(int riderId) {
        return riders.findById(riderId);
    }

    @Override
    public IRider searchRiderByEmail(String email) {
        return riders.findByEmail(email);
    }

    @Override
    public LinkedList<IRider> searchRidersByName(String fullName) {
        return riders.findByName(fullName);
    }

    @Override
    public LinkedList<IRider> searchRidersByHomeCity(String homeCity) {
        return riders.findByHomeCity(homeCity);
    }

    @Override
    public LinkedList<IRider> getAllRiders() {
        return riders.getAll();
    }

    @Override
    public IDriver searchDriverById(int driverId) {
        return drivers.findById(driverId);
    }

    @Override
    public IDriver searchDriverByVehiclePlate(String vehiclePlate) {
        return drivers.findByVehiclePlate(vehiclePlate);
    }

    @Override
    public LinkedList<IDriver> searchDriversByVehicleType(VehicleType vehicleType) {
        return drivers.findByVehicleType(vehicleType);
    }

    @Override
    public LinkedList<IDriver> getAllDrivers() {
        return drivers.getAll();
    }

    // ---------- removal with cascade ----------

    // private rides of this rider are deleted. On shared rides the rider is taken off,
    // and a shared ride left with no riders is deleted.
    @Override
    public boolean removeRider(int riderId) {
        IRider rider = riders.findById(riderId);
        if (rider == null) {
            return false;
        }

        // walk a copy of the rides, because deleteRide removes nodes from the real list
        LinkedList<IRide> allRides = rides.getAllAlphabetically();
        if (!allRides.empty()) {
            allRides.findfirst();
            while (true) {
                IRide ride = allRides.retrieve();

                if (ride.hasRider(riderId)) {
                    if (ride instanceof ISharedRide) {
                        ISharedRide sharedRide = (ISharedRide) ride;
                        sharedRide.removeParticipantById(riderId);
                        removeFromHistory(rider.getRideHistory(), ride.getRideId());
                        if (sharedRide.isEmpty()) {
                            deleteRide(ride);
                        }
                    } else {
                        deleteRide(ride);
                    }
                }

                if (allRides.last()) {
                    break;
                }
                allRides.findnext();
            }
        }

        return riders.removeById(riderId);
    }

    // every ride (private or shared) assigned to this driver is deleted.
    @Override
    public boolean removeDriver(int driverId) {
        IDriver driver = drivers.findById(driverId);
        if (driver == null) {
            return false;
        }

        // walk a copy of the rides, because deleteRide removes nodes from the real list
        LinkedList<IRide> allRides = rides.getAllAlphabetically();
        if (!allRides.empty()) {
            allRides.findfirst();
            while (true) {
                IRide ride = allRides.retrieve();

                if (ride.getDriver() != null && ride.getDriver().getId() == driverId) {
                    deleteRide(ride);
                }

                if (allRides.last()) {
                    break;
                }
                allRides.findnext();
            }
        }

        return drivers.removeById(driverId);
    }

    // ---------- scheduling ----------

    @Override
    public boolean schedulePrivateRide(String pickupLocation, IDateTime pickupTime, IDateTime dropoffTime,
            String dropoffLocation, int riderId, int driverId) {
        if (!validRideDetails(pickupLocation, pickupTime, dropoffTime, dropoffLocation)) {
            return false;
        }

        IRider rider = riders.findById(riderId);
        IDriver driver = drivers.findById(driverId);
        if (rider == null || driver == null) {
            return false;
        }

        if (hasConflict(rider.getRideHistory(), pickupTime, dropoffTime)
                || hasConflict(driver.getRideHistory(), pickupTime, dropoffTime)) {
            return false;
        }

        IPrivateRide ride = new PrivateRide(nextRideId, pickupLocation, pickupTime, dropoffTime, dropoffLocation,
                driver, rider);
        if (!rides.addRide(ride)) {
            return false;
        }
        nextRideId++;

        addToHistory(rider, ride);
        addToHistory(driver, ride);
        return true;
    }

    // needs at least two different riders. Every check runs before anything is changed,
    // so a rejected ride leaves the lists and histories as they were.
    @Override
    public boolean scheduleSharedRide(String pickupLocation, IDateTime pickupTime, IDateTime dropoffTime,
            String dropoffLocation, int[] riderIds, int driverId) {
        if (!validRideDetails(pickupLocation, pickupTime, dropoffTime, dropoffLocation)) {
            return false;
        }
        if (riderIds == null || riderIds.length < 2) {
            return false;
        }

        IDriver driver = drivers.findById(driverId);
        if (driver == null || hasConflict(driver.getRideHistory(), pickupTime, dropoffTime)) {
            return false;
        }

        IRider[] rideRiders = new IRider[riderIds.length];
        for (int i = 0; i < riderIds.length; i++) {
            // the same rider listed twice
            for (int j = 0; j < i; j++) {
                if (riderIds[j] == riderIds[i]) {
                    return false;
                }
            }

            IRider rider = riders.findById(riderIds[i]);
            if (rider == null || hasConflict(rider.getRideHistory(), pickupTime, dropoffTime)) {
                return false;
            }
            rideRiders[i] = rider;
        }

        ISharedRide ride = new SharedRide(nextRideId, pickupLocation, pickupTime, dropoffTime, dropoffLocation, driver);
        for (int i = 0; i < rideRiders.length; i++) {
            ride.addParticipant(rideRiders[i]);
        }
        if (!rides.addRide(ride)) {
            return false;
        }
        nextRideId++;

        addToHistory(driver, ride);
        for (int i = 0; i < rideRiders.length; i++) {
            addToHistory(rideRiders[i], ride);
        }
        return true;
    }

    // ---------- ride searches ----------

    @Override
    public LinkedList<IRide> searchRidesByPickupLocation(String pickupLocation) {
        return rides.findByPickupLocation(pickupLocation);
    }

    @Override
    public LinkedList<IRide> searchRidesByRiderName(String riderName) {
        return rides.findByRiderName(riderName);
    }

    // pickup locations can repeat, so this collects the riders of every shared ride at that location.
    // a rider on two of those rides is listed once. Private rides are skipped.
    @Override
    public LinkedList<IRider> getSharedRideParticipants(String pickupLocation) {
        LinkedList<IRider> result = new LinkedList<>();
        LinkedList<IRide> matches = rides.findByPickupLocation(pickupLocation);
        if (matches.empty()) {
            return result;
        }

        matches.findfirst();
        while (true) {
            IRide ride = matches.retrieve();

            if (ride instanceof ISharedRide) {
                LinkedList<IRider> participants = ((ISharedRide) ride).getParticipants();
                if (!participants.empty()) {
                    participants.findfirst();
                    while (true) {
                        addRiderOnce(result, participants.retrieve());
                        if (participants.last()) {
                            break;
                        }
                        participants.findnext();
                    }
                }
            }

            if (matches.last()) {
                break;
            }
            matches.findnext();
        }
        return result;
    }

    @Override
    public LinkedList<IRide> getAllRidesAlphabetically() {
        return rides.getAllAlphabetically();
    }

    // ---------- helpers ----------

    // locations must not be empty and the pickup must come before the drop-off.
    private boolean validRideDetails(String pickupLocation, IDateTime pickupTime, IDateTime dropoffTime,
            String dropoffLocation) {
        if (pickupLocation == null || pickupLocation.trim().isEmpty()) {
            return false;
        }
        if (dropoffLocation == null || dropoffLocation.trim().isEmpty()) {
            return false;
        }
        if (pickupTime == null || dropoffTime == null) {
            return false;
        }
        return pickupTime.compareTo(dropoffTime) < 0;
    }

    // true if the time range [pickupTime, dropoffTime) overlaps any ride in the schedule.
    // two ranges overlap when pickup1 < dropoff2 and pickup2 < dropoff1 (the rule in the project PDF).
    private boolean hasConflict(LinkedList<IRide> schedule, IDateTime pickupTime, IDateTime dropoffTime) {
        if (schedule.empty()) {
            return false;
        }

        schedule.findfirst();
        while (true) {
            IRide ride = schedule.retrieve();
            if (pickupTime.compareTo(ride.getDropoffTime()) < 0 && ride.getPickupTime().compareTo(dropoffTime) < 0) {
                return true;
            }
            if (schedule.last()) {
                return false;
            }
            schedule.findnext();
        }
    }

    // puts the ride right after the head of the person's history.
    // the order inside a history does not matter, so there is no need to walk to the end.
    private void addToHistory(IPerson person, IRide ride) {
        LinkedList<IRide> history = person.getRideHistory();
        history.findfirst();
        history.insert(ride);
    }

    // removes the ride with this ID from a history list, if it is there.
    private void removeFromHistory(LinkedList<IRide> history, int rideId) {
        if (history.empty()) {
            return;
        }

        history.findfirst();
        while (true) {
            if (history.retrieve().getRideId() == rideId) {
                history.remove();
                return;
            }
            if (history.last()) {
                return;
            }
            history.findnext();
        }
    }

    // removes the ride from the ride list and from the history of its driver and of every rider on it.
    private void deleteRide(IRide ride) {
        int rideId = ride.getRideId();
        rides.removeRideById(rideId);

        if (ride.getDriver() != null) {
            removeFromHistory(ride.getDriver().getRideHistory(), rideId);
        }

        if (ride instanceof IPrivateRide) {
            IRider rider = ((IPrivateRide) ride).getRider();
            if (rider != null) {
                removeFromHistory(rider.getRideHistory(), rideId);
            }
        } else if (ride instanceof ISharedRide) {
            LinkedList<IRider> participants = ((ISharedRide) ride).getParticipants();
            if (!participants.empty()) {
                participants.findfirst();
                while (true) {
                    removeFromHistory(participants.retrieve().getRideHistory(), rideId);
                    if (participants.last()) {
                        break;
                    }
                    participants.findnext();
                }
            }
        }
    }

    // adds the rider to the end of the list unless a rider with the same ID is already there.
    private void addRiderOnce(LinkedList<IRider> list, IRider rider) {
        if (list.empty()) {
            list.insert(rider);
            return;
        }

        list.findfirst();
        while (true) {
            if (list.retrieve().getId() == rider.getId()) {
                return;
            }
            if (list.last()) {
                break;
            }
            list.findnext();
        }
        // current is the last node here, so insert puts the rider at the end
        list.insert(rider);
    }
}
