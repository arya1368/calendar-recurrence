package com.gam.calendar.recurrence.type;

import com.gam.calendar.recurrence.Recurrence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.gam.calendar.recurrence.RecurrenceTestUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class MonthlyRecurrenceTest {
    private Recurrence monthlyRecurrence;
    private MonthlyRecurrence.Builder builder;
    private Date recurrenceStartDate;

    @BeforeEach
    void setUp() throws Exception {
        String thirtyFirstOfFarvardin96 = "2017-04-20";
        setCalendarTimeForConstantTestTime(thirtyFirstOfFarvardin96);
        recurrenceStartDate = CAL.getTime();
        builder = new MonthlyRecurrence.Builder(recurrenceStartDate);
    }

    @Test
    void createInstanceOfMonthlyRecurrence() {
        monthlyRecurrence = builder.build();
        assertTrue(monthlyRecurrence instanceof MonthlyRecurrence);
    }

    @Nested
    class WithOutEndDate {
        @BeforeEach
        void setUp() {
            monthlyRecurrence = builder.build();
        }

        @Nested
        class CalculateOccurrenceDatesInRange {
            @Test
            void inTestRangeShouldReturnThirtiethOfBahman() throws Exception {
                List<Date> occurrenceDate = monthlyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                assertTrue(occurrenceDate.size() == 1);
                String thirtiethOfBahman = "2018-02-19";
                setCalendarTimeForConstantTestTime(thirtiethOfBahman);
                assertEquals(CAL.getTime(), occurrenceDate.get(0));
            }
        }

        @Nested
        class CalculateOccurrenceDateAfter {
            @ParameterizedTest
            @ValueSource(strings = {"2017-04-19", "2017-04-18", "2017-02-19", "2012-04-19"})
            void whenGivenDateIsBeforeRecurrenceBeginDateShouldReturnRecurrenceBeginDate(String beforeRecurrence)
                    throws Exception {
                setCalendarTimeForConstantTestTime(beforeRecurrence);
                assertEquals(recurrenceStartDate, getOccurrenceDateAfterCALDate());
            }

            @Test
            void facts() throws Exception {
                String thirtyFirstOfOrdibehesht96 = "2017-05-21";
                assertOccurrenceDateIsSameTillNextMonth(thirtyFirstOfOrdibehesht96, 0);

                String thirtiethOfMehr96 = "2017-10-22";
                assertOccurrenceDateIsSameTillNextMonth(thirtiethOfMehr96, 5);

                String thirtyFirstOfFarvardin97 = "2018-04-20";
                assertOccurrenceDateIsSameTillNextMonth(thirtyFirstOfFarvardin97, 11);

                String twentyNinthOfEsfand97 = "2019-03-20";
                assertOccurrenceDateIsSameTillNextMonth(twentyNinthOfEsfand97, 22);
            }

            private void assertOccurrenceDateIsSameTillNextMonth(String expectedDate, int elapsedMonth) throws Exception {
                setCalendarTimeForConstantTestTime(expectedDate);
                Date expected = CAL.getTime();

                CAL.setTime(recurrenceStartDate);
                CAL.add(Calendar.MONTH, elapsedMonth);
                CAL.add(Calendar.HOUR_OF_DAY, 3);
                assertEquals(expected, getOccurrenceDateAfterCALDate());

                CAL.add(Calendar.DATE, 8);
                assertEquals(expected, getOccurrenceDateAfterCALDate());
            }

            private Date getOccurrenceDateAfterCALDate() {
                return monthlyRecurrence.calculateOccurrenceDateAfter(CAL.getTime());
            }
        }
    }

    @Nested
    class WithEndDateInTestRange {
        private Date recurrenceEndDate;

        @BeforeEach
        void setUp() throws Exception {
            String twentyNinthOfBahman96 = "2018-02-18";
            setCalendarTimeForConstantTestTime(twentyNinthOfBahman96);
            recurrenceEndDate = CAL.getTime();

            monthlyRecurrence = builder.setRecurrenceEndDate(recurrenceEndDate).build();
        }

        @Nested
        class CalculateOccurrenceDatesInRange {
            @Test
            void inTestRangeShouldReturnEmptyList() {
                List<Date> occurrenceDates = monthlyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                assertTrue(occurrenceDates.isEmpty());
            }
        }

        @Nested
        class CalculateOccurrenceDateAfter {
            @Test
            void whenGivenDateIsAfterRecurrenceEndShouldReturnNull() {
                CAL.setTime(recurrenceEndDate);
                CAL.add(Calendar.MILLISECOND, 1);
                Date occurrenceDate = getOccurrenceDateAfterCALDate();
                assertNull(occurrenceDate);

                CAL.setTime(recurrenceEndDate);
                CAL.add(Calendar.SECOND, 4);
                occurrenceDate = getOccurrenceDateAfterCALDate();
                assertNull(occurrenceDate);

                CAL.setTime(recurrenceEndDate);
                CAL.add(Calendar.DATE, 6);
                occurrenceDate = getOccurrenceDateAfterCALDate();
                assertNull(occurrenceDate);

                CAL.setTime(recurrenceEndDate);
                CAL.add(Calendar.WEEK_OF_MONTH, 1);
                occurrenceDate = getOccurrenceDateAfterCALDate();
                assertNull(occurrenceDate);
            }

            private Date getOccurrenceDateAfterCALDate() {
                return monthlyRecurrence.calculateOccurrenceDateAfter(CAL.getTime());
            }
        }
    }

    @Nested
    class WithExDates {
        private Date exDate;

        @BeforeEach
        void setUp() throws Exception {
            String thirtiethOfBahman96 = "2018-02-19";
            setCalendarTimeForConstantTestTime(thirtiethOfBahman96);
            exDate = CAL.getTime();
            monthlyRecurrence = builder.addExDate(exDate).build();
        }

        @Nested
        class CalculateOccurrenceDatesInRange {
            @Test
            void inTestRangeShouldReturnEmptyList() {
                List<Date> occurrenceDates = monthlyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                assertTrue(occurrenceDates.isEmpty());
            }
        }

        @Nested
        class CalculateOccurrenceDateAfter {
            @Test
            void shouldReturnNextOccurrenceAfterExDate() throws Exception {
                setCalendarTimeForConstantTestTime("2018-02-01");
                Date occurrenceDate = monthlyRecurrence.calculateOccurrenceDateAfter(CAL.getTime());
                assertFalse(occurrenceDate.compareTo(exDate) == 0);
                String twentyNinthOfEsfand96 = "2018-03-20";
                setCalendarTimeForConstantTestTime(twentyNinthOfEsfand96);
                assertEquals(CAL.getTime(), occurrenceDate);
            }
        }
    }

    @Nested
    class CalculateElapsedMonth {
        private MonthlyRecurrence monthlyRecurrence;

        @BeforeEach
        void setUp() throws Exception {
            monthlyRecurrence = (MonthlyRecurrence) builder.build();
        }

        @ParameterizedTest
        @ValueSource(strings = {"2017-04-18", "2017-02-19", "2013-04-19"})
        void beforeRecurrenceBeginDateReturnZero(String beforeRecurrence) throws Exception {
            assertEquals(0, monthlyRecurrence.calculateElapsedUnit(SDF.parse(beforeRecurrence)));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 10, 10000})
        void facts(int elapsedMonthFromRecurrenceBeginDate) {
            int expectedElapsedMonth = elapsedMonthFromRecurrenceBeginDate + 1;
            assertElapsedMonthIsSameTillNextMonth(expectedElapsedMonth, elapsedMonthFromRecurrenceBeginDate);
        }

        private void assertElapsedMonthIsSameTillNextMonth(int expectedElapsedMonth,int elapsedMonthFromRecurrenceBeginDate) {
            addMonthToRecurrenceBeginDate(elapsedMonthFromRecurrenceBeginDate);
            CAL.add(Calendar.MILLISECOND, 1);
            assertEquals(expectedElapsedMonth, calculateElapsedMonth());

            addMonthToRecurrenceBeginDate(elapsedMonthFromRecurrenceBeginDate);
            CAL.add(Calendar.SECOND, 10);
            assertEquals(expectedElapsedMonth, calculateElapsedMonth());

            addMonthToRecurrenceBeginDate(elapsedMonthFromRecurrenceBeginDate);
            CAL.add(Calendar.MINUTE, 2);
            assertEquals(expectedElapsedMonth, calculateElapsedMonth());

            addMonthToRecurrenceBeginDate(elapsedMonthFromRecurrenceBeginDate);
            CAL.add(Calendar.HOUR_OF_DAY, 2);
            assertEquals(expectedElapsedMonth, calculateElapsedMonth());

            addMonthToRecurrenceBeginDate(elapsedMonthFromRecurrenceBeginDate);
            CAL.add(Calendar.DATE, 4);
            assertEquals(expectedElapsedMonth, calculateElapsedMonth());
        }

        private void addMonthToRecurrenceBeginDate(int n) {
            CAL.setTime(recurrenceStartDate);
            CAL.add(Calendar.MONTH, n);
        }

        private int calculateElapsedMonth() {
            return monthlyRecurrence.calculateElapsedUnit(CAL.getTime());
        }
    }
}
