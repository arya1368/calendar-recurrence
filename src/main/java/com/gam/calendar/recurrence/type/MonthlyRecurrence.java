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

    @Override
    protected int calculateElapsedUnit(Date toDate) {
        calendar.setTime(getBeginDate());
        int fromMonth = calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH);
        double fromDay = calendar.get(Calendar.DATE);
        int lastDayOfFromMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        double fromHour = getCalendarHourIncludeMinuteSecondMilliSecond();

        calendar.setTime(toDate);
        int toMonth = calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH);
        double toDay = calendar.get(Calendar.DATE);
        int lastDayOfToMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        double toHour = getCalendarHourIncludeMinuteSecondMilliSecond();

        if (fromMonth > toMonth)
            return 0;

        int elapsedMonth = toMonth - fromMonth;

        if (fromDay == lastDayOfFromMonth && toDay == lastDayOfToMonth)
            return fromHour >= toHour ? elapsedMonth : ++elapsedMonth;

        fromDay += fromHour / 24.0;
        toDay += toHour / 24.0;
        return fromDay >= toDay ? elapsedMonth : ++elapsedMonth;
    }

    @Override
    protected void addDifferenceUnitToRecurrenceBeginDate(int differenceUnit) {
        calendar.setTime(getBeginDate());
        calendar.add(Calendar.MONTH, differenceUnit);
    }

    public static class Builder extends RecurrenceBuilder {

        public Builder(Date beginDate) {
            super(beginDate);
        }

        @Override
        public Recurrence build() {
            return new MonthlyRecurrence(this);
        }
    }
}
