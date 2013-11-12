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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessDeniedException;
import java.nio.file.AccessMode;
import java.nio.file.ClosedFileSystemException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.ReadOnlyFileSystemException;
import java.nio.file.StandardOpenOption;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Maintains the file system specific hierarchy.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class JvfsFileSystem extends FileSystem {

    /**
     * Contracted name of the {@link BasicFileAttributeView}.
     */
    static final String FILE_ATTR_VIEW_BASIC = "basic";

    /**
     * Provider which created this {@link JvfsFileSystemProvider}.
     */
    private final JvfsFileSystemProvider provider;
    /**
     * Organizes the file hierarchy.
     *
     * The key is the absolute pathname of the file ({@link Entry#path}).
     */
    private final Map<String, JvfsFileEntry> attic = JvfsCollections.newHashMap();
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

    /**
     * Dedicated constructor.
     *
     * @param provider must not be {@literal null}
     */
    JvfsFileSystem(final JvfsFileSystemProvider provider) {
        super();
        JvfsAssertions.notNull(provider, "provider");
        this.provider = provider;
        this.open = true;
        final FileStore store = new JvfsFileStore();
        final List<FileStore> stores = JvfsCollections.newArrayList(1);
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
        return JvfsFileSystems.DIR_SEP;
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        this.checkClosed();
        return Arrays.<Path>asList(new JvfsPath(this));
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        this.checkClosed();
        return fileStores;
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        this.checkClosed();
        return JvfsCollections.newHashSet();
    }

    @Override
    public Path getPath(final String first, final String... more) {
        this.checkClosed();
        JvfsAssertions.notNull(first, "first");
        final String merged = this.merge(first, more);
        return new JvfsPath(merged, this);
    }

    /**
     * Merges the path context with a varargs String sub-contexts, returning the result.
     *
     * @param first must not be {@literal null}
     * @param more must not be {@literal null}
     * @return never {@literal null}
     */
    private String merge(final String first, final String... more) {
        assert first != null : "first must be specified";
        assert more != null : "more must be specified";

        final StringBuilder merged = new StringBuilder();
        merged.append(first);

        for (final String name : more) {
            merged.append(JvfsFileSystems.DIR_SEP);
            merged.append(name);
        }

        return merged.toString();
    }

    @Override
    public PathMatcher getPathMatcher(final String syntaxAndPattern) {
        return JvfsPathMatcher.newMatcher(syntaxAndPattern);
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WatchService newWatchService() throws IOException {
        throw new UnsupportedOperationException("JVFS archives do not support a watch services!");
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

    private void assertFileExists(final String path) throws NoSuchFileException {
        if (!contains(path)) {
            throw new NoSuchFileException(path);
        }
    }

    JvfsFileEntry get(final String path) {
        checkClosed();
        return attic.get(path);
    }

    boolean contains(final String path) {
        checkClosed();
        return attic.containsKey(path);
    }

    FileChannel newFileChannel(final String path, final Set<? extends OpenOption> options, final FileAttribute<?>... attrs) throws IOException {
        checkClosed();
        final boolean forWrite = (options.contains(StandardOpenOption.WRITE) || options.contains(StandardOpenOption.APPEND));

        if (forWrite) {
            if (isReadOnly()) {
                throw new ReadOnlyFileSystemException();
            }

            if (contains(path)) {
                if (options.contains(StandardOpenOption.CREATE_NEW)) {
                    throw new FileAlreadyExistsException(path);
                }

                if (get(path).isDirectory()) {
                    throw new FileAlreadyExistsException("directory <" + path + "> exists");
                }
            } else {
                if (!options.contains(StandardOpenOption.CREATE_NEW)) {
                    throw new NoSuchFileException(path);
                }
            }
        } else {
            if (contains(path)) {
                if (get(path).isDirectory()) {
                    throw new NoSuchFileException(path);
                }
            } else {
                throw new NoSuchFileException(path);
            }
        }

        final JvfsFileEntry entry = contains(path) ? get(path) : JvfsFileEntry.newFile(path);
        return new JvfsFileChannel(entry.getContent());
    }

    SeekableByteChannel newByteChannel(final String path, final Set<? extends OpenOption> options, final FileAttribute<?>... attrs) {
        checkClosed();
        final JvfsFileEntry entry;

        if (contains(path)) {
            entry = get(path);
        } else {
            entry = JvfsFileEntry.newFile(path);
        }

        return entry.getContent();
    }

    void checkAccess(final String pathname, final AccessMode... modes) throws IOException {
        checkClosed();
        assertFileExists(pathname);

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

        final JvfsFileEntry entry = get(pathname);

        if (r) {
            if (!entry.isReadable()) {
                throw new AccessDeniedException(pathname);
            }
        }

        if (w) {
            if (isReadOnly()) {
                throw new AccessDeniedException(pathname);
            }

            if (!entry.isWritable()) {
                throw new AccessDeniedException(pathname);
            }
        }

        if (x) {
            if (!entry.isExecutable()) {
                throw new AccessDeniedException(pathname);
            }
        }
    }

    void createDirectory(final String path, final FileAttribute<?>... attrs) throws IOException {
        checkClosed();
        assertFileExists(path);
        final List<String> names = JvfsPath.tokenize(path);
        names.remove(names.size() - 1);
        final StringBuilder buf = new StringBuilder();

        for (final String name : names) {
            buf.append(JvfsFileSystems.DIR_SEP).append(name);

            if (!contains(buf.toString())) {
                attic.put(buf.toString(), JvfsFileEntry.newDir(buf.toString()));
            }
        }
    }

    void delete(final String path) throws IOException {
        checkClosed();
        assertFileExists(path);
        final JvfsFileEntry entry = get(path);

        if (entry.isDirectory() && entry.isEmpty()) {
            throw new DirectoryNotEmptyException(path);
        }

        attic.remove(path);
    }

    JvfsFileAttributes getFileAttributes(final String path) throws IOException {
        checkClosed();
        assertFileExists(path);
        return new JvfsFileAttributes(get(path));
    }

    void setTimes(final String path, final FileTime mtime, final FileTime atime, final FileTime ctime) throws IOException {
        checkClosed();
        assertFileExists(path);
        final JvfsFileEntry entry = get(path);
        entry.setLastModifiedTime(mtime.to(TimeUnit.SECONDS));
        entry.setLastAccessTime (atime.to(TimeUnit.SECONDS));
        entry.setCreationTime (ctime.to(TimeUnit.SECONDS));
    }

    void copy(final String path, final String target, final CopyOption... options) throws IOException {
        checkClosed();
        assertFileExists(path);

        if (contains(target)) {
            throw new FileAlreadyExistsException(target);
        }

        attic.put(target, get(path).copy());
    }

    void move(final String path, final String target, final CopyOption... options) throws IOException {
        checkClosed();
        assertFileExists(path);

        if (contains(target)) {
            throw new FileAlreadyExistsException(target);
        }

        final JvfsFileEntry entry = get(path);
        synchronized (attic) {
            attic.remove(entry.getPath());
            attic.put(target, entry);
        }
    }

    FileStore getFileStore() {
        return fileStores.get(0);
    }

    boolean isHidden(final String path) throws IOException {
        checkClosed();
        assertFileExists(path);
        return get(path).isHidden();
    }

}
