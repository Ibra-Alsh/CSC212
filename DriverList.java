public class DriverList implements IDriverList {
    private LinkedList<IDriver> drivers;

    public DriverList() {
        drivers = new LinkedList<>();
    }

    //adds driver in the correct ID order then returns true, else returns false.
    public boolean add(IDriver driver) {
        if (driver == null || findById(driver.getId()) != null) {
            return false;
        }

        if (drivers.empty()) {
            drivers.insert(driver);
            return true;
        }

        // Checks for a duplicate plates.
        drivers.findfirst();
        while (true) {
            
            IDriver current = drivers.retrieve();

            if (driver.getVehiclePlate().equals(current.getVehiclePlate())) {
                return false;
            }

            if (drivers.last()) {
                break;
            }

            drivers.findnext();
        }

        // Insert in ascending ID order.
        drivers.findfirst();
        while (true) {
            IDriver current = drivers.retrieve();

            if (driver.compareTo(current) < 0) {
                drivers.update(driver);
                drivers.insert(current);
                return true;
            }

            if (drivers.last()) {
                drivers.insert(driver);
                return true;
            }

            drivers.findnext();
        }
    }

    public IDriver findById(int dId) {
        if (drivers.empty()) {
            return null;
        }

        drivers.findfirst();

        while (true) {
            IDriver current = drivers.retrieve();
            if (current.getId() == dId) {
                return current;
            }
            if (drivers.last()) {
                break;
            }

            drivers.findnext();
        }

        return null;
    }

    public LinkedList<IDriver> findByName (String fName) {
        if (drivers.empty()) {
            return new LinkedList<>();
        }

        LinkedList<IDriver> matchingNames = new LinkedList<>();
        drivers.findfirst();
        while (true) {
            IDriver current = drivers.retrieve();

            if (current.getName().equals(fName)) {
                matchingNames.insert(current);
            }
            if (drivers.last()) {
                break;
            }

            drivers.findnext();
        }

        return matchingNames;
    }

    public IDriver findByVehiclePlate(String cPlate) {
        if (drivers.empty()) {
            return null;
        }

        drivers.findfirst();
        while (true) {
            IDriver current = drivers.retrieve();

            if (current.getVehiclePlate().equals(cPlate)) {
                return current;
            }
            if (drivers.last()) {
                break;
            }

            drivers.findnext();
        }

        return null;
    }

    public LinkedList<IDriver> findByVehicleType(VehicleType vehicleType) {
        if (drivers.empty()) {
            return new LinkedList<>();
        }

        LinkedList<IDriver> matchingTypes = new LinkedList<>();
        drivers.findfirst();
        while (true) {
            IDriver current = drivers.retrieve();

            if (current.getVehicleType() == vehicleType) {
                matchingTypes.insert(current);
            }
            if (drivers.last()) {
                break;
            }

            drivers.findnext();
        }

        return matchingTypes;
    }

    public LinkedList<IDriver> getAll() {
        return drivers;
    }

    public boolean removeById(int dId) {
        if (drivers.empty()) {
            return false;
        }

        drivers.findfirst();
        while (true) {
            IDriver current = drivers.retrieve();

            if (current.getId() == dId) {
                drivers.remove();
                return true;
            }
            if (drivers.last()) {
                break;
            }

            drivers.findnext();
        }

        return false;
    }
    
    public boolean removeByVehiclePlate(String cPlate) {
        if (drivers.empty()) {
            return false;
        }

        drivers.findfirst();
        while (true) {
            IDriver current = drivers.retrieve();

            if (current.getVehiclePlate().equals(cPlate)) {
                drivers.remove();
                return true;
            }
            if (drivers.last()) {
                break;
            }

            drivers.findnext();
        }

        return false;
    }

    public int removeByName(String fName) {
        if (drivers.empty()) {
            return 0;
        }

        int numRemoved = 0;
        drivers.findfirst();

        while (true) {

            IDriver current = drivers.retrieve();

            if (current.getName().equals(fName)) {
                drivers.remove();
                numRemoved++;
                if (drivers.empty()) {
                    break;
                }
                continue;
            }
            if (drivers.last()) {
                break;
            }

            drivers.findnext();
        }

        return numRemoved;
    }

    public int removeByVehicleType(VehicleType vehicleType) {
        if (drivers.empty()) {
            return 0;
        }

        int numRemoved = 0;
        drivers.findfirst();

        while (true) {

            IDriver current = drivers.retrieve();

            if (current.getVehicleType() == vehicleType) {
                drivers.remove();
                numRemoved++;
                if (drivers.empty()) {
                    break;
                }
                continue;
            }
            if (drivers.last()) {
                break;
            }

            drivers.findnext();
        }

        return numRemoved;
    }

    public int size() {
        if (drivers.empty()) {
            return 0;
        }
        
        int size = 0;
        drivers.findfirst();
        while (true) {
            size++;
            if (drivers.last()) {
                break;
            }
            drivers.findnext();
        }
        return size;
    }
}
