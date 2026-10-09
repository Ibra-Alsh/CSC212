public class Driver extends Person implements IDriver {
    private String vehiclePlate;
    private VehicleType vehicleType;

    public Driver(int id, String name, String phoneNumber, String vehiclePlate, VehicleType vehicleType) throws IllegalArgumentException {
        super(id, name, phoneNumber);
        setVehiclePlate(vehiclePlate);
        setVehicleType(vehicleType);
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    @Override
    public void setVehiclePlate(String vehiclePlate) {
        if (vehiclePlate == null || !vehiclePlate.matches("[A-Z]{3}[0-9]{4}")) {
            throw new IllegalArgumentException("Vehicle plate must contain 3 uppercase letters followed by 4 digits.");
        }
        this.vehiclePlate = vehiclePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    @Override
    public int compareTo(IDriver other) {
        if (this.getId() < other.getId()) {
            return -1;
        } else if (this.getId() > other.getId()) {
            return 1;
        } else {
            return 0;
        }
    }

    public LinkedList<IRide> getRideHistory() {
        return super.getRideHistory();
    }

    @Override
    public String toString() {
        return "Driver: " + getId() + ", name: " + getName() + ", phoneNumber: " + getPhoneNumber() + ", vehiclePlate: " + vehiclePlate + ", vehicleType: " + vehicleType;
    }
}

