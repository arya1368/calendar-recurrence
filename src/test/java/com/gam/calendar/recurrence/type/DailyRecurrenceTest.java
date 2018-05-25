package com.gam.calendar.recurrence.type;

import com.gam.calendar.recurrence.Recurrence;
import com.ibm.icu.util.Calendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gam.calendar.recurrence.RecurrenceBuilder.DEFAULT_INTERVAL;
import static com.gam.calendar.recurrence.RecurrenceTestUtil.*;
import static org.junit.jupiter.api.Assertions.*;

class DailyRecurrenceTest {
    private Recurrence dailyRecurrence;
    private DailyRecurrence.Builder builder;
    private Date recurrenceStartDate;

    @BeforeEach
    void setUp() throws Exception {
        String twelfthOfDey96 = "2018-01-02";
        setCalendarTimeForConstantTestTime(twelfthOfDey96);
        recurrenceStartDate = CAL.getTime();
        builder = new DailyRecurrence.Builder(recurrenceStartDate);
    }

    @Test
    void createInstanceOfDailyRecurrence() {
        dailyRecurrence = builder.build();
        assertTrue(dailyRecurrence instanceof DailyRecurrence);
    }

    @Nested
    class WithOutEndDate {
        @BeforeEach
        void setUp() throws Exception {
            dailyRecurrence = builder.build();
        }

        @Nested
        class CalculateOccurrenceDatesInRange {
            @Test
            void mustContainsAllDayInTestRange() {
                List<Date> occurrenceDates = dailyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                assertTrue(occurrenceDates.size() == 30);
                CAL.setTime(RANGE_WITH_30_DAYS.getFromDate());
                setCalendarTimeForConstantTestTime();
                for (Date date : occurrenceDates) {
                    assertEquals(CAL.getTime(), date);
                    CAL.add(com.ibm.icu.util.Calendar.DATE, DEFAULT_INTERVAL);
                }
            }
        }

        @Nested
        class CalculateOccurrenceDateAfter {
            @ParameterizedTest
            @ValueSource(strings = {"2018-01-01", "2017-12-02", "2017-06-15", "2013-01-02"})
            void whenGivenDateIsBeforeRecurrenceBeginDateShouldReturnRecurrenceBeginDate(String beforeRecurrence)
                    throws Exception {
                setCalendarTimeForConstantTestTime(beforeRecurrence);
                assertEquals(recurrenceStartDate, dailyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));
            }

            @ParameterizedTest
            @ValueSource(strings = {"2018-01-02", "2018-01-12", "2018-8-05", "2019-09-22"})
            void facts(String lastRec) throws Exception {
                setCalendarTimeForConstantTestTime(lastRec);
                CAL.add(Calendar.DATE, 1);
                Date expected = CAL.getTime();

                setCalendarTimeForConstantTestTime(lastRec);
                assertEquals(expected, dailyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));
                CAL.add(Calendar.HOUR, 2);
                assertEquals(expected, dailyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));
                setCalendarTimeForEndOfDay();
                assertEquals(expected, dailyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));
            }
        }
    }

    @Nested
    class WithEndDateInTestRange {
        private Date recurrenceEndDate;

        @BeforeEach
        void setUp() throws Exception {
            String twentySecondOfBahman96 = "2018-02-11";
            setCalendarTimeForConstantTestTime(twentySecondOfBahman96);
            recurrenceEndDate = CAL.getTime();

            dailyRecurrence = builder.setRecurrenceEndDate(recurrenceEndDate).build();
        }

        @Nested
        class CalculateOccurrenceDatesInRange {
            @Test
            void inTestRangeShouldReturn22ExpectedDay() throws Exception {
                List<Date> occurrenceDates = dailyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                assertTrue(occurrenceDates.size() == 22);
                CAL.setTime(RANGE_WITH_30_DAYS.getFromDate());
                setCalendarTimeForConstantTestTime();
                for (Date date : occurrenceDates) {
                    assertEquals(CAL.getTime(), date);
                    CAL.add(com.ibm.icu.util.Calendar.DATE, DEFAULT_INTERVAL);
                }
            }
        }

        @Nested
        class CalculateOccurrenceDateAfter {
            @Test
            void whenGivenDateIsAfterRecurrenceEndShouldReturnNull() {
                CAL.setTime(recurrenceEndDate);
                CAL.add(Calendar.MILLISECOND, 1);
                Date occurrenceDate = dailyRecurrence.calculateOccurrenceDateAfter(CAL.getTime());
                assertNull(occurrenceDate);

                CAL.setTime(recurrenceEndDate);
                CAL.add(Calendar.SECOND, 4);
                occurrenceDate = dailyRecurrence.calculateOccurrenceDateAfter(CAL.getTime());
                assertNull(occurrenceDate);

                CAL.setTime(recurrenceEndDate);
                CAL.add(Calendar.DATE, 6);
                occurrenceDate = dailyRecurrence.calculateOccurrenceDateAfter(CAL.getTime());
                assertNull(occurrenceDate);
            }
        }
    }

    @Nested
    class WithExDates {
        private List<Date> exDates;

        @BeforeEach
        void setUp() throws Exception {
            String fifthOfBahman96 = "2018-01-25";
            String tenthOfBahman96 = "2018-01-30";
            String sixteenthOfBahman96 = "2018-02-05";
            initialExDates(fifthOfBahman96, tenthOfBahman96, sixteenthOfBahman96);
            dailyRecurrence = builder.setExDates(exDates).build();
        }

        private void initialExDates(String... dates) throws ParseException {
            exDates = new ArrayList<>();
            for (String d : dates) {
                setCalendarTimeForConstantTestTime(d);
                exDates.add(CAL.getTime());
            }
        }

        @Nested
        class CalculateOccurrenceDatesInRange {
            @Test
            void inTestRangeShouldReturn27ExpectedDay() throws Exception {
                List<Date> occurrenceDates = dailyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                assertTrue(occurrenceDates.size() == 27);
                CAL.setTime(RANGE_WITH_30_DAYS.getFromDate());
                setCalendarTimeForConstantTestTime();
                for (Date date : occurrenceDates) {
                    boolean isExDate = false;
                    for (Date ex : exDates) {
                        if (CAL.getTime().compareTo(ex) == 0) {
                            isExDate = true;
                            CAL.add(Calendar.DATE, DEFAULT_INTERVAL);
                            assertEquals(CAL.getTime(), date);
                            CAL.add(Calendar.DATE, DEFAULT_INTERVAL);
                        }
                    }
                    if (!isExDate) {
                        assertEquals(CAL.getTime(), date);
                        CAL.add(Calendar.DATE, DEFAULT_INTERVAL);
                    }
                }
            }
        }

        @Nested
        class CalculateOccurrenceDateAfter {
            @Test
            void shouldReturnNextOccurrenceAfterExDate() throws Exception {
                setCalendarTimeForConstantTestTime("2018-01-24");
                Date occurrenceDate = dailyRecurrence.calculateOccurrenceDateAfter(CAL.getTime());
                setCalendarTimeForConstantTestTime("2018-01-26");
                assertEquals(CAL.getTime(), occurrenceDate);

                setCalendarTimeForConstantTestTime("2018-01-29");
                occurrenceDate = dailyRecurrence.calculateOccurrenceDateAfter(CAL.getTime());
                setCalendarTimeForConstantTestTime("2018-01-31");
                assertEquals(CAL.getTime(), occurrenceDate);

                setCalendarTimeForConstantTestTime("2018-02-04");
                occurrenceDate = dailyRecurrence.calculateOccurrenceDateAfter(CAL.getTime());
                setCalendarTimeForConstantTestTime("2018-02-06");
                assertEquals(CAL.getTime(), occurrenceDate);
            }
        }
    }

    @Nested
    class WithInterval {
        private int interval = 3;

        @BeforeEach
        void setUp() throws Exception {
            dailyRecurrence = builder.setInterval(interval).build();
        }

        @Nested
        class CalculateOccurrenceDatesInRange {
            @Test
            void mustContainsExpectedDayInTestRange() throws Exception {
                List<Date> occurrenceDates = dailyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                assertTrue(occurrenceDates.size() == 10);
                String thirdOfBahman96 = "2018-01-23";
                setCalendarTimeForConstantTestTime(thirdOfBahman96);
                for (Date date : occurrenceDates) {
                    assertEquals(CAL.getTime(), date);
                    CAL.add(Calendar.DATE, interval);
                }
            }
        }

        @Nested
        class CalculateOccurrenceDateAfter {
            @Test
            void shouldReturnExpectedDateDependOnRecurrenceBeginDateAndInterval() throws Exception {
                setCalendarTimeForConstantTestTime("2018-01-02");
                Date occurrenceDate = dailyRecurrence.calculateOccurrenceDateAfter(CAL.getTime());
                setCalendarTimeForConstantTestTime("2018-01-05");
                assertEquals(CAL.getTime(), occurrenceDate);

                setCalendarTimeForConstantTestTime("2018-02-02");
                occurrenceDate = dailyRecurrence.calculateOccurrenceDateAfter(CAL.getTime());
                setCalendarTimeForConstantTestTime("2018-02-04");
                assertEquals(CAL.getTime(), occurrenceDate);

                setCalendarTimeForConstantTestTime("2018-02-18");
                occurrenceDate = dailyRecurrence.calculateOccurrenceDateAfter(CAL.getTime());
                setCalendarTimeForConstantTestTime("2018-02-19");
                assertEquals(CAL.getTime(), occurrenceDate);
            }
        }
    }

    @Nested
    class CalculateElapsedDay {
        private DailyRecurrence dailyRecurrence;

        @BeforeEach
        void setUp() throws Exception {
            dailyRecurrence = (DailyRecurrence) builder.build();
        }

        @ParameterizedTest
        @ValueSource(strings = {"2018-01-01", "2017-12-02", "2017-01-02"})
        void beforeRecurrenceBeginDateReturnZero(String beforeRecurrence) throws Exception {
            assertEquals(0, dailyRecurrence.calculateElapsedUnit(SDF.parse(beforeRecurrence)));
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 10, 10000})
        void facts(int elapsedDay) {
            assertElapsedDay(elapsedDay);
        }

        private void assertElapsedDay(int elapsedDay) {
            CAL.setTime(recurrenceStartDate);
            CAL.add(Calendar.DATE, elapsedDay);
            assertEquals(elapsedDay, calculateElapsedDay());
            CAL.add(Calendar.HOUR, -1);
            assertEquals(elapsedDay, calculateElapsedDay());
            CAL.add(Calendar.HOUR, 4);
            assertEquals(elapsedDay + 1, calculateElapsedDay());
        }

        private int calculateElapsedDay() {
            return dailyRecurrence.calculateElapsedUnit(CAL.getTime());
        }
    }
}
