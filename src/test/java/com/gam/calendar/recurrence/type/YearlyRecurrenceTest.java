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

class YearlyRecurrenceTest {
    private Recurrence yearlyRecurrence;
    private YearlyRecurrence.Builder builder;
    private Date recurrenceStartDate;

    @BeforeEach
    void setUp() throws Exception {
        String seventeenthOfBahman91 = "2013-02-05";
        setCalendarTimeForConstantTestTime(seventeenthOfBahman91);
        recurrenceStartDate = CAL.getTime();
        builder = new YearlyRecurrence.Builder(recurrenceStartDate);
    }

    @Test
    void createInstanceOfYearlyRecurrence() {
        yearlyRecurrence = builder.build();
        assertTrue(yearlyRecurrence instanceof YearlyRecurrence);
    }

    @Nested
    class WithOutEndDate {
        @BeforeEach
        void setUp() throws Exception {
            yearlyRecurrence = builder.build();
        }

        @Nested
        class CalculateOccurrenceDatesInRange {
            @Test
            void inTestRangeShouldReturnSeventeenthOfBahman97() throws Exception {
                List<Date> occurrenceDates = yearlyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                assertTrue(occurrenceDates.size() == 1);
                String seventeenthOfBahman97 = "2018-02-06";
                setCalendarTimeForConstantTestTime(seventeenthOfBahman97);
                assertEquals(CAL.getTime(), occurrenceDates.get(0));
            }
        }

        @Nested
        class CalculateOccurrenceDateAfter {
            @ParameterizedTest
            @ValueSource(strings = {"2013-02-05", "2013-02-04", "2013-01-05", "2010-02-05"})
            void whenGivenDateIsEqualOrBeforeRecurrenceBeginDateShouldReturnRecurrenceBeginDate(String beforeRecurrence)
                    throws Exception {
                setCalendarTimeForConstantTestTime(beforeRecurrence);
                assertEquals(recurrenceStartDate, getOccurrenceDateAfterCALDate());
            }

            @Test
            void facts() throws Exception {
                String seventeenthOfBahman92 = "2014-02-06";
                assertOccurrenceDateIsSameTillNextYear(seventeenthOfBahman92, 0);

                String seventeenthOfBahman95 = "2017-02-05";
                assertOccurrenceDateIsSameTillNextYear(seventeenthOfBahman95, 3);

                String seventeenthOfBahman96 = "2018-02-06";
                assertOccurrenceDateIsSameTillNextYear(seventeenthOfBahman96, 4);
            }

            private void assertOccurrenceDateIsSameTillNextYear(String expectedDate, int elapsedYear) throws Exception {
                setCalendarTimeForConstantTestTime(expectedDate);
                Date expected = CAL.getTime();

                CAL.setTime(recurrenceStartDate);
                CAL.add(Calendar.YEAR, elapsedYear);
                CAL.add(Calendar.HOUR_OF_DAY, 1);
                assertEquals(expected, getOccurrenceDateAfterCALDate());

                CAL.add(Calendar.DATE, 8);
                assertEquals(expected, getOccurrenceDateAfterCALDate());

                CAL.add(Calendar.MONTH, 4);
                assertEquals(expected, getOccurrenceDateAfterCALDate());
            }

            private Date getOccurrenceDateAfterCALDate() {
                return yearlyRecurrence.calculateOccurrenceDateAfter(CAL.getTime());
            }
        }
    }

    @Nested
    class WithEndDateInTestRange {
        private Date recurrenceEndDate;

        @BeforeEach
        void setUp() throws Exception {
            String twentiethOfBahman96 = "2018-02-01";
            setCalendarTimeForConstantTestTime(twentiethOfBahman96);
            recurrenceEndDate = CAL.getTime();

            yearlyRecurrence = builder
                    .setInterval(1)
                    .setRecurrenceEndDate(recurrenceEndDate)
                    .build();
        }

        @Nested
        class CalculateOccurrenceDatesInRange {
            @Test
            void inTestRangeShouldReturnEmptyList() {
                List<Date> occurrenceDates = yearlyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                assertTrue(occurrenceDates.isEmpty());
            }
        }

        @Nested
        class CalculateOccurrenceDateAfter {
            @Test
            void whenGivenDateIsAfterRecurrenceEndShouldReturnNull() {
                CAL.setTime(recurrenceEndDate);
                CAL.add(Calendar.HOUR_OF_DAY, 1);
                Date occurrenceDate = getOccurrenceDateAfterCALDate();
                assertNull(occurrenceDate);

                CAL.add(Calendar.DATE, 10);
                occurrenceDate = getOccurrenceDateAfterCALDate();
                assertNull(occurrenceDate);

                CAL.add(Calendar.MONTH, 6);
                occurrenceDate = getOccurrenceDateAfterCALDate();
                assertNull(occurrenceDate);

                CAL.add(Calendar.YEAR, 2);
                occurrenceDate = getOccurrenceDateAfterCALDate();
                assertNull(occurrenceDate);
            }

            private Date getOccurrenceDateAfterCALDate() {
                return yearlyRecurrence.calculateOccurrenceDateAfter(CAL.getTime());
            }
        }
    }

    @Nested
    class WithExDates {
        private Date exDate;

        @BeforeEach
        void setUp() throws Exception {
            String seventeenthOfBahman97 = "2018-02-06";
            setCalendarTimeForConstantTestTime(seventeenthOfBahman97);
            exDate = CAL.getTime();
            yearlyRecurrence = builder.addExDate(exDate).build();
        }

        @Nested
        class CalculateOccurrenceDatesInRange {
            @Test
            void inTestRangeShouldReturnEmptyList() {
                List<Date> occurrenceDates = yearlyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                assertTrue(occurrenceDates.isEmpty());
            }
        }

        @Nested
        class CalculateOccurrenceDateAfter {
            @Test
            void shouldReturnNextOccurrenceAfterExDate() throws Exception {
                setCalendarTimeForConstantTestTime("2018-01-06");
                Date occurrenceDate = yearlyRecurrence.calculateOccurrenceDateAfter(CAL.getTime());
                assertFalse(occurrenceDate.compareTo(exDate) == 0);
                setCalendarTimeForConstantTestTime("2019-02-06");
                assertEquals(CAL.getTime(), occurrenceDate);
            }
        }
    }

    @Nested
    class CalculateElapsedYear {
        private YearlyRecurrence yearlyRecurrence;

        @BeforeEach
        void setUp() throws Exception {
            setCalendarTimeForConstantTestTime("2017-05-26");
            recurrenceStartDate = CAL.getTime();
            yearlyRecurrence = (YearlyRecurrence) new YearlyRecurrence.Builder(recurrenceStartDate).build();
        }

        @ParameterizedTest
        @ValueSource(strings = {"2017-05-25", "2017-02-26", "2014-05-26"})
        void beforeRecurrenceBeginDateReturnZero(String beforeRecurrence) throws Exception {
            assertEquals(0, yearlyRecurrence.calculateElapsedUnit(SDF.parse(beforeRecurrence)));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 10, 100})
        void facts(int elapsedYearFromRecurrenceBeginDate) {
            int expectedElapsedYear = elapsedYearFromRecurrenceBeginDate + 1;
            assertElapsedYearIsSameTillNextYear(expectedElapsedYear, elapsedYearFromRecurrenceBeginDate);
        }

        private void assertElapsedYearIsSameTillNextYear(int expectedElapsedYear, int elapsedYearFromRecurrenceBeginDate) {
            CAL.setTime(recurrenceStartDate);
            CAL.add(Calendar.YEAR, elapsedYearFromRecurrenceBeginDate);
            CAL.add(Calendar.HOUR_OF_DAY, 1);
            assertEquals(expectedElapsedYear, yearlyRecurrence.calculateElapsedUnit(CAL.getTime()));
            CAL.add(Calendar.DATE, 10);
            assertEquals(expectedElapsedYear, yearlyRecurrence.calculateElapsedUnit(CAL.getTime()));
            CAL.add(Calendar.MONTH, 8);
            assertEquals(expectedElapsedYear, yearlyRecurrence.calculateElapsedUnit(CAL.getTime()));
        }
    }
}
