package com.gam.calendar.recurrence;

import com.gam.calendar.recurrence.type.YearlyRecurrence;
import com.ibm.icu.util.Calendar;
import org.junit.Before;
import org.junit.Test;

import static com.gam.calendar.recurrence.RecurrenceTestUtil.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 19/05/2018
 */
public class ElapsedYearsTest {

    private Recurrence yearlyRecurrence;

    @Before
    public void setUp() throws Exception {
        String fifthOfKordad96 = "2017-05-26";
        CAL.setTime(SDF.parse(fifthOfKordad96));
        setCalendarTimeForConstantTestTime();
        yearlyRecurrence = new YearlyRecurrence.Builder(CAL.getTime()).build();
    }

    @Test
    public void elapsedYearsBeforeRecurrenceBeginDateReturnZero() throws Exception {
        CAL.setTime(yearlyRecurrence.getBeginDate());
        CAL.add(Calendar.DATE, -1);
        assertEquals(0, yearlyRecurrence.calculateElapsedUnit(CAL.getTime()));
        CAL.add(Calendar.MONTH, -5);
        assertEquals(0, yearlyRecurrence.calculateElapsedUnit(CAL.getTime()));
        CAL.add(Calendar.YEAR, -5);
        assertEquals(0, yearlyRecurrence.calculateElapsedUnit(CAL.getTime()));
    }

    @Test
    public void elapsedYearFacts() throws Exception {
        CAL.setTime(yearlyRecurrence.getBeginDate());
        CAL.add(Calendar.HOUR_OF_DAY, 1);
        assertEquals(1, yearlyRecurrence.calculateElapsedUnit(CAL.getTime()));
        CAL.setTime(yearlyRecurrence.getBeginDate());
        CAL.add(Calendar.DATE, 10);
        assertEquals(1, yearlyRecurrence.calculateElapsedUnit(CAL.getTime()));
        CAL.setTime(yearlyRecurrence.getBeginDate());
        CAL.add(Calendar.MONTH, 8);
        assertEquals(1, yearlyRecurrence.calculateElapsedUnit(CAL.getTime()));
        CAL.setTime(yearlyRecurrence.getBeginDate());
        CAL.add(Calendar.YEAR, 2);
        CAL.add(Calendar.MONTH, -2);
        assertEquals(2, yearlyRecurrence.calculateElapsedUnit(CAL.getTime()));
    }
}
