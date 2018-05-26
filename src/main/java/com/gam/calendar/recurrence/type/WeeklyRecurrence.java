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

    protected List<Date> findNextOccurrences(DateRange range) {
        List<Date> occurrenceInThisWeek = new ArrayList<>();
        for (int weekDayOrder : daysOfWeekOrder.keySet()) {
            calendar.set(Calendar.DAY_OF_WEEK, daysOfWeekOrder.get(weekDayOrder));
            if (isDateInRange(range) && !isDateInExDates())
                occurrenceInThisWeek.add(calendar.getTime());
        }

        return occurrenceInThisWeek;
    }

    protected Date findNextOccurrence(Date from) {
        for (int weekDayOrder : daysOfWeekOrder.keySet()) {
            calendar.set(Calendar.DAY_OF_WEEK, daysOfWeekOrder.get(weekDayOrder));
            if (!isDateInExDates()
                    && isDateInRange(new DateRange(getBeginDate(), getEndDate()))
                    && from.compareTo(calendar.getTime()) < 0)
                return calendar.getTime();
        }

        return null;
    }

    protected int calculateElapsedUnit(Date toDate) {
        calendar.setTime(toDate);
        setCalendarWithRecurrenceBeginTime();
        int elapsed = (int) ChronoUnit.WEEKS.between(
                getBeginDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
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
