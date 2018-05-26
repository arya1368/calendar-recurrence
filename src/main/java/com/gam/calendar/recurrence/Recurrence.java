package com.gam.calendar.recurrence;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 16/05/2018
 */
public abstract class Recurrence implements RecurrenceCalculator {
    public static final ULocale PERSIAN_LOCALE = new ULocale("fa_IR@calendar=persian");

    private DateRange dateRange;
    protected int interval;
    protected List<Date> exDates;
    protected Calendar calendar;
    protected Map<Integer, Integer> daysOfWeekOrder;

    protected Recurrence(RecurrenceBuilder builder) {
        dateRange = builder.range;
        interval = builder.interval;
        exDates = builder.exDates;
        calendar = Calendar.getInstance(PERSIAN_LOCALE);
        daysOfWeekOrder = builder.daysOfWeekOrder;
    }

    protected Date getBeginDate() {
        return dateRange.getFromDate();
    }

    protected Date getEndDate() {
        return dateRange.getToDate();
    }

    public List<Date> calculateOccurrenceDatesInRange(DateRange range) {
        List<Date> occurrenceDates = new ArrayList<>();
        int differenceUnit = calculateDifferenceUnit(range.getFromDate());
        addDifferenceUnitToRecurrenceBeginDate(differenceUnit);
        for (; isDateInRange(range); addDifferenceUnitToRecurrenceBeginDate(differenceUnit)) {
            if (!isDateInExDates())
                occurrenceDates.add(calendar.getTime());

            differenceUnit += interval;
        }

        return occurrenceDates;
    }

    public Date calculateOccurrenceDateAfter(Date from) {
        int differenceUnit = calculateDifferenceUnit(from);
        addDifferenceUnitToRecurrenceBeginDate(differenceUnit);
        for (; isInUpperBound(calendar.getTime(), getEndDate()); addDifferenceUnitToRecurrenceBeginDate(differenceUnit)) {
            if (!isDateInExDates() && from.compareTo(calendar.getTime()) < 0)
                return calendar.getTime();

            differenceUnit += interval;
        }

        return null;
    }

    protected int calculateDifferenceUnit(Date toDate) {
        int elapsed = calculateElapsedUnit(toDate);
        int difference = elapsed % interval;
        return difference == 0 ? elapsed : elapsed + Math.abs(interval - difference);
    }

    protected abstract int calculateElapsedUnit(Date toDate);

    protected abstract void addDifferenceUnitToRecurrenceBeginDate(int differenceUnit);

    protected boolean isDateInRange(DateRange range) {
        Date d = calendar.getTime();
        return isInLowerBound(d, range.getFromDate()) && isInUpperBound(d, range.getToDate());
    }

    protected boolean isInLowerBound(Date d, Date fromDate) {
        return d.compareTo(fromDate) >= 0 && d.compareTo(getBeginDate()) >= 0;
    }

    protected boolean isInUpperBound(Date d, Date toDate) {
        return d == null || toDate == null ||
                d.compareTo(toDate) <= 0 && (getEndDate() == null || d.compareTo(getEndDate()) <= 0);

    }

    protected boolean isDateInExDates() {
        for (Date d : exDates)
            if (calendar.getTime().compareTo(d) == 0)
                return true;

        return false;
    }

    protected void setCalendarWithRecurrenceBeginTime() {
        Calendar beginCal = Calendar.getInstance();
        beginCal.setTime(getBeginDate());

        calendar.set(Calendar.HOUR_OF_DAY, beginCal.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, beginCal.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, beginCal.get(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, beginCal.get(Calendar.MILLISECOND));
    }

    protected double getCalendarHourIncludeMinuteSecondMilliSecond() {
        return calendar.get(Calendar.HOUR_OF_DAY) +
                calendar.get(Calendar.MINUTE) / 60.0 +
                calendar.get(Calendar.SECOND) / 3600.0 +
                calendar.get(Calendar.MILLISECOND) / 3600000.0;
    }
}
