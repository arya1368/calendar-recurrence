package com.gam.calendar.recurrence.type;

import com.gam.calendar.recurrence.RecurrenceBuilder;
import com.gam.calendar.recurrence.Recurrence;
import com.ibm.icu.util.Calendar;

import java.time.LocalDate;
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
        calendar.setTime(toDate);
        double toHour = getCalendarHourIncludeMinuteSecondMilliSecond();
        setCalendarWithRecurrenceBeginTime();
        int elapsed = (int) ChronoUnit.DAYS.between(
                LocalDateTime.ofInstant(getBeginDate().toInstant(), ZoneId.systemDefault()),
                LocalDateTime.ofInstant(calendar.getTime().toInstant(), ZoneId.systemDefault())
        );
        calendar.setTime(getBeginDate());
        double fromHour = getCalendarHourIncludeMinuteSecondMilliSecond();

        if (elapsed < 0)
            return 0;

        return fromHour >= toHour ? elapsed : ++elapsed;
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
