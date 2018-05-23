package com.gam.calendar.recurrence.type;

import com.gam.calendar.recurrence.Recurrence;
import com.ibm.icu.util.Calendar;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.gam.calendar.recurrence.RecurrenceTestUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 16/05/2018
 */
public class DailyRecurrenceTest {

    private Recurrence dailyRecurrence;

    @Before
    public void setUp() throws Exception {
        CAL.setTime(SDF.parse("2018-01-01"));
        setCalendarTimeForConstantTestTime();
        dailyRecurrence = new DailyRecurrence.Builder(CAL.getTime()).setInterval(1).build();
    }

    @Test
    public void createInstanceOfDailyRecurrence() throws Exception {
        assertTrue(dailyRecurrence instanceof DailyRecurrence);
    }

    @Test
    public void calculateDailyRecurrenceWithOneDayInterval() throws Exception {
        List<Date> occurrenceDates = dailyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
        assertEquals(30, occurrenceDates.size());
        assertContainsAllExpectedDays(occurrenceDates);
    }

    private void assertContainsAllExpectedDays(List<Date> occurrenceDates) {
        CAL.setTime(RANGE_WITH_30_DAYS.getFromDate());
        setCalendarTimeForConstantTestTime();
        for (Date date : occurrenceDates) {
            assertEquals(CAL.getTime(), date);
            CAL.add(Calendar.DATE, dailyRecurrence.getInterval());
        }
    }

    @Test
    public void calculateDailyRecurrenceWithOneDayIntervalAnd5ExDates() throws Exception {
        CAL.setTime(RANGE_WITH_30_DAYS.getFromDate());
        setCalendarTimeForConstantTestTime();
        for (int i = 1; i < 6; i++) {
            CAL.add(Calendar.DATE, i);
            dailyRecurrence.getExDates().add(CAL.getTime());
        }
        assertEquals(5, dailyRecurrence.getExDates().size());
        List<Date> occurrenceDates = dailyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
        assertOccurrenceDontContainsExDates(25, occurrenceDates);
    }

    @Test
    public void calculateDailyRecurrenceStartedInRangeWithTwoDayIntervalAndOneExDate() throws Exception {
        CAL.setTime(RANGE_WITH_30_DAYS.getFromDate());
        setCalendarTimeForConstantTestTime();
        CAL.add(Calendar.DATE, 5);
        Date recBeginDate = CAL.getTime();
        CAL.add(Calendar.DATE, 4);
        Date exDate = CAL.getTime();
        dailyRecurrence = new DailyRecurrence.Builder(recBeginDate)
                .addExDate(exDate)
                .setInterval(2).build();
        List<Date> occurrenceDates = dailyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
        assertOccurrenceDontContainsExDates(12, occurrenceDates);
    }

    private void assertOccurrenceDontContainsExDates(int expectedSize, List<Date> occurrenceDates) {
        assertEquals(expectedSize, occurrenceDates.size());
        for (Date ex : dailyRecurrence.getExDates())
            assertFalse(occurrenceDates.contains(ex));
    }
}