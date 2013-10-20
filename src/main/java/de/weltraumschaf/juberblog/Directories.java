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

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import org.apache.commons.lang3.Validate;

/**
 * Provides file system for all necessary directories.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Directories {

    /**
     * Used to get configured base directories.
     */
    private final BlogConfiguration config;
    /**
     * A default file system.
     */
    private final FileSystem fs = FileSystems.getDefault();

    /**
     * Dedicated constructor.
     *
     * @param config must not be {@code null}
     */
    public Directories(final BlogConfiguration config) {
        super();
        Validate.notNull(config, "Config must not be null!");
        this.config = config;
    }

    /**
     * Get the data directory path.
     *
     * @return never {@code null}
     */
    public Path getDataDir() {
        return fs.getPath(config.getDataDir());
    }

    /**
     * Get the template directory path.
     *
     * @return never {@code null}
     */
    public Path getTemplateDir() {
        return fs.getPath(config.getTemplateDir());
    }

    /**
     * Get the htdocs directory path.
     *
     * @return never {@code null}
     */
    public Path getHtdocsDir() {
        return fs.getPath(config.getHtdocs());
    }

}
