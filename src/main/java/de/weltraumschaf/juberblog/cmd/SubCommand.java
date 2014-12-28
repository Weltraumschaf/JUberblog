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

import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.app.Options;
import de.weltraumschaf.juberblog.core.Configuration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Templates;

/**
 * Implementations are a subcommand of the main application.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface SubCommand {

    /**
     * Get the sub command options.
     *
     * @return never {@code null}
     */
    Options options();

    /**
     * Used for CLI IO.
     *
     * @return never {@code null}
     */
    IO io();

    /**
     * Get all important templates,
     *
     * @return never {@code null}
     */
    public Templates templates();

    /**
     * Get all important directories,
     *
     * @return never {@code null}
     */
    public Directories directories();

    /**
     * Get all important configurations,
     *
     * @return never {@code null}
     */
    public Configuration configuration();

    /**
     * Executes the sub command.
     *
     * @throws Exception on any error during execution
     */
    void execute() throws Exception;

    /**
     * Maps literal sub command names to types.
     */
    enum Name {

        /**
         * Create sub command.
         */
        CREATE,
        /**
         * Install sub command.
         */
        INSTALL,
        /**
         * Publish sub command.
         */
        PUBLISH,
        /**
         * USed for any unsupported or unrecognized sub command.
         */
        UNKNOWN;

        /**
         * Nearly same as {@link #valueOf(java.lang.String)} but does not thrown an exception if the given name string
         * does not map to an enum, but returns {@link #UNKNOWN}.
         *
         * @param name must not be {@code null} or empty
         * @return never {@code null}
         */
        public static Name betterValueOf(final String name) {
            try {
                return valueOf(Validate.notEmpty(name, "name").toUpperCase());
            } catch (final IllegalArgumentException ex) {
                return UNKNOWN;
            }
        }

        /**
         * Tells if a given name is a sub command name.
         *
         * @param name must not be {@code null} or empty
         * @return {@code true} if given name is valid sub command, else {@code false}
         */
        public static boolean isSubCommand(final String name) {
            return UNKNOWN != betterValueOf(name);
        }
    }

}
