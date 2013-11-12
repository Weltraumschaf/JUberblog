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
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class JVFSFileAttributes implements BasicFileAttributes {

    private final JVFSPath path;

    JVFSFileAttributes(final JVFSPath path) {
        super();
        assert path != null : "path must be specified";
        this.path = path;
    }

    private static FileTime createFileTime(final long timestamp) {
        return FileTime.from(timestamp, TimeUnit.SECONDS);
    }

    @Override
    public FileTime lastModifiedTime() {
        return createFileTime(getFileSystem().get(path.toString()).getLastModifiedTime());
    }

    @Override
    public FileTime lastAccessTime() {
        return createFileTime(getFileSystem().get(path.toString()).getLastAccessTime());
    }

    @Override
    public FileTime creationTime() {
        return createFileTime(getFileSystem().get(path.toString()).getCreationTime());
    }

    @Override
    public boolean isRegularFile() {
        return !isDirectory();
    }

    @Override
    public boolean isDirectory() {
        return getFileSystem().get(path.toString()).isDirectory();
    }

    @Override
    public boolean isSymbolicLink() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isOther() {
        // Either a dir or regular file
        return false;
    }

    @Override
    public long size() {
        if (this.isDirectory()) {
            return -1L;
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object fileKey() {
        return path.toString();
    }

    private JVFSFileSystem getFileSystem() {
        return ((JVFSFileSystem) path.getFileSystem());
    }

}
