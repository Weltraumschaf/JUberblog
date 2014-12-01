/*
 *  LICENSE
 *
 * "THE BEER-WARE LICENSE" (Revision 43):
 * "Sven Strittmatter" <weltraumschaf@googlemail.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it,
 * you can buy me a non alcohol-free beer in return.
 *
 * Copyright (C) 2012 "Sven Strittmatter" <weltraumschaf@googlemail.com>
 */

package de.weltraumschaf.juberblog;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class DateFormatter {

    /**
     * Publishing date format.
     *
     * See http://www.joda.org/joda-time/key_format.html
     */
    public static final DateTimeFormatter RSS_PUBLISH_DATE_FORMAT
        = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss Z");

    public static String format(DateTime time, final DateTimeFormatter format) {
        return time.toString(format);
    }
}
