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
import java.net.URI;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Implements the I/O operations.
 *
 * Inspired by implementation of {@link sun.nio.fs.LinuxFileSystemProvider} and ZipFS demo.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class JVFSFileSystemProvider extends FileSystemProvider {

    /**
     * The one and only file system.
     */
    private final JVFSFileSystem fileSystem;

    /**
     * Dedicated constructor.
     */
    public JVFSFileSystemProvider() {
        super();
        fileSystem = new JVFSFileSystem(this);
    }

    /**
     * Constructor used by {@link FileSystems} to create default provider.
     *
     * @param parent is ignored at the moment
     */
    public JVFSFileSystemProvider(final FileSystemProvider parent) {
        this();
    }

    @Override
    public String getScheme() {
        return JVFSFileSystems.PROTOCOL;
    }

    private void checkUri(URI uri) {
        if (!uri.getScheme().equalsIgnoreCase(getScheme())) {
            throw new IllegalArgumentException("URI does not match this provider!");
        }

        if (uri.getAuthority() != null) {
            throw new IllegalArgumentException("Authority component present!");
        }

        if (uri.getPath() == null) {
            throw new IllegalArgumentException("Path component is undefined!");
        }

        if (!uri.getPath().equals(JVFSFileSystems.DIR_SEP)) {
            throw new IllegalArgumentException(String.format("Path component should be '%s'", JVFSFileSystems.DIR_SEP));
        }

        if (uri.getQuery() != null) {
            throw new IllegalArgumentException("Query component present!");
        }

        if (uri.getFragment() != null) {
            throw new IllegalArgumentException("Fragment component present!");
        }
    }

    @Override
    public FileSystem newFileSystem(final URI uri, final Map<String, ?> env) throws IOException {
        checkUri(uri);
        throw new FileSystemAlreadyExistsException();
    }

    @Override
    public FileSystem getFileSystem(final URI uri) {
        checkUri(uri);
        return fileSystem;
    }

    @Override
    public Path getPath(final URI uri) {
        return new JVFSPath(uri.getPath(), fileSystem);
    }

    @Override
    public AsynchronousFileChannel newAsynchronousFileChannel(
            final Path path,
            final Set<? extends OpenOption> options,
            final ExecutorService exec,
            final FileAttribute<?>... attrs)
            throws IOException {
        throw new UnsupportedOperationException("Not supported yet!");
    }

    @Override
    public FileChannel newFileChannel(
            final Path path,
            final Set<? extends OpenOption> options,
            final FileAttribute<?>... attrs) throws IOException {
        return toJvfsPath(path).newFileChannel(options, attrs);
    }

    @Override
    public SeekableByteChannel newByteChannel(
            final Path path,
            final Set<? extends OpenOption> options,
            final FileAttribute<?>... attrs) throws IOException {
        return toJvfsPath(path).newByteChannel(options, attrs);
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(
            final Path dir,
            final DirectoryStream.Filter<? super Path> filter) throws IOException {
        return toJvfsPath(dir).newDirectoryStream(filter);
    }

    @Override
    public void createDirectory(final Path dir, final FileAttribute<?>... attrs) throws IOException {
        toJvfsPath(dir).createDirectory(attrs);
    }

    @Override
    public void delete(final Path path) throws IOException {
        toJvfsPath(path).delete();
    }

    @Override
    public void copy(final Path source, final Path target, final CopyOption... options) throws IOException {
        toJvfsPath(source).copy(toJvfsPath(target), options);
    }

    @Override
    public void move(final Path source, final Path target, final CopyOption... options) throws IOException {
        toJvfsPath(source).move(toJvfsPath(target), options);
    }

    @Override
    public boolean isSameFile(final Path path, final Path path2) throws IOException {
        return toJvfsPath(path).isSameFile(path2);
    }

    @Override
    public boolean isHidden(final Path path) throws IOException {
        return toJvfsPath(path).isHidden();
    }

    @Override
    public FileStore getFileStore(final Path path) throws IOException {
        return toJvfsPath(path).getFileStore();
    }

    @Override
    public void checkAccess(final Path path, final AccessMode... modes) throws IOException {
        toJvfsPath(path).checkAccess(modes);
    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(
            final Path path,
            final Class<V> type,
            final LinkOption... options) {
        return (V) new JVFSFileAttributeView(toJvfsPath(path));
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(
            final Path path,
            final Class<A> type,
            final LinkOption... options) throws IOException {
        if (type == BasicFileAttributes.class) {
            return (A) toJvfsPath(path).getAttributes();
        }

        return null;
    }

    @Override
    public Map<String, Object> readAttributes(
            final Path path,
            final String attributes,
            final LinkOption... options) throws IOException {
        return toJvfsPath(path).readAttributes(attributes, options);
    }

    @Override
    public void setAttribute(
            final Path path,
            final String attribute,
            final Object value,
            final LinkOption... options) throws IOException {
        toJvfsPath(path).setAttribute(attribute, value, options);
    }

    /**
     * Casts given path to {@link JVFSPath}.
     *
     * Throws a {@link ProviderMismatchException} if given object is not instance of {@link JVFSPath}.
     *
     * @param obj must not be {@code null}
     * @return never {@code null}
     */
    private static JVFSPath toJvfsPath(final Path obj) {
        JVFSAssertions.notNull(obj, "obj");

        if (!(obj instanceof JVFSPath)) {
            throw new ProviderMismatchException("Given path not of type " + JVFSPath.class.getSimpleName() + "!");
        }

        return (JVFSPath) obj;
    }
}
