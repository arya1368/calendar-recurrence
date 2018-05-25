package com.gam.calendar.recurrence.type;

import com.gam.calendar.recurrence.Recurrence;
import com.gam.calendar.recurrence.RecurrenceBuilder;
import com.ibm.icu.util.Calendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gam.calendar.recurrence.RecurrenceTestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WeeklyRecurrenceTest {
    private Recurrence weeklyRecurrence;
    private RecurrenceBuilder builder;
    private Date recurrenceStartDate;

    @BeforeEach
    void setUp() throws Exception {
        String firstOfDey96 = "2017-12-22";
        setCalendarTimeForConstantTestTime(firstOfDey96);
        recurrenceStartDate = CAL.getTime();
        builder = new WeeklyRecurrence.Builder(recurrenceStartDate);
    }

    private List<Date> listOf(String... dateStr) throws Exception {
        List<Date> dates = new ArrayList<>();
        for (String d : dateStr) {
            setCalendarTimeForConstantTestTime(d);
            dates.add(CAL.getTime());
        }

        return dates;
    }

    @Test
    void createInstanceOfDailyRecurrence() {
        weeklyRecurrence = builder.onSaturday().build();
        assertTrue(weeklyRecurrence instanceof WeeklyRecurrence);
    }

    @Nested
    class OnEvenDates {
        @BeforeEach
        void setUp() throws Exception {
            builder = builder.onSaturday().onMonday().onWednesday();
        }

        @Nested
        class WithOutEndDate {
            @BeforeEach
            void setUp() {
                weeklyRecurrence = builder.build();
            }

            @Nested
            class CalculateOccurrenceDatesInRange {
                @Test
                void mustContainsAllEvenDateInTestRange() throws Exception {
                    List<Date> occurrenceDates = weeklyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                    assertTrue(occurrenceDates.size() == 13);
                    List<Date> expectedDates = listOf("2018-01-22", "2018-01-24",
                            "2018-01-27", "2018-01-29", "2018-01-31",
                            "2018-02-03", "2018-02-05", "2018-02-07",
                            "2018-02-10", "2018-02-12", "2018-02-14",
                            "2018-02-17", "2018-02-19");
                    assertTrue(occurrenceDates.containsAll(expectedDates));
                }
            }

            @Nested
            class CalculateOccurrenceDateAfter {
                @ParameterizedTest
                @ValueSource(strings = {"2017-12-21", "2017-12-12", "2017-08-12", "2013-12-09"})
                void whenGivenDateIsBeforeRecurrenceBeginDateShouldReturnRecurrenceBeginDate(String beforeRecurrence)
                        throws Exception {
                    String saturdaySecondOfBahman96 = "2017-12-23";
                    setCalendarTimeForConstantTestTime(saturdaySecondOfBahman96);
                    Date firstOccurrenceInRec = CAL.getTime();
                    setCalendarTimeForConstantTestTime(beforeRecurrence);
                    assertEquals(firstOccurrenceInRec, weeklyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));
                }

                @Test
                void facts() throws Exception {
                    String mondayForthOfBahman96 = "2017-12-25";
                    setCalendarTimeForConstantTestTime(mondayForthOfBahman96);
                    Date expected = CAL.getTime();
                    String saturdaySecondOfBahman96 = "2017-12-23";
                    setCalendarTimeForConstantTestTime(saturdaySecondOfBahman96);
                    assertEquals(expected, weeklyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));

                    String saturdaySixteenthOfBahman96 = "2018-01-06";
                    setCalendarTimeForConstantTestTime(saturdaySixteenthOfBahman96);
                    expected = CAL.getTime();
                    String thursdayFourteenthOfBahman96 = "2018-01-04";
                    setCalendarTimeForConstantTestTime(thursdayFourteenthOfBahman96);
                    assertEquals(expected, weeklyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));

                    String wednesdayForthOfMehr97 = "2018-09-26";
                    setCalendarTimeForConstantTestTime(wednesdayForthOfMehr97);
                    expected = CAL.getTime();
                    setCalendarTimeForStartOfDay();
                    assertEquals(expected, weeklyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));
                }
            }
        }

        @Nested
        class WithEndDateInTestRange {
            private Date recurrenceEndDate;

            @BeforeEach
            void setUp() throws Exception {
                String saturdayFourteenthOfBahman96 = "2018-02-03";
                setCalendarTimeForConstantTestTime(saturdayFourteenthOfBahman96);
                recurrenceEndDate = CAL.getTime();
                weeklyRecurrence = builder.setRecurrenceEndDate(recurrenceEndDate).build();
            }

            @Nested
            class CalculateOccurrenceDatesInRange {
                @Test
                void inTestRangeShouldReturn6Dates() throws Exception {
                    List<Date> occurrenceDates = weeklyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                    assertTrue(occurrenceDates.size() == 6);
                    List<Date> expectedDates = listOf("2018-01-22", "2018-01-24",
                            "2018-01-27", "2018-01-29", "2018-01-31",
                            "2018-02-03");
                    assertTrue(occurrenceDates.containsAll(expectedDates));
                }
            }

            @Nested
            class CalculateOccurrenceDateAfter {
                @Test
                void whenGivenDateIsAfterRecurrenceEndShouldReturnNull() {
                    CAL.setTime(recurrenceEndDate);
                    CAL.add(Calendar.MILLISECOND, 1);
                    assertNull(weeklyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));

                    CAL.setTime(recurrenceEndDate);
                    CAL.add(Calendar.SECOND, 10);
                    assertNull(weeklyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));

                    CAL.setTime(recurrenceEndDate);
                    CAL.add(Calendar.DATE, 4);
                    assertNull(weeklyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));

                    CAL.setTime(recurrenceEndDate);
                    CAL.add(Calendar.WEEK_OF_YEAR, 4);
                    assertNull(weeklyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));
                }
            }
        }

        @Nested
        class WithExDates {
            private List<Date> exDates;

            @BeforeEach
            void setUp() throws Exception {
                String mondaySecondOfBahman96 = "2018-01-22";
                String mondaySixteenthOfBhaman96 = "2018-02-05";
                String wednesdayTwentyFifthOfBhaman96 = "2018-02-14";
                exDates = listOf(mondaySecondOfBahman96, mondaySixteenthOfBhaman96, wednesdayTwentyFifthOfBhaman96);
                weeklyRecurrence = builder.setExDates(exDates).build();
            }

            @Nested
            class CalculateOccurrenceDatesInRange {
                @Test
                void mustContainsAllEvenDateInTestRange() throws Exception {
                    List<Date> occurrenceDates = weeklyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                    assertTrue(occurrenceDates.size() == 10);
                    List<Date> expectedDates = listOf("2018-01-24",
                            "2018-01-27", "2018-01-29", "2018-01-31",
                            "2018-02-03", "2018-02-07",
                            "2018-02-10", "2018-02-12",
                            "2018-02-17", "2018-02-19");
                    assertTrue(occurrenceDates.containsAll(expectedDates));
                }
            }

            @Nested
            class CalculateOccurrenceDateAfter {
                @Test
                void shouldReturnNextOccurrenceAfterExDate() throws Exception {
                    String wednesdayFourthOfBahman96 = "2018-01-24";
                    setCalendarTimeForConstantTestTime(wednesdayFourthOfBahman96);
                    Date expected = CAL.getTime();
                    setCalendarTimeForConstantTestTime("2018-01-21");
                    assertEquals(expected, weeklyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));

                    String saturdayTwentyEighthOfBahman96 = "2018-02-17";
                    setCalendarTimeForConstantTestTime(saturdayTwentyEighthOfBahman96);
                    expected = CAL.getTime();
                    setCalendarTimeForConstantTestTime("2018-02-12");
                    assertEquals(expected, weeklyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));
                }
            }
        }

        @Nested
        class WithInterval {
            private int interval = 2;

            @BeforeEach
            void setUp() {
                weeklyRecurrence = builder.setInterval(interval).build();
            }

            @Nested
            class CalculateOccurrenceDatesInRange {
                @Test
                void mustContainsAllEvenDateInTestRange() throws Exception {
                    List<Date> occurrenceDates = weeklyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                    assertTrue(occurrenceDates.size() == 6);
                    List<Date> expectedDates = listOf("2018-01-27", "2018-01-29", "2018-01-31",
                            "2018-02-10", "2018-02-12", "2018-02-14");
                    assertTrue(occurrenceDates.containsAll(expectedDates));
                }
            }

            @Nested
            class CalculateOccurrenceDateAfter {
                @Test
                void ifGivenDateIsLastOccurrenceInWeekShouldReturnFirstOfNextInterval() throws Exception {
                    String mondayNinthOfBahman96 = "2018-01-29";
                    setCalendarTimeForConstantTestTime(mondayNinthOfBahman96);
                    Date expected = CAL.getTime();
                    String saturdaySeventhOfBahman96 = "2018-01-27";
                    setCalendarTimeForConstantTestTime(saturdaySeventhOfBahman96);
                    assertEquals(expected, weeklyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));

                    String saturdayTwentyFirstOfbahman96 = "2018-02-10";
                    setCalendarTimeForConstantTestTime(saturdayTwentyFirstOfbahman96);
                    expected = CAL.getTime();
                    String wednesdayEleventhOfBahman96 = "2018-01-31";
                    setCalendarTimeForConstantTestTime(wednesdayEleventhOfBahman96);
                    assertEquals(expected, weeklyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));
                }
            }
        }
    }

    @Nested
    class OnWeekends {
        @BeforeEach
        void setUp() throws Exception {
            builder = builder.onThursday().onFriday();
        }

        @Nested
        class WithOutEndDate {
            @BeforeEach
            void setUp() {
                weeklyRecurrence = builder.build();
            }

            @Nested
            class CalculateOccurrenceDatesInRange {
                @Test
                void mustContainsAllWeekendsInTestRange() throws Exception {
                    List<Date> occurrenceDates = weeklyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                    assertTrue(occurrenceDates.size() == 8);
                    List<Date> expectedDates = listOf("2018-01-25", "2018-01-26",
                            "2018-02-01", "2018-02-02",
                            "2018-02-08", "2018-02-09",
                            "2018-02-15", "2018-02-16");
                    assertTrue(occurrenceDates.containsAll(expectedDates));
                }
            }

            @Nested
            class CalculateOccurrenceDateAfter {
                @Test
                void facts() throws Exception {
                    String fridayThirdOfFarvardin97 = "2018-03-23";
                    setCalendarTimeForConstantTestTime(fridayThirdOfFarvardin97);
                    Date expected = CAL.getTime();
                    String thursdaySecondOfFarvardin97 = "2018-03-22";
                    setCalendarTimeForConstantTestTime(thursdaySecondOfFarvardin97);
                    assertEquals(expected, weeklyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));

                    String thursdayNinthOfFarvardin97 = "2018-03-29";
                    setCalendarTimeForConstantTestTime(thursdayNinthOfFarvardin97);
                    expected = CAL.getTime();
                    String mondaySixthOfFarvardin97 = "2018-03-26";
                    setCalendarTimeForConstantTestTime(mondaySixthOfFarvardin97);
                    assertEquals(expected, weeklyRecurrence.calculateOccurrenceDateAfter(CAL.getTime()));
                }
            }
        }

        @Nested
        class WithEndDateInTestRange {
            private Date recurrenceEndDate;

            @BeforeEach
            void setUp() throws Exception {
                String saturdayFourteenthOfBahman96 = "2018-02-03";
                setCalendarTimeForConstantTestTime(saturdayFourteenthOfBahman96);
                recurrenceEndDate = CAL.getTime();
                weeklyRecurrence = builder.setRecurrenceEndDate(recurrenceEndDate).build();
            }

            @Nested
            class CalculateOccurrenceDatesInRange {
                @Test
                void inTestRangeShouldReturn4Dates() throws Exception {
                    List<Date> occurrenceDates = weeklyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                    assertTrue(occurrenceDates.size() == 4);
                    List<Date> expectedDates = listOf("2018-01-25", "2018-01-26",
                            "2018-02-01", "2018-02-02");
                    assertTrue(occurrenceDates.containsAll(expectedDates));
                }
            }
        }

        @Nested
        class WithIntervalAndExDate {
            private Date exDate;
            private int interval = 3;

            @BeforeEach
            void setUp() throws Exception {
                String thursdayTwelfthOfBahman96 = "2018-02-01";
                setCalendarTimeForConstantTestTime(thursdayTwelfthOfBahman96);
                exDate = CAL.getTime();
                weeklyRecurrence = builder.setInterval(interval).addExDate(exDate).build();
            }

            @Test
            void mustContainsExpectedWeekendsInTestRange() throws Exception {
                List<Date> occurrenceDates = weeklyRecurrence.calculateOccurrenceDatesInRange(RANGE_WITH_30_DAYS);
                assertTrue(occurrenceDates.size() == 1);
                String fridayThirteenthOfBahman96 = "2018-02-02";
                setCalendarTimeForConstantTestTime(fridayThirteenthOfBahman96);
                assertEquals(CAL.getTime(), occurrenceDates.get(0));
            }
        }
    }

    @Nested
    class CalculateElapsedWeek {
        private WeeklyRecurrence weeklyRecurrence;

        @BeforeEach
        void setUp() {
            weeklyRecurrence = (WeeklyRecurrence) builder.onSaturday().build();
        }

        @ParameterizedTest
        @ValueSource(strings = {"2017-12-21", "2017-12-12", "2017-08-12", "2013-12-09"})
        void beforeRecurrenceBeginDateReturnZero(String beforeRecurrence) throws Exception {
            assertEquals(0, weeklyRecurrence.calculateElapsedUnit(SDF.parse(beforeRecurrence)));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 6, 13, 10000})
        void facts(int elapsedWeek) throws Exception {
            CAL.setTime(recurrenceStartDate);
            CAL.add(Calendar.WEEK_OF_YEAR, elapsedWeek);
            CAL.add(Calendar.HOUR, 1);
            assertEquals(elapsedWeek, weeklyRecurrence.calculateElapsedUnit(CAL.getTime()));
            CAL.add(Calendar.DATE, 4);
            assertEquals(elapsedWeek, weeklyRecurrence.calculateElapsedUnit(CAL.getTime()));
            CAL.add(Calendar.DATE, 4);
            assertEquals(elapsedWeek + 1, weeklyRecurrence.calculateElapsedUnit(CAL.getTime()));
        }
    }
}
