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
package de.weltraumschaf.juberblog.jvfs;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class JVFSAssertions {

    private static final String NOT_NULL_MESSAGE = "Parameter '%s' must not be null!";
    private static final String LESS_TAHN_MESSAGE = "Paramater '%s' must be less than '%d'!";
    private static final String LESS_TAHHAN_EQUAL = "Paramater '%s' must be less than or equal '%d'!";
    private static final String GREATER_TAHN_MESSAGE = "Paramater '%s' must be greater than '%d'!";
    private static final String EQUAL_MESSAGE = "Parameter '%s' is not equal to '%s'!";

    private JVFSAssertions() {
        super();
    }

    public static void notNull(final Object o, final String name) {
        if (null == o) {
            throw new NullPointerException(String.format(NOT_NULL_MESSAGE, name));
        }
    }

    public static void lessThan(final int checked, final int reference, final String name) {
        if (checked < reference) {
            throw new IllegalArgumentException(String.format(LESS_TAHN_MESSAGE, name, reference));
        }
    }

    public static void lessThanEqual(int checked, int reference, String name) {
        if (checked <= reference) {
            throw new IllegalArgumentException(String.format(LESS_TAHHAN_EQUAL, name, reference));
        }
    }

    public static void greaterThan(final int checked, final int reference, final String name) {
        if (checked > reference) {
            throw new IllegalArgumentException(String.format(GREATER_TAHN_MESSAGE, name, reference));
        }
    }

    public static void isEqual(final Object actual, final Object expected, final String name) {
        if (notEqual(actual, expected)) {
            throw new IllegalArgumentException(String.format(EQUAL_MESSAGE, name, expected));
        }
    }

    public static boolean equal(final Object a, final Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static boolean notEqual(final Object a, final Object b) {
        return !equal(a, b);
    }
}
