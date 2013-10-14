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

import de.weltraumschaf.commons.system.ExitCode;

/**
 * Exit codes for application.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public enum ExitCodeImpl implements ExitCode {

    /**
     * Any unspecified error.
     */
    FATAL(-1),
    /**
     * No errors.
     */
    OK(0),
    /**
     * Unspecified fatal error occurred.
     */
    UNKNOWN_COMMAND(1),
    /**
     * Too few command line arguments.
     */
    TOO_FEW_ARGUMENTS(2),
    /**
     * Can't load configuration file.
     */
    CANT_LOAD_CONFIG(3),
    /**
     * Indicates a missing command line argument.
     */
    MISSING_ARGUMENT(4),
    /**
     * Indicates an invalid command line argument.
     */
    BAD_ARGUMENT(5);
    /**
     * Exit code number returned as exit code to JVM.
     */
    private final int code;

    /**
     * Dedicated constructor.
     *
     * @param code exit code number
     */
    ExitCodeImpl(final int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
