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

package de.weltraumschaf.juberblog.template;

/**
 * Implementors can a variable be assigned.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Assignable {

    /**
     * Assign any object as template variable.
     *
     * @param name must not be {@code null}
     * @param value must not be {@code null}
     */
    void assignVariable(final String name, final Object value);

}
