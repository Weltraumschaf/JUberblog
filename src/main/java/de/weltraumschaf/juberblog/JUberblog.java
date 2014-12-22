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
package de.weltraumschaf.juberblog;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.app.Options;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Templates;
import java.nio.file.Paths;

/**
 * Registry object to cary around important objects.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class JUberblog {

    /**
     * Holds important directories.
     */
    private final Directories dirs;
    /**
     * Holds the needed templates.
     */
    private final Templates tpls;
    /**
     * Holds the blog's configuration.
     */
    private final Configuration cfg;
    /**
     * Holds the command line options.
     */
    private final Options opt;
    /**
     * Provides I/O for the command line.
     */
    private final IO io;

    public JUberblog(final Directories dirs, final Templates tpls, final Configuration cfg, final Options opt, final IO io) {
        super();
        this.dirs = Validate.notNull(dirs, "dirs");
        this.tpls = Validate.notNull(tpls, "tpls");
        this.cfg = Validate.notNull(cfg, "cfg");
        this.opt = Validate.notNull(opt, "opt");
        this.io = Validate.notNull(io, "io");
    }

    /**
     * Get the directories.
     *
     * @return never {@code null}
     */
    public Directories directories() {
        return dirs;
    }

    /**
     * Get the templates.
     *
     * @return never {@code null}
     */
    public Templates templates() {
        return tpls;
    }

    /**
     * Get the configuration.
     *
     * @return never {@code null}
     */
    public Configuration configuration() {
        return cfg;
    }

    /**
     * Get the options.
     *
     * @return never {@code null}
     */
    public Options options() {
        return opt;
    }

    /**
     * Get the I/O.
     *
     * @return never {@code null}
     */
    public IO io() {
        return io;
    }

    public static JUberblog generate(final Options cliOptions, final IO io) {
        return new JUberblog(
                new Directories(
                        Paths.get("."),
                        Paths.get("."),
                        Paths.get("."),
                        Paths.get("."),
                        Paths.get(".")),
                new Templates(
                        Paths.get("."),
                        Paths.get("."),
                        Paths.get("."),
                        Paths.get("."),
                        Paths.get("."),
                        Paths.get(".")),
                new Configuration(),
                cliOptions,
                io);
    }

    public static final class Configuration {

    }
}
