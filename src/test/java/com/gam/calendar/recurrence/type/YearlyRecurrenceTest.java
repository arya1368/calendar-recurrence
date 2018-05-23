package com.gam.calendar.recurrence.type;

import com.gam.calendar.recurrence.Recurrence;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.gam.calendar.recurrence.RecurrenceTestUtil.*;
import static org.junit.Assert.*;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 19/05/2018
 */
public class YearlyRecurrenceTest {

    private Recurrence yearlyRecurrence;

    @Before
    public void setUp() throws Exception {
        String seventeenthOfBahman91 = "2013-02-05";
        CAL.setTime(SDF.parse(seventeenthOfBahman91));
        setCalendarTimeForConstantTestTime();
        yearlyRecurrence = new YearlyRecurrence.Builder(CAL.getTime()).build();
    }

    @Test
    public void createInstanceOfYearlyRecurrence() throws Exception {
        assertTrue(yearlyRecurrence instanceof YearlyRecurrence);
    }

    @Test
    public void calculateOccurrenceDateInRage() throws Exception {
        List<Date> occurrenceDates = yearlyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
        assertEquals(1, occurrenceDates.size());
        String seventeenthOfBahman97 = "2018-02-06";
        CAL.setTime(SDF.parse(seventeenthOfBahman97));
        setCalendarTimeForConstantTestTime();
        assertEquals(CAL.getTime(), occurrenceDates.get(0));
    }
}