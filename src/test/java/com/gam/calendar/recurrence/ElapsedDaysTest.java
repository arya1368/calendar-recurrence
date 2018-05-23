package com.gam.calendar.recurrence;

import com.gam.calendar.recurrence.type.DailyRecurrence;
import com.ibm.icu.util.Calendar;
import org.junit.Before;
import org.junit.Test;

import static com.gam.calendar.recurrence.RecurrenceTestUtil.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 16/05/2018
 */
public class ElapsedDaysTest {

    private Recurrence dailyRecurrence;

    @Before
    public void setUp() throws Exception {
        CAL.setTime(SDF.parse("2018-01-01"));
        setCalendarTimeForConstantTestTime();
        dailyRecurrence = new DailyRecurrence.Builder(CAL.getTime()).setInterval(1).build();
    }

    @Test
    public void inSameDayAsRecurrenceBeginDate() throws Exception {
        CAL.setTime(dailyRecurrence.getBeginDate());
        CAL.add(Calendar.HOUR_OF_DAY, -3);
        assertElapsedIsSameForDifferentHoursOfDay(0);
    }

    @Test
    public void elapsedDaysFacts() throws Exception {
        CAL.setTime(dailyRecurrence.getBeginDate());
        CAL.add(Calendar.DATE, 1);
        assertElapsedIsSameForDifferentHoursOfDay(1);

        CAL.setTime(dailyRecurrence.getBeginDate());
        CAL.add(Calendar.DATE, -3);
        assertElapsedIsSameForDifferentHoursOfDay(-3);

        CAL.setTime(dailyRecurrence.getBeginDate());
        CAL.add(Calendar.DATE, 100);
        assertElapsedIsSameForDifferentHoursOfDay(100);
    }

    private void assertElapsedIsSameForDifferentHoursOfDay(int expectedElapsed) {
        assertEquals(expectedElapsed, dailyRecurrence.calculateElapsedUnit(CAL.getTime()));
        CAL.add(Calendar.HOUR_OF_DAY, 5);
        assertEquals(expectedElapsed, dailyRecurrence.calculateElapsedUnit(CAL.getTime()));

        setCalendarTimeForEndOfDay();
        assertEquals(expectedElapsed, dailyRecurrence.calculateElapsedUnit(CAL.getTime()));
    }
}
