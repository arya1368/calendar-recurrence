package com.gam.calendar.recurrence;

import com.gam.calendar.recurrence.type.WeeklyRecurrence;
import com.ibm.icu.util.Calendar;
import org.junit.Before;
import org.junit.Test;


import static com.gam.calendar.recurrence.RecurrenceTestUtil.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 19/05/2018
 */
public class ElapsedWeeksTest {

    private Recurrence weeklyRecurrence;

    @Before
    public void setUp() throws Exception {
        String saturdayTwentiethFarvardin = "2017-03-25";
        CAL.setTime(SDF.parse(saturdayTwentiethFarvardin));
        setCalendarTimeForConstantTestTime();
        weeklyRecurrence = new WeeklyRecurrence.Builder(CAL.getTime()).onSaturday().build();
    }

    @Test
    public void inSameWeekAsRecurrenceBeginDate() throws Exception {
        CAL.setTime(weeklyRecurrence.getBeginDate());
        CAL.add(Calendar.HOUR_OF_DAY, 3);
        assertEquals(0, weeklyRecurrence.calculateElapsedUnit(CAL.getTime()));
        CAL.setTime(weeklyRecurrence.getBeginDate());
        CAL.add(Calendar.DATE, 2);
        assertEquals(0, weeklyRecurrence.calculateElapsedUnit(CAL.getTime()));
        CAL.setTime(weeklyRecurrence.getBeginDate());
        CAL.add(Calendar.DATE, 6);
        setCalendarTimeForEndOfDay();
        assertEquals(0, weeklyRecurrence.calculateElapsedUnit(CAL.getTime()));
    }

    @Test
    public void elapsedWeeksFacts() throws Exception {
        CAL.setTime(weeklyRecurrence.getBeginDate());
        CAL.add(Calendar.DATE, 7);
        assertEquals(1, weeklyRecurrence.calculateElapsedUnit(CAL.getTime()));
        CAL.setTime(weeklyRecurrence.getBeginDate());
        CAL.add(Calendar.WEEK_OF_YEAR, 2);
        assertEquals(2, weeklyRecurrence.calculateElapsedUnit(CAL.getTime()));
        CAL.add(Calendar.DATE, 4);
        assertEquals(2, weeklyRecurrence.calculateElapsedUnit(CAL.getTime()));
    }
}
