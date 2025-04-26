package com.zenyte.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple utility for date-formats.
 */
public enum DateFormat {

    HH_MM_SS("HH:mm:ss");

    private final String pattern;

    DateFormat(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Create a new SimpleDateFormat from the {@link #pattern}.
     * @return the {@link SimpleDateFormat} instance.
     */
    public SimpleDateFormat toSimpleDateFormat() {
        return new SimpleDateFormat(pattern);
    }

    /**
     * Formats a Date into a date/time string.
     * @param date the time value to be formatted into a time string.
     * @return the formatted time string.
     */
    public String format(Date date) {
        return toSimpleDateFormat().format(date);
    }

    /**
     * Parses text from the beginning of the given string to produce a date.
     * The method may not use the entire text of the given string.
     * <p>
     * See the {@link SimpleDateFormat#parse(String, ParsePosition)} method for more information
     * on date parsing.
     *
     * @param source A <code>String</code> whose beginning should be parsed.
     * @return A <code>Date</code> parsed from the string.
     * @exception ParseException if the beginning of the specified string
     *            cannot be parsed.
     */
    public Date parse(String source) throws ParseException {
        return toSimpleDateFormat().parse(source);
    }
}
