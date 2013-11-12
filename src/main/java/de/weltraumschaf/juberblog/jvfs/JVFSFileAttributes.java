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

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.TimeUnit;

/**
 * Represents the file attributes.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class JVFSFileAttributes implements BasicFileAttributes {

    /**
     * Wrapped file system entry.
     */
    private final JVFSFileEntry entry;

    /**
     * Dedicated constructor.
     *
     * @param entry must not be {@literal null}
     */
    JVFSFileAttributes(final JVFSFileEntry entry) {
        super();
        assert entry != null : "entry must be specified";
        this.entry = entry;
    }

    /**
     * Creates a file time based on a long in seconds.
     *
     * @param timestamp must be non negative
     * @return never {@literal null}
     */
    private static FileTime createFileTime(final long timestamp) {
        JVFSAssertions.greaterThanEqual(timestamp, 0, "timestamp");
        return FileTime.from(timestamp, TimeUnit.SECONDS);
    }

    @Override
    public FileTime lastModifiedTime() {
        return createFileTime(entry.getLastModifiedTime());
    }

    @Override
    public FileTime lastAccessTime() {
        return createFileTime(entry.getLastAccessTime());
    }

    @Override
    public FileTime creationTime() {
        return createFileTime(entry.getCreationTime());
    }

    @Override
    public boolean isRegularFile() {
        return !isDirectory();
    }

    @Override
    public boolean isDirectory() {
        return entry.isDirectory();
    }

    @Override
    public boolean isSymbolicLink() {
        return entry.isSymbolicLink();
    }

    @Override
    public boolean isOther() {
        // Either a dir or regular file
        return false;
    }

    @Override
    public long size() {
        return entry.size();
    }

    @Override
    public Object fileKey() {
        return entry.toString();
    }

}
