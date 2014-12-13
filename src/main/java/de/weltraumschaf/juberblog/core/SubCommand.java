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
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.create.CreateSubCommand;
import de.weltraumschaf.juberblog.install.InstallSubCommand;
import de.weltraumschaf.juberblog.publish.PublishSubCommand;

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

    /**
     * Creates sub command instances.
     */
    public static class Factory {

        /**
         * Creates sub command for name.
         * <p>
         * Throws {@link IllegalArgumentException} for unsupported names.
         * </p>
         *
         * @param name must not be {@code null}
         * @param options must not be {@code null}
         * @param io must not be {@code null}
         * @return never {@code null}, always new instance
         */
        public SubCommand forName(final Name name, final Options options, final IO io) {
            switch (Validate.notNull(name, "name")) {
                case CREATE:
                    return new CreateSubCommand(options, io);
                case INSTALL:
                    return new InstallSubCommand(options, io);
                case PUBLISH:
                    return new PublishSubCommand(options, io);
                default:
                    throw new IllegalArgumentException(String.format("Unsupported command name: '%s'!", name));
            }
        }
    }
}
