package com.gam.calendar.recurrence;


import com.gam.calendar.recurrence.type.DailyRecurrence;
import com.gam.calendar.recurrence.type.MonthlyRecurrence;
import com.gam.calendar.recurrence.type.WeeklyRecurrence;
import com.gam.calendar.recurrence.type.YearlyRecurrence;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 26/05/2018
 */
public class RecurrenceBuilderFactoryTest {
    
    @Test
    void canCreateDailyBuilder() {
        assertTrue(RecurrenceBuilderFactory.DAILY.newInstance(new Date()) instanceof DailyRecurrence.Builder);
    }

    @Test
    void canCreateWeeklyBuilder() {
        assertTrue(RecurrenceBuilderFactory.WEEKLY.newInstance(new Date()) instanceof WeeklyRecurrence.Builder);
    }

    @Test
    void canCreateMonthlyBuilder() {
        assertTrue(RecurrenceBuilderFactory.MONTHLY.newInstance(new Date()) instanceof MonthlyRecurrence.Builder);
    }

    @Test
    void canCreateYearlyBuilder() {
        assertTrue(RecurrenceBuilderFactory.YEARLY.newInstance(new Date()) instanceof YearlyRecurrence.Builder);
    }
}