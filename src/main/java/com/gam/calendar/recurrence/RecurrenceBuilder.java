package com.gam.calendar.recurrence;

import java.util.*;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 16/05/2018
 */
public abstract class RecurrenceBuilder {
    public static final int DEFAULT_INTERVAL = 1;

    protected DateRange range;
    protected int interval;
    protected List<Date> exDates;
    protected Map<Integer, Integer> daysOfWeekOrder;

    public RecurrenceBuilder(Date beginDate) {
        requireNonNull(beginDate, "beginDate can not be null.");
        range = new DateRange();
        exDates = new ArrayList<>();
        daysOfWeekOrder = new HashMap<>();
        range.setFromDate(beginDate);
        interval = DEFAULT_INTERVAL;
    }

    public RecurrenceBuilder setRecurrenceEndDate(Date endDate) {
        range.setToDate(endDate);
        return this;
    }

    public RecurrenceBuilder setInterval(int interval) {
        if (interval <= 0)
            throw new IllegalBuilderArgumentException("interval must be greater than zero. interval: " + interval);

        this.interval = interval;
        return this;
    }

    public RecurrenceBuilder addExDate(Date exDate) {
        requireNonNull(exDate, "can not add null to exDates.");
        exDates.add(exDate);
        return this;
    }

    public RecurrenceBuilder setExDates(List<Date> exDates) {
        requireNonNull(exDates, "exDates can not be null.");
        this.exDates = exDates;
        return this;
    }

    public RecurrenceBuilder onSaturday() {
        daysOfWeekOrder.put(0, Calendar.SATURDAY);
        return this;
    }

    public RecurrenceBuilder onSunday() {
        daysOfWeekOrder.put(1, Calendar.SUNDAY);
        return this;
    }

    public RecurrenceBuilder onMonday() {
        daysOfWeekOrder.put(2, Calendar.MONDAY);
        return this;
    }

    public RecurrenceBuilder onTuesday() {
        daysOfWeekOrder.put(3, Calendar.TUESDAY);
        return this;
    }

    public RecurrenceBuilder onWednesday() {
        daysOfWeekOrder.put(4, Calendar.WEDNESDAY);
        return this;
    }

    public RecurrenceBuilder onThursday() {
        daysOfWeekOrder.put(5, Calendar.THURSDAY);
        return this;
    }

    public RecurrenceBuilder onFriday() {
        daysOfWeekOrder.put(6, Calendar.FRIDAY);
        return this;
    }

    public abstract Recurrence build();

    private void requireNonNull(Object obj, String message) {
        if (obj == null)
            throw new IllegalBuilderArgumentException(message);
    }

    public static class IllegalBuilderArgumentException extends RuntimeException {

        public IllegalBuilderArgumentException(String message) {
            super(message);
        }
    }
}
