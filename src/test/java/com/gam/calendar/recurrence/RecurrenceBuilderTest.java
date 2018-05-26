package com.gam.calendar.recurrence;


import com.gam.calendar.recurrence.RecurrenceBuilder.IllegalBuilderArgumentException;
import com.gam.calendar.recurrence.type.DailyRecurrence;
import com.gam.calendar.recurrence.type.MonthlyRecurrence;
import com.gam.calendar.recurrence.type.WeeklyRecurrence;
import com.gam.calendar.recurrence.type.YearlyRecurrence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 26/05/2018
 */
public class RecurrenceBuilderTest {
    private RecurrenceBuilder dailyBuilder;
    private RecurrenceBuilder weeklyBuilder;
    private RecurrenceBuilder monthlyBuilder;
    private RecurrenceBuilder yearlyBuilder;

    @BeforeEach
    void setUp() {
        dailyBuilder = new DailyRecurrence.Builder(new Date());
        weeklyBuilder = new WeeklyRecurrence.Builder(new Date());
        monthlyBuilder = new MonthlyRecurrence.Builder(new Date());
        yearlyBuilder = new YearlyRecurrence.Builder(new Date());
    }

    @Test
    void canCreateInstanceOfBuilders() {
        assertTrue(dailyBuilder instanceof DailyRecurrence.Builder);
        assertTrue(weeklyBuilder instanceof WeeklyRecurrence.Builder);
        assertTrue(monthlyBuilder instanceof MonthlyRecurrence.Builder);
        assertTrue(yearlyBuilder instanceof YearlyRecurrence.Builder);
    }

    @Test
    void createBuilderWithNullDate_throwIllegalBuilderArgumentException() {
        IllegalBuilderArgumentException exception = assertThrows(IllegalBuilderArgumentException.class, () -> new DailyRecurrence.Builder(null));
        assertEquals("beginDate can not be null.", exception.getMessage());
    }

    @Test
    void canAddNullToRecurrenceEndDate() throws Exception {
        RecurrenceBuilder builder = dailyBuilder.setRecurrenceEndDate(null);
        assertNull(builder.range.getToDate());
    }

    @Test
    void defaultIntervalIsOne() throws Exception {
        assertTrue(weeklyBuilder.interval == 1);
    }

    @Test
    void addZeroInterval_throwIllegalBuilderArgumentException() throws Exception {
        IllegalBuilderArgumentException exception = assertThrows(IllegalBuilderArgumentException.class, () -> monthlyBuilder.setInterval(0));
        assertEquals("interval must be greater than zero. interval: 0", exception.getMessage());
    }

    @Test
    void addNegativeInterval_throwIllegalBuilderArgumentException() throws Exception {
        IllegalBuilderArgumentException exception = assertThrows(IllegalBuilderArgumentException.class, () -> yearlyBuilder.setInterval(-3));
        assertEquals("interval must be greater than zero. interval: -3", exception.getMessage());
    }

    @Test
    void addNullExDate_throwIllegalBuilderArgumentException() throws Exception {
        IllegalBuilderArgumentException exception = assertThrows(IllegalBuilderArgumentException.class, () -> dailyBuilder.addExDate(null));
        assertEquals("can not add null to exDates.", exception.getMessage());
    }

    @Test
    void setNullForExDates_throwIllegalBuilderArgumentException() throws Exception {
        IllegalBuilderArgumentException exception = assertThrows(IllegalBuilderArgumentException.class, () -> weeklyBuilder.setExDates(null));
        assertEquals("exDates can not be null.", exception.getMessage());
    }

    @Test
    void canAddListOfDatesToExDates() throws Exception {
        List<Date> exDates = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            exDates.add(new Date());

        RecurrenceBuilder builder = monthlyBuilder.setExDates(exDates);
        assertEquals(exDates, builder.exDates);
    }

    @Test
    void addOddDaysOfWeek() {
        assertTrue(weeklyBuilder.daysOfWeekOrder.size() == 0);
        RecurrenceBuilder builder = weeklyBuilder.onSunday().onTuesday().onThursday();
        assertTrue(builder.daysOfWeekOrder.size() == 3);
    }

    @Test
    void addEvenDaysOfWeek() {
        assertTrue(weeklyBuilder.daysOfWeekOrder.size() == 0);
        RecurrenceBuilder builder = weeklyBuilder.onSaturday().onMonday().onWednesday();
        assertTrue(builder.daysOfWeekOrder.size() == 3);
    }

    @Test
    void addWeekends() {
        assertTrue(weeklyBuilder.daysOfWeekOrder.size() == 0);
        RecurrenceBuilder builder = weeklyBuilder.onThursday().onFriday();
        assertTrue(builder.daysOfWeekOrder.size() == 2);
    }
}