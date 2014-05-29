/*
 * 
 * Copyright [2013] [claudio degioanni claudio.degioanni@proxima-centauri.it]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.proximacentauri.ienergy.da.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Date utils class
 * 
 * @author proximacentauri
 * 
 */
public abstract class DateUtil {

	/**
	 * Format a date using a given format
	 * 
	 * @param date
	 *            the date to format
	 * @param format
	 *            the date format
	 * @return the formatted date
	 */
	public static String formatDateString(Date date, String format) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(format);

		return dateFormat.format(date);
	}

	/**
	 * Util function to sum a value to a date
	 * 
	 * @param date
	 *            the base date
	 * @param field
	 *            the date filed to add
	 * @param amount
	 *            the amount to add
	 * @return the added value
	 */
	public static Date add(Date date, int field, int amount) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, amount);
		return cal.getTime();
	}

	/**
	 * Extract the start date passed by user
	 * 
	 * @param start
	 *            the start date, can be null (also no user selection)
	 * @param end
	 *            the end date, used id
	 * @param hour
	 *            the hour to subtract to end date, only if start date is null
	 * @return the start date
	 */
	static public Date extractStartDate(Date start, Date end, int hour) {
		// Configure date query
		if (start == null) {
			start = DateUtil.add(end, Calendar.HOUR_OF_DAY, -hour);
		}
		return start;
	}

	/**
	 * Extract the start date passed by user, 1 hour
	 * 
	 * @param start
	 *            the start date, can be null (also no user selection)
	 * @param end
	 *            the end date, used id
	 * @return the start date
	 */
	static public Date extractStartDate(Date start, Date end) {
		// Configure date query
		return extractStartDate(start, end, 1);
	}

	/**
	 * Extract the end data passed by user
	 * 
	 * @param end
	 *            the end date can be null
	 * @return the end date
	 */
	static public Date extractEndDate(Date end) {
		// configure end time
		if (end == null) {
			end = new Date();
		}
		return end;
	}
}
