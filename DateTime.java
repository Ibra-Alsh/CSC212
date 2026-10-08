public class DateTime implements IDateTime {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public DateTime(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }


    //returns year
    public int getYear() {
        return year;
    }


    //return month in [1..12].
    public int getMonth() {
        return month;
    }

    //return day in [1..31].
    public  int getDay(){
        return day;
    }

    //return hour in [0..23].
    public int getHour() {
        return hour;
    }

    //return minute in [0..59].
    public int getMinute(){
        return minute;
    }

    //Formats this date/time for display (e.g., "MM/DD/YYYY HH:MM").
    public String format(){
        return String.format("%02d/%02d/%04d %02d:%02d", month, day, year, hour, minute);
    }

    /**
     * Compares this date/time with another chronologically: by year, then
     * month, then day, then hour, then minute. Returns a negative integer,
     * zero, or a positive integer as this date/time is earlier than, equal
     * to, or later than the other date/time. This ordering is what
     * schedulePrivateRide and scheduleSharedRide must use to detect whether
     * two time ranges overlap.
     */
    @Override
    public int compareTo(IDateTime other){
        if (this.year < other.getYear()) {
            return -1;
        } else if (this.year > other.getYear()) {
            return 1;
        } else {
            if (this.month < other.getMonth()) {
                return -1;
            } else if (this.month > other.getMonth()) {
                return 1;
            } else {
                if (this.day < other.getDay()) {
                    return -1;
                } else if (this.day > other.getDay()) {
                    return 1;
                } else {
                    if (this.hour < other.getHour()) {
                        return -1;
                    } else if (this.hour > other.getHour()) {
                        return 1;
                    } else {
                        if (this.minute < other.getMinute()) {
                            return -1;
                        } else if (this.minute > other.getMinute()) {
                            return 1;
                        } else {
                            return 0; // They are equal
                        }
                    }
                }
            }
        }
    }
}