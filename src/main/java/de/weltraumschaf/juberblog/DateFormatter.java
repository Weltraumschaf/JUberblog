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

import de.weltraumschaf.commons.validate.Validate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Formats Joda time objects to various predefined formats.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class DateFormatter {

    /**
     * Formats the given date with a given format.
     *
     * @param time must not be {@code null}
     * @param format must not be {@code null}
     * @return never {@code null}
     */
    public static String format(final DateTime time, final Format format) {
        return Validate.notNull(time, "time")
                .toString(Validate.notNull(format, "format").getFormatter());
    }

    /**
     * Predefined format patterns.
     */
    public enum Format {

        /**
         * Publishing date format for RSS files.
         *
         * See http://www.joda.org/joda-time/key_format.html
         */
        RSS_PUBLISH_DATE_FORMAT("EEE, dd MMM yyyy HH:mm:ss Z");

        /**
         * T
         */
        private final DateTimeFormatter formatter;

        /**
         * Dedicated constructor.
         *
         * @param pattern must not be {@code nul} or empty
         */
        private Format(final String pattern) {
            this.formatter = DateTimeFormat.forPattern(Validate.notEmpty(pattern, "pattern"));
        }

        private DateTimeFormatter getFormatter() {
            return formatter;
        }
    }
}
