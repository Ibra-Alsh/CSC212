/**
 * Minimal date/time abstraction used by private rides and shared rides.
 * Implementations must define chronological ordering via compareTo.
 */
public interface IDateTime extends Comparable<IDateTime> {

    //return year (e.g., 2026). 
    int getYear();

    //return month in [1..12]. 
    int getMonth();

    //return day in [1..31]. 
    int getDay();

    //return hour in [0..23]. 
    int getHour();

    //return minute in [0..59]. 
    int getMinute();

    //Formats this date/time for display (e.g., "MM/DD/YYYY HH:MM").
     String format();

    /**
     * Compares this date/time with another chronologically: by year, then
     * month, then day, then hour, then minute. Returns a negative integer,
     * zero, or a positive integer as this date/time is earlier than, equal
     * to, or later than the other date/time. This ordering is what
     * schedulePrivateRide and scheduleSharedRide must use to detect whether
     * two time ranges overlap.
     */
    @Override
    int compareTo(IDateTime other);
}