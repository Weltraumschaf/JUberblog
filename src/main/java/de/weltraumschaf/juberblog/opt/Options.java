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

package de.weltraumschaf.juberblog.opt;

/**
 * Interface for immutable options value objects.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Options {

    /**
     * Whether the help flag is set.
     *
     * @return {@code true} if set, else {@code false}
     */
    boolean isHelp();
    /**
     * Whether the verbose flag is set.
     *
     * @return {@code true} if set, else {@code false}
     */
    boolean isVerbose();

    boolean isVersion();

    boolean isDebug();

}
