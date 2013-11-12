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

import com.sun.nio.zipfs.ZipFileAttributes;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.AccessMode;
import java.nio.file.ClosedFileSystemException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Maintains the file system specific hierarchy.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class JVFSFileSystem extends FileSystem {

    /**
     * Contracted name of the {@link BasicFileAttributeView}.
     */
    static final String FILE_ATTR_VIEW_BASIC = "basic";

    /**
     * Provider which created this {@link JVFSFileSystemProvider}.
     */
    private final JVFSFileSystemProvider provider;
    private final Map<String, Entry> attic = new HashMap<String, Entry>();
    /**
     * List of file stores.
     */
    private final List<FileStore> fileStores;
    /**
     * Whether or not this FS is open.
     *
     * Volatile as we don't need compound operations and thus don't need full sync.
     */
    private volatile boolean open;

    JVFSFileSystem(final JVFSFileSystemProvider provider) {
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
        return JVFSFileSystems.DIR_SEP;
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        this.checkClosed();
        return Arrays.<Path>asList(new JVFSPath(this));
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
            merged.append(JVFSFileSystems.DIR_SEP);
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

    public Entry get(final String path) {
        return attic.get(path);
    }

    public boolean contains(final String path) {
        return attic.containsKey(path);
    }

    public void checkAccess(final Path path, final AccessMode... modes) throws NoSuchFileException, AccessDeniedException {
        final String pathname = path.toString();
        if (!contains(pathname)) {
            throw new NoSuchFileException(pathname);
        }

        boolean r = false;
        boolean w = false;
        boolean x = false;
        for (AccessMode mode : modes) {
            switch (mode) {
                case READ:
                    r = true;
                    break;
                case WRITE:
                    w = true;
                    break;
                case EXECUTE:
                    x = true;
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        final Entry entry = get(pathname);

        if (r) {
            if (!entry.readable) {
                throw new AccessDeniedException(pathname);
            }
        }

        if (w) {
            if (isReadOnly()) {
                throw new AccessDeniedException(pathname);
            }

            if (!entry.writeable) {
                throw new AccessDeniedException(pathname);
            }
        }

        if (x) {
            if (!entry.executable) {
                throw new AccessDeniedException(pathname);
            }
        }
    }

    public static final class Entry {

        private final String path;
        private final boolean direcotry;
        private long lastModifiedTime;
        private long lastAccessTime;
        private long creationTime;
        private boolean readable;
        private boolean writeable;
        private boolean executable;

        private Entry(final String path, final boolean direcotry) {
            super();
            this.path = path;
            this.direcotry = direcotry;
        }

        public static Entry newDir(final String path) {
            return new Entry(path, true);
        }

        public static Entry newFile(final String path) {
            return new Entry(path, false);
        }

        @Override
        public int hashCode() {
            return path.hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }

            final Entry other = (Entry) obj;
            return path.equals(other.path);
        }

        @Override
        public String toString() {
            return path;
        }

        public boolean isDirectory() {
            return direcotry;
        }

        public long getLastModifiedTime() {
            return lastModifiedTime;
        }

        public long getLastAccessTime() {
            return lastAccessTime;
        }

        public long getCreationTime() {
            return creationTime;
        }

    }

}
