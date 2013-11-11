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
import java.nio.file.FileStore;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class JVFSFileStore extends FileStore {

    public JVFSFileStore() {
        super();
    }

    @Override
    public String name() {
        return "jvfs";
    }

    @Override
    public String type() {
        return "in-memory";
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public long getTotalSpace() throws IOException {
        return this.getUsableSpace() + this.getUsedSpace();
    }

    public long getUsedSpace() {
        return 0;
    }

    @Override
    public long getUsableSpace() throws IOException {
        return Runtime.getRuntime().freeMemory();
    }

    @Override
    public long getUnallocatedSpace() throws IOException {
        return this.getUsableSpace();
    }

    @Override
    public boolean supportsFileAttributeView(Class<? extends FileAttributeView> type) {
        return BasicFileAttributeView.class.equals(type);
    }

    @Override
    public boolean supportsFileAttributeView(String name) {
        // Only support "basic"
        return JVFSFileSystem.FILE_ATTR_VIEW_BASIC.equals(name);
    }

    @Override
    public <V extends FileStoreAttributeView> V getFileStoreAttributeView(Class<V> type) {
        return null;
    }

    @Override
    public Object getAttribute(String attribute) throws IOException {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " does not support attributes.");
    }

}
