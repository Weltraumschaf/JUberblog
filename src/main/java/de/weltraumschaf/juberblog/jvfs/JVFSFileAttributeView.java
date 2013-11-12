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

import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Map;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class JVFSFileAttributeView implements BasicFileAttributeView {

    private final JVFSPath path;

    JVFSFileAttributeView(final JVFSPath path) {
        super();
        assert path != null : "path must be specified";
        this.path = path;
    }

    @Override
    public String name() {
        return JVFSFileSystem.FILE_ATTR_VIEW_BASIC;
    }

    @Override
    public BasicFileAttributes readAttributes() throws IOException {
        return path.getAttributes();
    }

    @Override
    public void setTimes(final FileTime lastModifiedTime, final FileTime lastAccessTime, final FileTime createTime)
        throws IOException {
        path.setTimes(lastModifiedTime, lastAccessTime, createTime);
    }

    void setAttribute(final String attribute, final Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    Map<String, Object> readAttributes(String attributes) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

}
