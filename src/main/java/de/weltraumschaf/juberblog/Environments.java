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

/**
 * Provides instances to access environment variables.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Environments {

    /**
     * Hidden for static factory.
     */
    private Environments() {
        super();
    }

    /**
     * Get a default implementation.
     *
     * @return never {@code null}, always new instance
     */
    public static Env defaultEnv() {
        return new DefaultEnv();
    }

    /**
     * Implementations provide access to read environment variables.
     */
    public interface Env {
        /**
         * Get a environment variable.
         *
         * @param name must not be {@code null} or empty
         * @return never {@code null}, maybe empty
         */
        String get(String name);
    }

    /**
     * Default implementation which uses {@link System#getenv(java.lang.String)}.
     */
    private static final class DefaultEnv implements Env {

        @Override
        public String get(final String name) {
            Validate.notEmpty(name, "Name must not be null or empty!");
            final String var = System.getenv(name);
            return var == null ? "" : var;
        }

    }
}
