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

import de.weltraumschaf.commons.application.IO;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface SubCommand {

    Options options();

    IO io();

    void execute() throws Exception;

    enum SubCommandName {

        CREATE, INSTALL, PUBLISH, UNKNOWN;

        public static SubCommandName betterValueOf(final String name) {
            try {
                return valueOf(name.toUpperCase());
            } catch (final IllegalArgumentException ex) {
                return UNKNOWN;
            }
        }

        public static boolean isSubCommand(final String name) {
            return UNKNOWN != betterValueOf(name);
        }
    }
}
