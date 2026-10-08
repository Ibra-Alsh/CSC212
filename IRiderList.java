/**
 * Stores all riders in a structure maintained in sorted order by rider ID.
 */
public interface IRiderList {

    // Inserts a rider into the list in sorted order by ID. If a rider with the same ID already exists, insertion fails.
    boolean add(IRider rider);

    //Searches for a rider by ID.
    IRider findById(int riderId);

    // Searches for all riders with the given full name.
    LinkedList<IRider> findByName(String fullName);

    //Searches for a rider by email address.
    IRider findByEmail(String email);

    //Returns all riders from the specified home city.
    LinkedList<IRider> findByHomeCity(String homeCity);

    //Returns all riders in the linked list.
    LinkedList<IRider> getAll();

    //Removes a rider by ID. Returns true if a rider with that ID was found and removed; false otherwise.
    boolean removeById(int riderId);

    //Removes a rider by email address. Returns true if a rider with that email was found and removed; false otherwise.
    boolean removeByEmail(String email);

    /**
     * Name and home city are not unique, so more than one rider may match.
     * Removes every rider whose full name exactly matches the given name
     * (the same full-name equality used by findByName), not a first-name-only
     * or partial match.
     * Returns the number of riders removed (0 if none matched).
     */
    int removeByName(String fullName);

    /**
     * Removes every rider from the specified home city.
     * Returns the number of riders removed (0 if none matched).
     */
    int removeByHomeCity(String homeCity);

    //return the total number of riders stored.
    int size();
}
