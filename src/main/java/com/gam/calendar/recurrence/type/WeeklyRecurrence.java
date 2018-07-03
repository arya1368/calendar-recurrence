package com.gam.calendar.recurrence.type;

import com.gam.calendar.recurrence.DateRange;
import com.gam.calendar.recurrence.Recurrence;
import com.gam.calendar.recurrence.RecurrenceBuilder;
import com.ibm.icu.util.Calendar;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 19/05/2018
 */
public class WeeklyRecurrence extends Recurrence {

    protected WeeklyRecurrence(WeeklyRecurrence.Builder builder) {
        super(builder);
    }

    @Override
    protected List<Date> findNextOccurrences(DateRange range) {
        List<Date> occurrenceInThisWeek = new ArrayList<>();
        for (Map.Entry<Integer, Integer> dayWithOrder : daysOfWeekOrder.entrySet()) {
            calendar.set(Calendar.DAY_OF_WEEK, dayWithOrder.getValue());
            if (isDateInRange(range) && !isDateInExDates())
                occurrenceInThisWeek.add(calendar.getTime());
        }

        return occurrenceInThisWeek;
    }

    @Override
    protected Date findNextOccurrence(Date from) {
        for (Map.Entry<Integer, Integer> dayWithOrder : daysOfWeekOrder.entrySet()) {
            calendar.set(Calendar.DAY_OF_WEEK, dayWithOrder.getValue());

            if (!isDateInExDates()
                    && isDateInRange(new DateRange(getBeginDate(), getEndDate()))
                    && from.compareTo(calendar.getTime()) < 0)
                return calendar.getTime();
        }

        return null;
    }

    @Override
    protected int calculateElapsedUnit(Date toDate) {
        calendar.setTime(toDate);
        setCalendarWithRecurrenceBeginTime();
        int elapsed = (int) ChronoUnit.WEEKS.between(
                getBeginDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        );
        return elapsed < 0 ? 0 : elapsed;
    }

    @Override
    protected void addDifferenceUnitToRecurrenceBeginDate(int differenceUnit) {
        calendar.setTime(getBeginDate());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        calendar.add(Calendar.WEEK_OF_YEAR, differenceUnit);
    }

    public static class Builder extends RecurrenceBuilder {

        public Builder(Date beginDate) {
            super(beginDate);
        }

        @Override
        public Recurrence build() {
            if (daysOfWeekOrder.isEmpty())
                throw new IllegalBuilderArgumentException("must add at least one day of week.");

            return new WeeklyRecurrence(this);
        }
    }

}
