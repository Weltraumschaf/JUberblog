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

package de.weltraumschaf.juberblog.app;

import de.weltraumschaf.commons.validate.Validate;
import java.util.Arrays;

/**
 * Abstraction for CLI arguments.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class Arguments {

    /**
     * The arguments.
     */
    private final String[] args;

    /**
     * Creates empty arguments.
     *
     * Mostly for testing purposes.
     */
    Arguments() {
        this(new String[] {});
    }

    /**
     * Dedicated constructor.
     *
     * @param args must not be {@code null}, defense copied
     */
    Arguments(final String[] args) {
        Validate.notNull(args, "Argumets must not be null!");
        this.args = args.clone();
    }

    /**
     * Get the first argument.
     *
     * @return never {@code null}, maybe empty
     */
    String getFirstArgument() {
        return args.length == 0 ? "" : args[0];
    }

    /**
     * Get all arguments but the first.
     *
     * @return never {@code null}, maybe empty
     */
    String[] getTailArguments() {
        if (args.length < 2) {
            return new String[] {};
        }

        return Arrays.copyOfRange(args, 1, args.length);
    }

    String[] getAll() {
        return args.clone();
    }

    int size() {
        return args.length;
    }

    boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public String toString() {
        return "first: " + getFirstArgument()
                + ", tail: " + Arrays.asList(getTailArguments()).toString();
    }

}
