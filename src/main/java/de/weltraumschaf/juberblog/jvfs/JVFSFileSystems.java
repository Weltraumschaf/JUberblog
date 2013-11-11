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
 * Factory to get virtual file system stuff.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class JVFSFileSystems {

    /**
     * OS dependent directory separator.
     */
    public static final String DIR_SEP = System.getProperty("file.separator");
    /**
     * Protocol portion of a {@link URI} to JVFS {@link FileSystem}s.
     */
    public static final String PROTOCOL = "file";
    /**
     * Protocol suffix before ID portion of ShrinkWrap {@link URI}s.
     */
    private static final String URI_PROTOCOL_SUFFIX = "://";
    /**
     * Name of property to set default file system provider implementations.
     *
     * @see java.nio.file.FileSystems.DefaultFileSystemHolder.getDefaultProvider():java.nio.file.spi.FileSystemProvider
     */
    private static final String IMPLEMENTATION_PROPERTY_NAME = "java.nio.file.spi.DefaultFileSystemProvider";

    /**
     * Hidden for pure static class.
     */
    private JVFSFileSystems() {
        super();
        throw new UnsupportedOperationException("Class with only static methods");
    }

    /**
     * Returns a new file system provider.
     *
     * @return never {@code null} always new instance
     */
    public static FileSystemProvider newProvider() {
        return new JVFSFileSystemProvider();
    }

    /**
     * Constructs a new {@link URI} with the form: {@code file:///}.
     *
     * @return never {@code null}
     */
    public static URI getRootUri() {
        final StringBuilder sb = new StringBuilder();
        sb.append(PROTOCOL);
        sb.append(URI_PROTOCOL_SUFFIX);
        sb.append(DIR_SEP);
        return URI.create(sb.toString());
    }

    /**
     * Registers the JVFS implementation of {@link FileSystemProvider} as default file system.
     */
    public static void registerAsDefault() {
        System.setProperty(IMPLEMENTATION_PROPERTY_NAME, "de.weltraumschaf.juberblog.jvfs.JVFSFileSystemProvider");
    }

    /**
     * Removes the custom implementation.
     */
    public static void unregisterAsDefault() {
        System.setProperty(IMPLEMENTATION_PROPERTY_NAME, "");
    }

}
