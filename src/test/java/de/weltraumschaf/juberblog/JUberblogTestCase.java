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

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Some helper stuff.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public abstract class JUberblogTestCase {

    public static final String ENCODING = "utf-8";
    public static final String BASE = "/de/weltraumschaf/juberblog/";

    protected final Path createPath(final String name) throws URISyntaxException {
        return Paths.get(getClass().getResource(BASE + name).toURI());
    }
}
