package com.gam.calendar.recurrence;

import com.gam.calendar.recurrence.type.MonthlyRecurrence;
import com.ibm.icu.util.Calendar;
import org.junit.Before;
import org.junit.Test;

import static com.gam.calendar.recurrence.RecurrenceTestUtil.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 19/05/2018
 */
public class ElapsedMonthsTest {

    private Recurrence monthlyRecurrence;

    @Before
    public void setUp() throws Exception {
        String fifthOfFarvardin = "2017-03-25";
        CAL.setTime(SDF.parse(fifthOfFarvardin));
        setCalendarTimeForConstantTestTime();
        monthlyRecurrence = new MonthlyRecurrence.Builder(CAL.getTime()).setInterval(1).build();
    }

    @Test
    public void elapsedMonthsBeforeRecurrenceBeginDateReturnZero() throws Exception {
        CAL.setTime(monthlyRecurrence.getBeginDate());
        CAL.add(Calendar.DATE, -1);
        assertEquals(0, monthlyRecurrence.calculateElapsedUnit(CAL.getTime()));
        CAL.add(Calendar.DATE, -5);
        assertEquals(0, monthlyRecurrence.calculateElapsedUnit(CAL.getTime()));
        CAL.add(Calendar.MONTH, -5);
        assertEquals(0, monthlyRecurrence.calculateElapsedUnit(CAL.getTime()));
    }

    @Test
    public void elapsedMonthFacts() throws Exception {
        CAL.setTime(monthlyRecurrence.getBeginDate());
        CAL.add(Calendar.HOUR_OF_DAY, 1);
        assertEquals(1, monthlyRecurrence.calculateElapsedUnit(CAL.getTime()));
        CAL.setTime(monthlyRecurrence.getBeginDate());
        CAL.add(Calendar.DATE, 10);
        assertEquals(1, monthlyRecurrence.calculateElapsedUnit(CAL.getTime()));
        String forthOfKhordad = "2017-05-25";
        CAL.setTime(SDF.parse(forthOfKhordad));
        setCalendarTimeForConstantTestTime();
        assertEquals(2, monthlyRecurrence.calculateElapsedUnit(CAL.getTime()));
        String fifthOfKhordad = "2017-05-26";
        CAL.setTime(SDF.parse(fifthOfKhordad));
        setCalendarTimeForConstantTestTime();
        assertEquals(2, monthlyRecurrence.calculateElapsedUnit(CAL.getTime()));
        CAL.add(Calendar.HOUR_OF_DAY, 1);
        assertEquals(3, monthlyRecurrence.calculateElapsedUnit(CAL.getTime()));
        setCalendarTimeForEndOfDay();
        assertEquals(3, monthlyRecurrence.calculateElapsedUnit(CAL.getTime()));
    }
}
