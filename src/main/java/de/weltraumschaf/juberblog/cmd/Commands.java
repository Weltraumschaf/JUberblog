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

package de.weltraumschaf.juberblog.cmd;

import de.weltraumschaf.commons.IOStreams;
import de.weltraumschaf.juberblog.CliOptions;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Commands {

    private static final String CREATE = "create";
    private static final String PUBLISH = "publish";
    public static final String INSTALL = "install";

    private Commands() {
        super();
    }

    public static Command create(final CliOptions options, final IOStreams io) {
        switch (options.getSubCommandName()) {
            case CREATE:
                return new Create(options, io);
            case PUBLISH:
                return new Publish(options, io);
            case INSTALL:
                return new Install(options, io);
            default:
                throw new IllegalArgumentException(String.format("Unknown command '%s'!", options.getSubCommandName()));
        }
    }
}
