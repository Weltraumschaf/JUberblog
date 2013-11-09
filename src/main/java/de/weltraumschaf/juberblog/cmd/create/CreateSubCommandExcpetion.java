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

package de.weltraumschaf.juberblog.cmd.create;

import org.apache.commons.lang3.Validate;

/**
 * If anything goes wrong during publishing.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CreateSubCommandExcpetion extends Exception {

    /**
     * Convenience constructor.
     *
     * @param message must not be {@code null} or empty
     */
    public CreateSubCommandExcpetion(final String message) {
        this(message, null);
    }

    /**
     * Dedicated constructor.
     *
     * @param message must not be {@code null} or empty
     * @param cause may be {@code null}
     */
    public CreateSubCommandExcpetion(final String message, final Throwable cause) {
        super(message, cause);
        Validate.notEmpty(message, "Message must not be nul or empty!");
    }

}
