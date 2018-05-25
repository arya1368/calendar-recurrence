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

    protected WeeklyRecurrence(WeeklyRecurrence.Builder builder) {
        super(builder);
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

    public Date calculateOccurrenceDateAfter(Date from) {
        int differenceUnit = calculateDifferenceUnit(from);
        addDifferenceUnitToRecurrenceBeginDate(differenceUnit);
        for (; isInUpperBound(calendar.getTime(), getEndDate()); addDifferenceUnitToRecurrenceBeginDate(differenceUnit)) {
            for (int weekDayOrder : daysOfWeekOrder.keySet()) {
                calendar.set(Calendar.DAY_OF_WEEK, daysOfWeekOrder.get(weekDayOrder));
                if (!isDateInExDates() && isDateInRange(new DateRange(getBeginDate(), getEndDate()))
                        && from.compareTo(calendar.getTime()) < 0)
                    return calendar.getTime();
            }
            differenceUnit += interval;
        }

        return null;
    }

    protected int calculateElapsedUnit(Date toDate) {
        calendar.setTime(toDate);
        setCalendarWithRecurrenceBeginTime();
        int elapsed = (int) ChronoUnit.WEEKS.between(
                LocalDateTime.ofInstant(getBeginDate().toInstant(), ZoneId.systemDefault()),
                LocalDateTime.ofInstant(calendar.getTime().toInstant(), ZoneId.systemDefault())
        );
        return elapsed < 0 ? 0 : elapsed;
    }

    protected void addDifferenceUnitToRecurrenceBeginDate(int differenceUnit) {
        calendar.setTime(getBeginDate());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        calendar.add(Calendar.WEEK_OF_YEAR, differenceUnit);
    }

    public static class Builder extends RecurrenceBuilder {

        public Builder(Date beginDate) {
            super(beginDate);
        }

        public Recurrence build() {
            if (daysOfWeekOrder.isEmpty())
                throw new IllegalBuilderArgumentException("must add at least one day of week.");

            return new WeeklyRecurrence(this);
        }
    }

}
