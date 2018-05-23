package com.gam.calendar.recurrence.type;

import com.gam.calendar.recurrence.RecurrenceBuilder;
import com.gam.calendar.recurrence.Recurrence;
import com.ibm.icu.util.Calendar;

import java.util.Date;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 19/05/2018
 */
public class MonthlyRecurrence extends Recurrence {

    protected MonthlyRecurrence(MonthlyRecurrence.Builder builder) {
        super(builder);
    }

    protected int calculateElapsedUnit(Date toDate) {
        Calendar cal = Calendar.getInstance(PERSIAN_LOCALE);
        cal.setTime(getBeginDate());
        int fromMonth = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH);
        double fromDay = cal.get(Calendar.DATE) + cal.get(Calendar.HOUR_OF_DAY)/24.0;

        cal.setTime(toDate);
        int toMonth = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH);
        double toDay = cal.get(Calendar.DATE) + cal.get(Calendar.HOUR_OF_DAY)/24.0;

        if (fromMonth > toMonth)
            return 0;

        int elapsedMonth = toMonth - fromMonth;
        return fromDay >= toDay ? elapsedMonth : ++elapsedMonth;
    }

    protected void addDifferenceUnitToRecurrenceBeginDate(int differenceUnit) {
        calendar.setTime(getBeginDate());
        calendar.add(Calendar.MONTH, differenceUnit);
    }

    public static class Builder extends RecurrenceBuilder {

        public Builder(Date beginDate) {
            super(beginDate);
        }

        public Recurrence build() {
            return new MonthlyRecurrence(this);
        }
    }
}
