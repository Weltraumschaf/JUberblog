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
import de.weltraumschaf.commons.validate.Validate;

/**
 * Options for install commands.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class InstallOptions extends CommonOptions {

    /**
     * Where to install the blog scaffold.
     */
    @Parameter(names = {"-l", "--location" }, description = "Install location of the blog scaffold.")
    private String location;

    /**
     * Default constructor.
     */
    public InstallOptions() {
        this(false, false, "");
    }

    /**
     * Dedicated constructor.
     *
     * @param help {@code true} if help is wanted
     * @param verbose {@code true} if verbosity is wanted
     * @param location must not be {@code null}
     */
    public InstallOptions(final boolean help, final boolean verbose, final String location) {
        super(help, verbose);
        this.location = Validate.notNull(location, "Parameter 'location' must not be null!");
    }


    /**
     * Get install locations.
     *
     * @return never {@code null}, maybe empty
     */
    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "InstallOptions{" + super.toString() + ", location: " + location + '}';
    }

}
