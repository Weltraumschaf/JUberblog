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

package de.weltraumschaf.juberblog.jvfs;

import java.net.URI;
import java.nio.file.spi.FileSystemProvider;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class JVFSFileSystems {

    public static final String SEPARATOR = System.getProperty("file.separator");
    /**
     * Protocol portion of a {@link URI} to ShrinkWrap {@link FileSystem}s
     */
    public static final String PROTOCOL = "file";
    /**
     * Protocol suffix before ID portion of ShrinkWrap {@link URI}s
     */
    private static final String URI_PROTOCOL_SUFFIX = "://";

    private JVFSFileSystems() {
        super();
        throw new UnsupportedOperationException("Class with only static methods");
    }

    public static FileSystemProvider newProvider() {
        return new JVFSFileSystemProvider();
    }

    /**
     * Constructs a new {@link URI} with the form:
     *
     * <code>shrinkwrap://{archive.getId()}/</code>
     */
    public static URI getRootUri() {
        final StringBuilder sb = new StringBuilder();
        sb.append(PROTOCOL);
        sb.append(URI_PROTOCOL_SUFFIX);
        sb.append(SEPARATOR);
        return URI.create(sb.toString());
    }

}
