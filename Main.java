import java.io.File;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        RideSharingSystem system = new RideSharingSystem();
        boolean ridersLoaded = system.loadRidersFromCSV(dataFile("riders_100.csv"));
        boolean driversLoaded = system.loadDriversFromCSV(dataFile("drivers_30.csv"));
        boolean ridesLoaded = system.loadRidesFromCSV(dataFile("rides_40.csv"));

        if (!ridersLoaded || !driversLoaded || !ridesLoaded) {
            System.err.println("Unable to load all startup CSV data. Check that riders_100.csv, "
                    + "drivers_30.csv, and rides_40.csv are available and valid.");
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            try {
                runMenu(scanner, system);
            } catch (InputClosedException e) {
                System.out.println();
            }
        }
    }

    private static String dataFile(String fileName) {
        File inCurrentDirectory = new File(fileName);
        if (inCurrentDirectory.isFile()) {
            return inCurrentDirectory.getPath();
        }

        File inProjectDirectory = new File("Phase 1", fileName);
        if (inProjectDirectory.isFile()) {
            return inProjectDirectory.getPath();
        }

        File inParentDirectory = new File("..", fileName);
        if (inParentDirectory.isFile()) {
            return inParentDirectory.getPath();
        }

        return fileName;
    }

    private static void runMenu(Scanner scanner, RideSharingSystem system) {
        boolean running = true;
        while (running) {
            printMenu();
            int option = readInt(scanner, "Choose an option: ");

            switch (option) {
                case 0:
                    running = false;
                    break;
                case 1:
                    printResults(system.getAllRiders());
                    break;
                case 2:
                    printResults(system.searchRidersByHomeCity(readLine(scanner, "Home city: ")));
                    break;
                case 3:
                    printResults(system.searchRidersByName(readLine(scanner, "Rider name: ")));
                    break;
                case 4:
                    addRider(scanner, system);
                    break;
                case 5:
                    printResults(system.getAllDrivers());
                    break;
                case 6:
                    printResults(system.searchDriversByVehicleType(readVehicleType(scanner)));
                    break;
                case 7:
                    addDriver(scanner, system);
                    break;
                case 8:
                    printResults(system.getAllRidesAlphabetically());
                    break;
                case 9:
                    printResults(system.searchRidesByPickupLocation(readLine(scanner, "Pickup location: ")));
                    break;
                case 10:
                    printResults(system.searchRidesByRiderName(readLine(scanner, "Rider name: ")));
                    break;
                case 11:
                    printResults(system.getSharedRideParticipants(readLine(scanner, "Pickup location: ")));
                    break;
                case 12:
                    addRide(scanner, system);
                    break;
                default:
                    System.out.println("Invalid option. Choose a number from 0 to 12.");
                    break;
            }
        }
    }

    private static void printMenu() {
        System.out.println("1. List all riders.");
        System.out.println("2. Search for riders by home city.");
        System.out.println("3. Search for riders by name.");
        System.out.println("4. Add a rider.");
        System.out.println("5. List all drivers.");
        System.out.println("6. Search for drivers by vehicle type.");
        System.out.println("7. Add a driver.");
        System.out.println("8. List all rides alphabetically by pickup location.");
        System.out.println("9. Search for rides by pickup location.");
        System.out.println("10. Search for rides by rider name.");
        System.out.println("11. List the participants of a shared ride by pickup location.");
        System.out.println("12. Add a ride (ask the user whether it is private or shared, then prompt accordingly).");
        System.out.println("0. Exit the program.");
    }

    private static void addRider(Scanner scanner, RideSharingSystem system) {
        try {

            int id = readInt(scanner, "Rider ID: ");
            String name = readLine(scanner, "Name: ");
            String phoneNumber = readLine(scanner, "Phone number (10 digits): ");
            String email = readLine(scanner, "Email: ");
            String homeCity = readLine(scanner, "Home city: ");

            boolean added = system.addRider(new Rider(id, name, phoneNumber, email, homeCity));

            System.out.println(added ? "Rider added successfully." : "Failed to add rider.");
        } catch (IllegalArgumentException e) {

            System.out.println("Failed to add rider: " + e.getMessage());
        }
    }

    private static void addDriver(Scanner scanner, RideSharingSystem system) {
        try {

            int id = readInt(scanner, "Driver ID: ");
            String name = readLine(scanner, "Name: ");
            String phoneNumber = readLine(scanner, "Phone number (10 digits): ");
            String vehiclePlate = readLine(scanner, "Vehicle plate (ABC1234): ");
            VehicleType vehicleType = readVehicleType(scanner);

            boolean added = system.addDriver(new Driver(id, name, phoneNumber, vehiclePlate, vehicleType));
            System.out.println(added ? "Driver added successfully." : "Failed to add driver.");
        } catch (IllegalArgumentException e) {

            System.out.println("Failed to add driver: " + e.getMessage());
        }
    }

    private static void addRide(Scanner scanner, RideSharingSystem system) {

        String rideType = readLine(scanner, "Ride type (private/shared): ").toLowerCase(Locale.ROOT);
        while (!rideType.equals("private") && !rideType.equals("shared")) {

            System.out.println("Enter either private or shared.");
            rideType = readLine(scanner, "Ride type (private/shared): ").toLowerCase(Locale.ROOT);
        }

        String pickupLocation = readLine(scanner, "Pickup location: ");
        IDateTime pickupTime = readDateTime(scanner, "Pickup date and time (MM/DD/YYYY HH:MM): ");
        IDateTime dropoffTime = readDateTime(scanner, "Dropoff date and time (MM/DD/YYYY HH:MM): ");
        String dropoffLocation = readLine(scanner, "Dropoff location: ");

        if (rideType.equals("private")) {

            int riderId = readInt(scanner, "Rider ID: ");
            int driverId = readInt(scanner, "Driver ID: ");
            boolean scheduled = system.schedulePrivateRide(pickupLocation, pickupTime, dropoffTime, dropoffLocation, riderId, driverId);

            System.out.println(scheduled ? "Private ride scheduled successfully." : "Failed to schedule private ride.");
        } else {

            int[] riderIds = readRiderIds(scanner);
            int driverId = readInt(scanner, "Driver ID: ");
            boolean scheduled = system.scheduleSharedRide(pickupLocation, pickupTime, dropoffTime, dropoffLocation, riderIds, driverId);

            System.out.println(scheduled ? "Shared ride scheduled successfully." : "Failed to schedule shared ride.");
        }
    }

    private static int[] readRiderIds(Scanner scanner) {
        while (true) {

            String input = readLine(scanner, "Rider IDs (at least two, separated by commas): ");
            String[] parts = input.split("[,;\\s]+");

            if (parts.length < 2) {

                System.out.println("A shared ride requires at least two rider IDs.");
                continue;
            }

            try {

                int[] riderIds = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    riderIds[i] = Integer.parseInt(parts[i]);
                }
                return riderIds;
            } catch (NumberFormatException e) {
                System.out.println("Enter valid rider IDs separated by commas.");
            }
        }
    }

    private static IDateTime readDateTime(Scanner scanner, String prompt) {
        while (true) {

            String input = readLine(scanner, prompt);
            try {
                return RideSharingSystem.parseDateTime(input);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static VehicleType readVehicleType(Scanner scanner) {
        while (true) {
            System.out.print("Vehicle type " + java.util.Arrays.toString(VehicleType.values()) + ": ");
            String input = readLine(scanner, "").toUpperCase(Locale.ROOT);
            try {
                return VehicleType.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Enter one of the listed vehicle types.");
            }
        }
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {

            String input = readLine(scanner, prompt);
            try {

                return Integer.parseInt(input);
            } catch (NumberFormatException e) {

                System.out.println("Enter a valid whole number.");
            }
        }
    }

    private static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {

            throw new InputClosedException();
        }
        return scanner.nextLine().trim();
    }

    private static <T> void printResults(LinkedList<T> results) {
        if (results == null || results.empty()) {

            System.out.println("No results found.");
            return;
        }

        results.findfirst();
        while (true) {

            System.out.println(results.retrieve().toString());
            if (results.last()) 
                break;

            results.findnext();
        }
    }

    private static final class InputClosedException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
