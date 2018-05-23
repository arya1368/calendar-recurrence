package com.gam.calendar.recurrence;

import com.ibm.icu.util.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.gam.calendar.recurrence.Recurrence.PERSIAN_LOCALE;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 16/05/2018
 */
public class RecurrenceTestUtil {

    public static final Calendar CAL = Calendar.getInstance(PERSIAN_LOCALE);
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateRange RANGE_WITH_30_DAYS = new DateRange();
    static {
        try {
            setRangeWith30Days();
        } catch (ParseException e) {
            throw new TestUtilException("couldn't parse date format", e);
        }
    }

    private static void setRangeWith30Days() throws ParseException {
        CAL.setTime(SDF.parse("2018-01-21"));
        setCalendarTimeForStartOfDay();

        RANGE_WITH_30_DAYS.setFromDate(CAL.getTime());
        CAL.add(Calendar.DATE, 29);
        setCalendarTimeForEndOfDay();
        RANGE_WITH_30_DAYS.setToDate(CAL.getTime());
    }

    public static void setCalendarTimeForStartOfDay() {
        setCalendarTime(0 , 0 , 0);
    }

    public static void setCalendarTimeForEndOfDay() {
        setCalendarTime(23, 59, 59);
    }

    public static void setCalendarTimeForConstantTestTime() {
        setCalendarTime(8, 0 , 0);
    }

    public static void setCalendarTime(int hour, int minute, int second) {
        CAL.set(Calendar.HOUR_OF_DAY, hour);
        CAL.set(Calendar.MINUTE, minute);
        CAL.set(Calendar.SECOND, second);
    }

    public static class TestUtilException extends RuntimeException {
        public TestUtilException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
