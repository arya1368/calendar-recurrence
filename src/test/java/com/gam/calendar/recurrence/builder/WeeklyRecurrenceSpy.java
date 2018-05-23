package com.gam.calendar.recurrence.builder;


import com.gam.calendar.recurrence.Recurrence;
import com.gam.calendar.recurrence.type.WeeklyRecurrence;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 19/05/2018
 */
public class WeeklyRecurrenceSpy extends WeeklyRecurrence {

    protected WeeklyRecurrenceSpy(WeeklyRecurrenceSpy.Builder builder) {
        super(builder);
    }

    public Map<Integer, Integer> getDaysOfWeekWithOrder() {
        return daysOfWeekOrder;
    }

    public static class Builder extends WeeklyRecurrence.Builder {

        public Builder(Date beginDate) {
            super(beginDate);
        }

        @Override
        public Recurrence build() {
            return new WeeklyRecurrenceSpy(this);
        }
    }
}
