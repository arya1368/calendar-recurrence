package com.gam.calendar.recurrence;

import java.util.Date;

/**
 * @author Arya Pishgah (pishgah@gamelectronics.com) 26/05/2018
 */
interface BuilderFactory {

    RecurrenceBuilder newInstance(Date beginDate);
}
