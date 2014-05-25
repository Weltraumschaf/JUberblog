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

import com.beust.jcommander.Parameter;

/**
 * Common options for all commands.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public abstract class CommonOptions implements Options {

    /**
     * Help flag.
     */
    @Parameter(names = {"-h", "--help" }, description = "Show this message.", help = true)
    private boolean help;
    /**
     * Verbose flag.
     */
    @Parameter(names = {"-v", "--verbose" }, description = "Tell you more.")
    private boolean verbose;

    /**
     * Dedicated constructor.
     *
     * @param help {@code true} if help is wanted
     * @param verbose {@code true} if verbosity is wanted
     */
    public CommonOptions(final boolean help, final boolean verbose) {
        super();
        this.help = help;
        this.verbose = verbose;
    }

    @Override
    public boolean isHelp() {
        return help;
    }

    @Override
    public boolean isVerbose() {
        return verbose;
    }

    @Override
    public String toString() {
        return "help: " + help + ", verbose: " + verbose;
    }

}
