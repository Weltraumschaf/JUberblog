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

package de.weltraumschaf.juberblog.filter;

/**
 * Implementors processes an input string and will return the processed string.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Filter {

    /**
     * Applies the filter on the input string and return the processed string.
     *
     * @param input must not be {@code nul}
     * @return never {@code null}
     */
    String apply(String input);

}
