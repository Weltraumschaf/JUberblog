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

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class JVFSFileAttributeView implements BasicFileAttributeView {

    private final JVFSFileAttributes attributes;

    JVFSFileAttributeView(final JVFSFileAttributes attributes) {
        super();
        assert attributes != null : "attributes must be specified";
        this.attributes = attributes;
    }

    @Override
    public String name() {
        return JVFSFileAttributeView.class.getSimpleName();
    }

    @Override
    public BasicFileAttributes readAttributes() throws IOException {
        return attributes;
    }

    @Override
    public void setTimes(final FileTime lastModifiedTime, final FileTime lastAccessTime, final FileTime createTime)
        throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
