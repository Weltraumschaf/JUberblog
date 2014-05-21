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
import freemarker.template.Version;

/**
 * Contains global constants.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public enum Constants {

    /**
     * Used default string encoding.
     */
    DEFAULT_ENCODING("UTF-8"),
    /**
     * Used default new line separator.
     */
    DEFAULT_NEW_LINE(String.format("%n")),
    /**
     * System dependent file separator.
     */
    DIR_SEP(System.getProperty("file.separator")),
    /**
     * Location of scaffold directory.
     */
    SCAFFOLD_PACKAGE("de.weltraumschaf.juberblog.scaffold"),
    /**
     * Name of environment variable to enable debug mode.
     */
    ENVIRONMENT_VARIABLE_DEBUG("JUBERBLOG_DEBUG"),
    /**
     * NAme of the command line script.
     */
    COMMAND_NAME("juberblog"),
    /**
     * Package base of whole project.
     */
    PACKAGE_BASE("/de/weltraumschaf/juberblog"),
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
     * Major version of Freemarker template engine.
     */
    private static final int FREEMARKER_MAJOR_VERSION = 2;
    /**
     * Minor version of Freemarker template engine.
     */
    private static final int FREEMARKER_MINOR_VERSION = 3;
    /**
     * Mini version of Freemarker template engine.
     */
    private static final int FREEMARKER_MINI_VERSION = 20;
    /**
     * Used Freemarker version.
     */
    //CHECKSTYLE:OFF Must be declared after private numbers.
    public static final Version FREEMARKER_VERSION = new Version(
            FREEMARKER_MAJOR_VERSION,
            FREEMARKER_MINOR_VERSION,
            FREEMARKER_MINI_VERSION);
    //CHECKSTYLE:ON

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
