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

import com.beust.jcommander.Parameter;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Options {

    @Parameter(names = {"-h", "--help"}, description = "Show this help.")
    private boolean help;
    @Parameter(names = {"-v", "--version"}, description = "Show the version.")
    private boolean version;
    /**
     * Where id the blog installed.
     */
    @Parameter(names = {"-l", "--location" }, description = "Location of the blog installation.")
    private String location;

    public boolean isHelp() {
        return help;
    }

    public boolean isVersion() {
        return version;
    }

    public String getLocation() {
        return location;
    }

}
