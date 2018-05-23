package com.gam.calendar.recurrence.type;

import com.gam.calendar.recurrence.DateRange;
import com.gam.calendar.recurrence.RecurrenceBuilder;
import com.gam.calendar.recurrence.Recurrence;
import com.ibm.icu.util.Calendar;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 19/05/2018
 */
public class WeeklyRecurrence extends Recurrence {

    protected Map<Integer, Integer> daysOfWeekOrder;

    protected WeeklyRecurrence(WeeklyRecurrence.Builder builder) {
        super(builder);
        daysOfWeekOrder = builder.daysOfWeekOrder;
    }

    public List<Date> calculateOccurrenceDatesInRange(DateRange range) {
        List<Date> occurrenceDates = new ArrayList<>();
        int differenceWeek = calculateDifferenceUnit(range.getFromDate());
        addDifferenceUnitToRecurrenceBeginDate(differenceWeek);
        for (; isInUpperBound(calendar.getTime(), range.getToDate()); addDifferenceUnitToRecurrenceBeginDate(differenceWeek)) {
            for (int weekDayOrder : daysOfWeekOrder.keySet()) {
                calendar.set(Calendar.DAY_OF_WEEK, daysOfWeekOrder.get(weekDayOrder));
                if (isDateInRange(range) && !isDateInExDates())
                    occurrenceDates.add(calendar.getTime());
            }
            differenceWeek += interval;
        }

        return occurrenceDates;
    }

    protected int calculateElapsedUnit(Date toDate) {
        Calendar cal = getCalendarWithRecurrenceBeginTimeAndGivenDate(toDate);
        return (int) ChronoUnit.WEEKS.between(
                LocalDateTime.ofInstant(getBeginDate().toInstant(), ZoneId.systemDefault()),
                LocalDateTime.ofInstant(cal.getTime().toInstant(), ZoneId.systemDefault())
        );
    }

    protected void addDifferenceUnitToRecurrenceBeginDate(int differenceUnit) {
        calendar.setTime(getBeginDate());
        calendar.add(Calendar.WEEK_OF_YEAR, differenceUnit);
    }

    public static class Builder extends RecurrenceBuilder {

        protected Map<Integer, Integer> daysOfWeekOrder;

        public Builder(Date beginDate) {
            super(beginDate);
            daysOfWeekOrder = new HashMap<>();
        }

        public WeeklyRecurrence.Builder setRecurrenceEndDate(Date endDate) {
            range.setToDate(endDate);
            return this;
        }

        public WeeklyRecurrence.Builder setInterval(int interval) {
            if (interval <= 0)
                throw new IllegalBuilderArgumentException("interval can not be under zero. interval: " + interval);

            this.interval = interval;
            return this;
        }

        public WeeklyRecurrence.Builder addExDate(Date exDate) {
            requireNonNull(exDate, "can not add null to exDates.");
            exDates.add(exDate);
            return this;
        }

        public WeeklyRecurrence.Builder setExDates(List<Date> exDates) {
            requireNonNull(exDates, "exDates can not be null.");
            this.exDates = exDates;
            return this;
        }

        public WeeklyRecurrence.Builder onSaturday() {
            daysOfWeekOrder.put(0, Calendar.SATURDAY);
            return this;
        }

        public WeeklyRecurrence.Builder onSunday() {
            daysOfWeekOrder.put(1, Calendar.SUNDAY);
            return this;
        }

        public WeeklyRecurrence.Builder onMonday() {
            daysOfWeekOrder.put(2, Calendar.MONDAY);
            return this;
        }

        public WeeklyRecurrence.Builder onTuesday() {
            daysOfWeekOrder.put(3, Calendar.TUESDAY);
            return this;
        }

        public WeeklyRecurrence.Builder onWednesday() {
            daysOfWeekOrder.put(4, Calendar.WEDNESDAY);
            return this;
        }

        public WeeklyRecurrence.Builder onThursday() {
            daysOfWeekOrder.put(5, Calendar.THURSDAY);
            return this;
        }

        public WeeklyRecurrence.Builder onFriday() {
            daysOfWeekOrder.put(6, Calendar.FRIDAY);
            return this;
        }

        public Recurrence build() {
            if (daysOfWeekOrder.isEmpty())
                throw new IllegalBuilderArgumentException("must add at least one day of week.");

            return new WeeklyRecurrence(this);
        }
    }

}
