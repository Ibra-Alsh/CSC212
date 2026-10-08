/**
 * The set of vehicle types a driver's vehicle may be. Used by IDriver in
 * place of a free-text vehicle type string, so vehicle types are validated
 * by the type system instead of by string matching.
 */
public enum VehicleType {
    SEDAN,
    LUXURY_SEDAN,
    SUV,
    VAN
}
