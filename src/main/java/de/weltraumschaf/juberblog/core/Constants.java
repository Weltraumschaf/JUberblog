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
package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.validate.Validate;

/**
 * Contains global constants.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public enum Constants {

    /**
     * NAme of the command line script.
     */
    COMMAND_NAME("juberblog"),
    /**
     * Default new line from system.
     */
    DEFAULT_NEW_LINE(String.format("%n")),
    /**
     * System dependent file separator.
     */
    DIR_SEP(System.getProperty("file.separator")),
    /**
     * Name of environment variable to enable debug mode.
     */
    ENVIRONMENT_VARIABLE_DEBUG("JUBERBLOG_DEBUG"),
    /**
     * Package base of whole project.
     */
    PACKAGE_BASE("/de/weltraumschaf/juberblog"),
    /**
     * Location of scaffold directory.
     */
    SCAFFOLD_PACKAGE("de.weltraumschaf.juberblog.scaffold"),
    /**
     * Name of sites sub directory.
     */
    SITES_DIR("sites"),
    /**
     * Name of posts sub directory.
     */
    POSTS_DIR("posts"),
    /**
     * Name of drafts sub directory.
     */
    DRAFTS_DIR("drafts");

    /**
     * Constant value.
     */
    private final String value;

    /**
     * Dedicated constructor.
     *
     * @param value must not be {@code null}
     */
    private Constants(final String value) {
        Validate.notNull(value, "Value must not be null!");
        this.value = value;
    }

    /**
     * Get the constant value as string.
     *
     * @return never {@code null}
     */
    @Override
    public String toString() {
        return value;
    }

}
