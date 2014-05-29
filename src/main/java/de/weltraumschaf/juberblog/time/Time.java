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

/**
 * Factory to provide time utilities.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Time {

    /**
     * Hidden for pure static factory.
     */
    private Time() {
        super();
        throw new UnsupportedOperationException("Do not call by reflection!");
    }

    /**
     * Creates a new time provider.
     *
     * @return never {@code null}, always new instance
     */
    public static TimeProvider newProvider() {
        return new DefaultProvider();
    }
}
