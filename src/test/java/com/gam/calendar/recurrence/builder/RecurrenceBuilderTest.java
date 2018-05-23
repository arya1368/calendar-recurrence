package com.gam.calendar.recurrence.builder;

import com.gam.calendar.recurrence.RecurrenceBuilder;
import com.gam.calendar.recurrence.Recurrence;
import com.gam.calendar.recurrence.type.DailyRecurrence;
import com.gam.calendar.recurrence.type.MonthlyRecurrence;
import com.gam.calendar.recurrence.type.WeeklyRecurrence;
import com.gam.calendar.recurrence.type.YearlyRecurrence;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 19/05/2018
 */
public class RecurrenceBuilderTest {

    @Test(expected = RecurrenceBuilder.IllegalBuilderArgumentException.class)
    public void createBuilderWithNullDate_throwIllegalBuilderArgumentException() throws Exception {
        new YearlyRecurrence.Builder(null).build();
    }

    @Test
    public void canAddNullToRecurrenceEndDate() throws Exception {
        Recurrence r = new MonthlyRecurrence.Builder(new Date()).setRecurrenceEndDate(null).build();
        assertNull(r.getEndDate());
    }

    @Test
    public void defaultIntervalIsOne() throws Exception {
        Recurrence r = new MonthlyRecurrence.Builder(new Date()).build();
        assertTrue(r.getInterval() == 1);
    }

    @Test(expected = RecurrenceBuilder.IllegalBuilderArgumentException.class)
    public void addZeroInterval_throwIllegalBuilderArgumentException() throws Exception {
        new DailyRecurrence.Builder(new Date()).setInterval(0).build();
    }

    @Test(expected = RecurrenceBuilder.IllegalBuilderArgumentException.class)
    public void addUnderZeroInterval_throwIllegalBuilderArgumentException() throws Exception {
        new DailyRecurrence.Builder(new Date()).setInterval(-7).build();
    }

    @Test(expected = RecurrenceBuilder.IllegalBuilderArgumentException.class)
    public void addNullExDate_throwIllegalBuilderArgumentException() throws Exception {
        new MonthlyRecurrence.Builder(new Date()).addExDate(null);
    }

    @Test(expected = RecurrenceBuilder.IllegalBuilderArgumentException.class)
    public void setNullForExDates_throwIllegalBuilderArgumentException() throws Exception {
        new YearlyRecurrence.Builder(new Date()).setExDates(null);
    }

    @Test
    public void canAddListOfDatesToExDates() throws Exception {
        List<Date> exDates = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            exDates.add(new Date());

        Recurrence r = new DailyRecurrence.Builder(new Date()).setExDates(exDates).build();
        assertEquals(exDates, r.getExDates());
     }


    @Test(expected = RecurrenceBuilder.IllegalBuilderArgumentException.class)
    public void createWeeklyWithNoDaysOfWeek_throwIllegalBuilderArgumentException() throws Exception {
        new WeeklyRecurrence.Builder(new Date()).build();
    }

    @Test
    public void weeklyRecurrenceBuilderTest() throws Exception {
        Recurrence r = new WeeklyRecurrence.Builder(new Date())
                .setInterval(1).addExDate(new Date()).onSaturday().build();

        assertEquals(1, r.getInterval());
        assertEquals(1, r.getExDates().size());
    }

    @Test(expected = RecurrenceBuilder.IllegalBuilderArgumentException.class)
    public void addZeroAsInterval_throwIllegalBuilderArgumentException() throws Exception {
        new WeeklyRecurrenceSpy.Builder(new Date()).setInterval(0).onWednesday().build();
    }
}