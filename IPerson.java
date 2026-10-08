/**
 * Common fields and behavior shared by every person in the ride-sharing
 * system (riders and drivers). IRider and IDriver both extend this interface.
 */
public interface IPerson {

    //return the unique ID of this person.
    int getId();

    //return the full name of this person.
    String getName();

    //Sets the full name of this person.
    void setName(String name);

    //return the phone number of this person.
    String getPhoneNumber();

    /**
     * Sets the phone number of this person.
     * The phone number must be exactly 10 digits (numeric characters only,
     * e.g., "0551234567"). Implementations must validate the format and
     * throw IllegalArgumentException if it does not match.
     */
    void setPhoneNumber(String phoneNumber);

    //return the list of rides associated with this person (as a rider, or as an assigned driver).
    LinkedList<IRide> getRideHistory();

    // Returns a formatted string describing this person.
    @Override
    String toString();
}
