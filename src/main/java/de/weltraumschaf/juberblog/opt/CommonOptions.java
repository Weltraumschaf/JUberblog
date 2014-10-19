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
public class CommonOptions implements Options {

    /**
     * Help flag.
     */
    @Parameter(names = {"-h", "--help"}, description = "Show this message.", help = true)
    private boolean help;
    /**
     * Verbose flag.
     */
    @Parameter(names = {"--verbose"}, description = "Tell you more.")
    private boolean verbose;
    /**
     * Version flag.
     */
    @Parameter(names = {"-v", "--version"}, description = "Show version info.")
    private boolean version;
    /**
     * Debug flag.
     */
    @Parameter(names = {"--debug"}, description = "Print debug level information.")
    private boolean debug;

    public CommonOptions() {
        this(false, false);
    }

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
    public boolean isVersion() {
        return version;
    }

    @Override
    public boolean isDebug() {
        return debug;
    }

    @Override
    public String toString() {
        return "help: " + help + ", "
                + "verbose: " + verbose + ", "
                + "version: " + version + ", "
                + "debig: " + debug;
    }

}
