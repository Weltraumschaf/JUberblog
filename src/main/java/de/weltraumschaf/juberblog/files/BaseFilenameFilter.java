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

package de.weltraumschaf.juberblog.files;

import java.io.FilenameFilter;

/**
 * Common code for file name filters.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
abstract class BaseFilenameFilter implements FilenameFilter {

    /**
     * Determine if a given name ends with an file extension (e.g. .html).
     *
     * @param name must not be {@code nul}
     * @param extension must not be {@code nul}
     * @return {@code true} if name ends with given extension, else {@code false}
     */
    protected boolean hasExtension(final String name, final String extension) {
        return name.endsWith(extension);
    }
}
