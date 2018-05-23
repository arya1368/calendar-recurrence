package com.gam.calendar.recurrence.builder;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 19/05/2018
 */
public class WeeklyRecurrenceBuilderTest {

    private WeeklyRecurrenceSpy weeklyRecurrenceSpy;

    @Test
    public void addAllDaysOfWeekToRecurrence() throws Exception {
        weeklyRecurrenceSpy = (WeeklyRecurrenceSpy) new WeeklyRecurrenceSpy.Builder(new Date())
                .onSaturday().onSunday().onMonday()
                .onThursday().onWednesday().onTuesday()
                .onFriday().build();

        Set<Integer> expectedDaysOfWeek = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        Set<Integer> actualDaysOfWeek = new HashSet<>();
        for (int day : weeklyRecurrenceSpy.getDaysOfWeekWithOrder().keySet())
            actualDaysOfWeek.add(weeklyRecurrenceSpy.getDaysOfWeekWithOrder().get(day));

        assertTrue(actualDaysOfWeek.size() == 7);
        assertEquals(expectedDaysOfWeek, actualDaysOfWeek);
    }

    @Test
    public void addSaturdayMondayWednesday() throws Exception {
        weeklyRecurrenceSpy = (WeeklyRecurrenceSpy) new WeeklyRecurrenceSpy.Builder(new Date())
                .onSaturday().onMonday()
                .onWednesday().build();

        Set<Integer> expectedDaysOfWeek = new HashSet<>(Arrays.asList(2, 4, 7));
        Set<Integer> actualDaysOfWeek = new HashSet<>();
        for (int day : weeklyRecurrenceSpy.getDaysOfWeekWithOrder().keySet())
            actualDaysOfWeek.add(weeklyRecurrenceSpy.getDaysOfWeekWithOrder().get(day));

        assertTrue(actualDaysOfWeek.size() == 3);
        assertEquals(expectedDaysOfWeek, actualDaysOfWeek);
    }
}
