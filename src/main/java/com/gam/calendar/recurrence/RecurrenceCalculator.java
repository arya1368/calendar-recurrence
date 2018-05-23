package com.gam.calendar.recurrence;

import java.util.Date;
import java.util.List;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 19/05/2018
 */
public interface RecurrenceCalculator {

    List<Date> calculateOccurrenceDatesInRange(DateRange range);

    Date calculateOccurrenceDateAfter(Date from);
}
