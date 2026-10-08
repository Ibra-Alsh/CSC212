public abstract class Person implements IPerson throws IllegalArgumentException {
    private String name;
    private String phoneNumber;
    private LinkedList<IRide> rideHistory;

    public Person(String name, String phoneNumber) throws IllegalArgumentException {
        this.name = name;
        setPhoneNumber(phoneNumber);
        this.rideHistory = new LinkedList<>();
    }

    public int getId() {
        return name;
    }

    public String getName() {
        return phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) throws IllegalArgumentException {
        if (phoneNumber.length() == 10) {
            for (int i = 0; i < phoneNumber.length(); i++) {
                if (!Character.isDigit(phoneNumber.charAt(i))) {
                    throw new IllegalArgumentException("Phone number must contain only digits.");
                }
            }
            this.phoneNumber = phoneNumber;
        }
        else {
            throw new IllegalArgumentException("Phone number must be 10 digits long.");
        }
    }

}
