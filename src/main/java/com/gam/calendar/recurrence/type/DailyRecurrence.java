package com.gam.calendar.recurrence.type;

import com.gam.calendar.recurrence.RecurrenceBuilder;
import com.gam.calendar.recurrence.Recurrence;
import com.ibm.icu.util.Calendar;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 16/05/2018
 */
public class DailyRecurrence extends Recurrence {

    private DailyRecurrence(DailyRecurrence.Builder builder) {
        super(builder);
    }

    protected void addDifferenceUnitToRecurrenceBeginDate(int differenceUnit) {
        calendar.setTime(getBeginDate());
        calendar.add(Calendar.DATE, differenceUnit);
    }

    protected int calculateElapsedUnit(Date toDate) {
        Calendar cal = getCalendarWithRecurrenceBeginTimeAndGivenDate(toDate);
        return (int) ChronoUnit.DAYS.between(
                LocalDateTime.ofInstant(getBeginDate().toInstant(), ZoneId.systemDefault()),
                LocalDateTime.ofInstant(cal.getTime().toInstant(), ZoneId.systemDefault())
        );
    }

    public static class Builder extends RecurrenceBuilder {

        public Builder(Date beginDate) {
            super(beginDate);
        }

        public Recurrence build() {
            return new DailyRecurrence(this);
        }
    }
}
