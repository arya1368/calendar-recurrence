package com.gam.calendar.recurrence;

import com.gam.calendar.recurrence.type.DailyRecurrence;
import com.gam.calendar.recurrence.type.MonthlyRecurrence;
import com.gam.calendar.recurrence.type.WeeklyRecurrence;
import com.gam.calendar.recurrence.type.YearlyRecurrence;

import java.util.Date;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 26/05/2018
 */
public enum RecurrenceBuilderFactory implements BuilderFactory {

    DAILY {
        public RecurrenceBuilder newInstance(Date beginDate) {
            return new DailyRecurrence.Builder(beginDate);
        }
    },

    WEEKLY {
        public RecurrenceBuilder newInstance(Date beginDate) {
            return new WeeklyRecurrence.Builder(beginDate);
        }
    },

    MONTHLY {
        public RecurrenceBuilder newInstance(Date beginDate) {
            return new MonthlyRecurrence.Builder(beginDate);
        }
    },

    YEARLY {
        public RecurrenceBuilder newInstance(Date beginDate) {
            return new YearlyRecurrence.Builder(beginDate);
        }
    }
}
