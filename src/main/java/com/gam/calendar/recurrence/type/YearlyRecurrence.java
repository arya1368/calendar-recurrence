package com.gam.calendar.recurrence.type;

import com.gam.calendar.recurrence.RecurrenceBuilder;
import com.gam.calendar.recurrence.Recurrence;
import com.ibm.icu.util.Calendar;

import java.util.Date;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 19/05/2018
 */
public class YearlyRecurrence extends Recurrence {

    protected YearlyRecurrence(YearlyRecurrence.Builder builder) {
        super(builder);
    }

    protected int calculateElapsedUnit(Date toDate) {
        calendar.setTime(getBeginDate());
        int fromYear = calendar.get(Calendar.YEAR);
        int fromMonth = calendar.get(Calendar.MONTH);
        double fromDay = calendar.get(Calendar.DATE) + calendar.get(Calendar.HOUR_OF_DAY)/24.0;

        calendar.setTime(toDate);
        int toYear = calendar.get(Calendar.YEAR);
        int toMonth = calendar.get(Calendar.MONTH);
        double toDay = calendar.get(Calendar.DATE) + calendar.get(Calendar.HOUR_OF_DAY)/24.0;

        if (fromYear > toYear)
            return 0;

        int elapsedYear = toYear - fromYear;

        if (fromMonth > toMonth)
            return elapsedYear;

        if (fromMonth == toMonth)
            return fromDay >= toDay ? elapsedYear : ++elapsedYear;

        return ++elapsedYear;
    }

    protected void addDifferenceUnitToRecurrenceBeginDate(int differenceUnit) {
        calendar.setTime(getBeginDate());
        calendar.add(Calendar.YEAR, differenceUnit);
    }

    public static class Builder extends RecurrenceBuilder {

        public Builder(Date beginDate) {
            super(beginDate);
        }

        public Recurrence build() {
            return new YearlyRecurrence(this);
        }
    }
}
