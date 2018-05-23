package com.gam.calendar.recurrence.type;

import com.gam.calendar.recurrence.Recurrence;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gam.calendar.recurrence.RecurrenceTestUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 20/05/2018
 */
public class WeeklyRecurrenceTest {

    private Recurrence evenDayRecurrence;
    private Recurrence oddDayRecurrence;
    private Recurrence weekendRecurrence;

    @Before
    public void setUp() throws Exception {
        String saturdayThirtiethOfDey = "2018-01-20";
        CAL.setTime(SDF.parse(saturdayThirtiethOfDey));
        setCalendarTimeForStartOfDay();
        evenDayRecurrence = new WeeklyRecurrence.Builder(CAL.getTime())
                .onSaturday().onMonday().onWednesday()
                .build();

        oddDayRecurrence = new WeeklyRecurrence.Builder(CAL.getTime())
                .onSunday().onTuesday().onThursday()
                .build();

        weekendRecurrence = new WeeklyRecurrence.Builder(CAL.getTime())
                .onThursday().onFriday()
                .build();
    }

    @Test
    public void createInstanceOfWeeklyRecurrence() throws Exception {
        assertTrue(evenDayRecurrence instanceof WeeklyRecurrence);
        assertTrue(oddDayRecurrence instanceof WeeklyRecurrence);
        assertTrue(weekendRecurrence instanceof WeeklyRecurrence);
    }

    /**
     *  even: e
     *
     *  Saturday    Sunday    Monday    Thursday    Wednesday    Thursday    Friday
     * 2018-01-20               e                      e
     *      e                   e                      e
     *      e                   e                      e
     *      e                   e                      e
     *      e                   e      2018-02-20
     */
    @Test
    public void calculateOccurrenceDatesForEvenDateRecurrence() throws Exception {
        List<Date> evenDayOccurrence = evenDayRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
        assertEquals(13, evenDayOccurrence.size());
        List<Date> expectedDates = listOf("2018-01-22", "2018-01-24",
                "2018-01-27", "2018-01-29", "2018-01-31",
                "2018-02-03", "2018-02-05", "2018-02-07",
                "2018-02-10", "2018-02-12", "2018-02-14",
                "2018-02-17", "2018-02-19"
                );

        assertEquals(expectedDates, evenDayOccurrence);
    }

    /**
     *  even: e
     *
     *  Saturday    Sunday    Monday    Thursday    Wednesday    Thursday    Friday
     * 2018-01-20               e                      e
     *      e                   e                      e
     *      e                   e                      e
     *      e                   e                      e        2018-02-15
     *                                 2018-02-20
     */
    @Test
    public void calculateOccurrenceDateWhenRecurrenceEndInRange() throws Exception {
        evenDayRecurrence = new WeeklyRecurrence.Builder(CAL.getTime()).setRecurrenceEndDate(SDF.parse("2018-02-15"))
                .onSaturday().onMonday().onWednesday()
                .build();

        List<Date> evenDayOccurrence = evenDayRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
        assertEquals(11, evenDayOccurrence.size());
        List<Date> expectedDates = listOf("2018-01-22", "2018-01-24",
                "2018-01-27", "2018-01-29", "2018-01-31",
                "2018-02-03", "2018-02-05", "2018-02-07",
                "2018-02-10", "2018-02-12", "2018-02-14"
        );

        assertEquals(expectedDates, evenDayOccurrence);
    }

    /**
     *  odd: o
     *
     *  Saturday    Sunday    Monday    Thursday    Wednesday    Thursday    Friday
     * 2018-01-20     o                    o                       o
     *                o                    o                       o
     *                o                    o                       o
     *                o                    o                       o
     *                o                2018-02-20
     */
    @Test
    public void calculateOccurrenceDatesForOddDateRecurrence() throws Exception {
        List<Date> oddDayOccurrence = oddDayRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
        assertEquals(13, oddDayOccurrence.size());
        List<Date> expectedDates = listOf("2018-01-21", "2018-01-23", "2018-01-25",
                "2018-01-28", "2018-01-30", "2018-02-01",
                "2018-02-04", "2018-02-06", "2018-02-08",
                "2018-02-11", "2018-02-13", "2018-02-15",
                "2018-02-18"
        );

        assertEquals(expectedDates, oddDayOccurrence);
    }

    /**
     *  odd: o
     *  exDate: x
     *
     *  Saturday    Sunday    Monday    Thursday    Wednesday    Thursday    Friday
     * 2018-01-20
     * 2018-01-27     o                    o                       o
     *                o                    x                       o
     *                o                    o                       x
     *                o                2018-02-20
     */
    @Test
    public void calculateOccurrenceDateStartInRangeWithExDates() throws Exception {
        oddDayRecurrence = new WeeklyRecurrence.Builder(SDF.parse("2018-01-27"))
                .onSunday().onTuesday().onThursday()
                .setExDates(listOf("2018-02-06", "2018-02-15"))
                .build();

        List<Date> oddDayOccurrence = oddDayRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
        assertEquals(8, oddDayOccurrence.size());
        List<Date> expectedDates = listOf("2018-01-28", "2018-01-30", "2018-02-01",
                "2018-02-04", "2018-02-08",
                "2018-02-11", "2018-02-13",
                "2018-02-18"
        );

        assertEquals(expectedDates, oddDayOccurrence);
    }

    /**
     *  weekend: w
     *
     *  Saturday    Sunday    Monday    Thursday    Wednesday    Thursday    Friday
     * 2018-01-20                                                   w          w
     *                                                              w          w
     *                                                              w          w
     *                                                              w          w
     *                                 2018-02-20
     */
    @Test
    public void calculateOccurrenceDatesForWeekendDateRecurrence() throws Exception {
        List<Date> weekendOccurrence = weekendRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
        assertEquals(8, weekendOccurrence.size());
        List<Date> expectedDates = listOf("2018-01-25", "2018-01-26",
                "2018-02-01", "2018-02-02",
                "2018-02-08", "2018-02-09",
                "2018-02-15", "2018-02-16"
        );

        assertEquals(expectedDates, weekendOccurrence);
    }

    private List<Date> listOf(String... datesStr) throws ParseException {
        List<Date> dates = new ArrayList<>();
        for (String d : datesStr)
            dates.add(SDF.parse(d));

        return dates;
    }
}