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
import java.nio.file.ClosedFileSystemException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class JVFSFileSystem extends FileSystem {

    /**
     * Provider which created this {@link JVFSFileSystemProvider}
     */
    private final JVFSFileSystemProvider provider;
    private final List<FileStore> fileStores;
    /**
     * Whether or not this FS is open; volatile as we don't need compound operations and thus don't need full sync
     */
    private volatile boolean open;

    public JVFSFileSystem(final JVFSFileSystemProvider provider) {
        super();
        JVFSAssertions.notNull(provider, "provider");
        this.provider = provider;
        this.open = true;
        final FileStore store = new JVFSFileStore();
        final List<FileStore> stores = new ArrayList<FileStore>(1);
        stores.add(store);
        this.fileStores = Collections.unmodifiableList(stores);
    }

    @Override
    public FileSystemProvider provider() {
        return provider;
    }

    @Override
    public void close() throws IOException {
        this.open = false;
    }

    @Override
    public boolean isOpen() {
        return this.open;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public String getSeparator() {
        return JVFSFileSystems.SEPARATOR;
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        this.checkClosed();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        this.checkClosed();
        return fileStores;
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        this.checkClosed();
        return new HashSet<String>();
    }

    @Override
    public Path getPath(final String first, final String... more) {
        this.checkClosed();
        JVFSAssertions.notNull(first, "first");
        final String merged = this.merge(first, more);
        return new JVFSPath(merged, this);
    }

    /**
     * Merges the path context with a varargs String sub-contexts, returning the result
     *
     * @param first
     * @param more
     * @return
     */
    private String merge(final String first, final String[] more) {
        assert first != null : "first must be specified";
        assert more != null : "more must be specified";

        final StringBuilder merged = new StringBuilder();
        merged.append(first);

        for (int i = 0; i < more.length; i++) {
            merged.append(JVFSFileSystems.SEPARATOR);
            merged.append(more[i]);
        }

        return merged.toString();
    }

    @Override
    public PathMatcher getPathMatcher(String syntaxAndPattern) {
        throw new UnsupportedOperationException("JVFS does not support Path Matcher operations!");
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        throw new UnsupportedOperationException("JVFS archives do not support file ownership.");
    }

    @Override
    public WatchService newWatchService() throws IOException {
        throw new UnsupportedOperationException("JVFS archives do not support a "
                + WatchService.class.getSimpleName() + ".");
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     * Checks if the {@link ShrinkWrapFileSystem} is closed, and throws a {@link ClosedFileSystemException} if so.
     */
    private void checkClosed() {
        if (!this.isOpen()) {
            throw new ClosedFileSystemException();
        }
    }

}
