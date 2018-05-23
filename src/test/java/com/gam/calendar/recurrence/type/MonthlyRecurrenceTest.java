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
public class MonthlyRecurrenceTest {

    private Recurrence monthlyRecurrence;

    @Before
    public void setUp() throws Exception {
        String thirtyFirstOfFarvardin = "2017-04-19";
        CAL.setTime(SDF.parse(thirtyFirstOfFarvardin));
        setCalendarTimeForConstantTestTime();
        monthlyRecurrence = new MonthlyRecurrence.Builder(CAL.getTime()).build();
    }

    @Test
    public void createInstanceOfMonthlyRecurrence() throws Exception {
        assertTrue(monthlyRecurrence instanceof MonthlyRecurrence);
    }

    @Test
    public void calculateOccurrenceDateInRage() throws Exception {
        List<Date> occurrenceDates = monthlyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
        assertEquals(1, occurrenceDates.size());
        String thirtiethOfBahman = "2018-02-19";
        CAL.setTime(SDF.parse(thirtiethOfBahman));
        setCalendarTimeForConstantTestTime();
        assertEquals(CAL.getTime(), occurrenceDates.get(0));
    }

    @Test
    public void calculateOccurrenceDateAfterFirstOfDey() throws Exception {
        Date occurrenceDate = monthlyRecurrence.calculateOccurrenceDateAfter(SDF.parse("2018-02-22"));
        String twentyNinthOfEsfand = "2018-03-20";
        CAL.setTime(SDF.parse(twentyNinthOfEsfand));
        setCalendarTimeForConstantTestTime();
        assertEquals(CAL.getTime(), occurrenceDate);
    }

    @Test
    public void calculateOccurrenceDateAfterFirstOfDeyWhenTwentyNinthOfEsfandIsExDate() throws Exception {
        CAL.setTime(SDF.parse("2018-03-20"));
        setCalendarTimeForConstantTestTime();
        Date exDate = CAL.getTime();
        String thirtyFirstOfFarvardin = "2017-04-19";
        CAL.setTime(SDF.parse(thirtyFirstOfFarvardin));
        setCalendarTimeForConstantTestTime();
        monthlyRecurrence = new MonthlyRecurrence.Builder(CAL.getTime())
                .addExDate(exDate).build();

        Date occurrenceDate = monthlyRecurrence.calculateOccurrenceDateAfter(SDF.parse("2018-02-22"));
        String thirtiethOfFarvardin = "2018-04-19";
        CAL.setTime(SDF.parse(thirtiethOfFarvardin));
        setCalendarTimeForConstantTestTime();
        assertEquals(CAL.getTime(), occurrenceDate);
    }

    @Test
    public void calculateOccurrenceDateReturnNullWhenRecurrenceEndBeforeFindAnyDate() throws Exception {
        String thirtyFirstOfFarvardin = "2017-04-19";
        CAL.setTime(SDF.parse(thirtyFirstOfFarvardin));
        setCalendarTimeForConstantTestTime();
        monthlyRecurrence = new MonthlyRecurrence.Builder(CAL.getTime()).setRecurrenceEndDate(SDF.parse("2017-09-20")).build();
        Date occurrenceDate = monthlyRecurrence.calculateOccurrenceDateAfter(RANGE_WITH_30_DAYS.getFromDate());
        assertNull(occurrenceDate);
    }


}