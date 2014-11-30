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
     * If on start up system.in, system.out or System.err can't be accessed.
     */
    CANT_READ_IO_STREAMS(255);

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
