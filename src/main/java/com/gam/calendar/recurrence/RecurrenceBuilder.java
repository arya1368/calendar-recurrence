package com.gam.calendar.recurrence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 16/05/2018
 */
public abstract class RecurrenceBuilder {
    private static final int DEFAULT_INTERVAL = 1;

    protected DateRange range;
    protected int interval;
    protected List<Date> exDates;

    public RecurrenceBuilder(Date beginDate) {
        requireNonNull(beginDate, "beginDate can not be null.");
        range = new DateRange();
        exDates = new ArrayList<>();
        range.setFromDate(beginDate);
        interval = DEFAULT_INTERVAL;
    }

    public RecurrenceBuilder setRecurrenceEndDate(Date endDate) {
        range.setToDate(endDate);
        return this;
    }

    public RecurrenceBuilder setInterval(int interval) {
        if (interval <= 0)
            throw new IllegalBuilderArgumentException("interval can not be under zero. interval: " + interval);

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

    public abstract Recurrence build();

    protected void requireNonNull(Object obj, String message) {
        if (obj == null)
            throw new IllegalBuilderArgumentException(message);
    }

    public static class IllegalBuilderArgumentException extends RuntimeException {

        public IllegalBuilderArgumentException(String message) {
            super(message);
        }
    }
}
