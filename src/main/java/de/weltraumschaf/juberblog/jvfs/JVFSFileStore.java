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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String type() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isReadOnly() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getTotalSpace() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getUsableSpace() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getUnallocatedSpace() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean supportsFileAttributeView(Class<? extends FileAttributeView> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean supportsFileAttributeView(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <V extends FileStoreAttributeView> V getFileStoreAttributeView(Class<V> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getAttribute(String attribute) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
