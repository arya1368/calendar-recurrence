package com.gam.calendar.recurrence;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 16/05/2018
 */
public abstract class Recurrence implements RecurrenceCalculator {
    public static final ULocale PERSIAN_LOCALE = new ULocale("fa_IR@calendar=persian");

    protected DateRange dateRange;
    protected int interval;
    protected List<Date> exDates;
    protected Calendar calendar;

    protected Recurrence(RecurrenceBuilder builder) {
        dateRange = builder.range;
        interval = builder.interval;
        exDates = builder.exDates;
        calendar = Calendar.getInstance(PERSIAN_LOCALE);
    }

    public Date getBeginDate() {
        return dateRange.getFromDate();
    }

    public Date getEndDate() {
        return dateRange.getToDate();
    }

    public int getInterval() {
        return interval;
    }

    public List<Date> getExDates() {
        return exDates;
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
            if (!isDateInExDates())
                return calendar.getTime();

            differenceUnit += interval;
        }

        return null;
    }

    protected int calculateDifferenceUnit(Date toDate) {
        int elapsed = calculateElapsedUnit(toDate);
        elapsed = elapsed > 0 ? elapsed : 0;
        int difference = elapsed % interval;
        difference = difference == 0 ? difference : Math.abs(interval - difference);
        return difference + elapsed;
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

    protected Calendar getCalendarWithRecurrenceBeginTimeAndGivenDate(Date date) {
        Calendar beginCal = Calendar.getInstance();
        beginCal.setTime(getBeginDate());

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, beginCal.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, beginCal.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, beginCal.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, beginCal.get(Calendar.MILLISECOND));
        return cal;
    }
}