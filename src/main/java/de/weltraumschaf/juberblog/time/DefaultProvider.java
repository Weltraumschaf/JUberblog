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

package de.weltraumschaf.juberblog.time;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class DefaultProvider implements TimeProvider {

    private static final int LENGTH_OTHERS = 2;
    private static final int LENGTH_HEAR = 4;

    private final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendYear(LENGTH_HEAR, LENGTH_HEAR)
                .appendLiteral('-')
                .appendMonthOfYear(LENGTH_OTHERS)
                .appendLiteral('-')
                .appendDayOfMonth(LENGTH_OTHERS)
                .appendLiteral('T')
                .appendHourOfDay(LENGTH_OTHERS)
                .appendLiteral('.')
                .appendMinuteOfHour(LENGTH_OTHERS)
                .appendLiteral('.')
                .appendSecondOfDay(LENGTH_OTHERS)
                .toFormatter();

    @Override
    public String nowAsString() {
        return new DateTime().toString(formatter);
    }

}
