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
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;

/**
 * Implements the I/O operations.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class JVFSFileSystemProvider extends FileSystemProvider {

    private JVFSFileSystem fileSystem;

    /**
     * Dedicated constructor.
     */
    public JVFSFileSystemProvider() {
        super();
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

    @Override
    public FileSystem newFileSystem(final URI uri, final Map<String, ?> env) throws IOException {
        // FIXME See ZipFS.
        return new JVFSFileSystem(this);
    }

    @Override
    public synchronized FileSystem getFileSystem(final URI uri) {
        // FIXME See ZipFS.
        JVFSAssertions.isEqual(uri, JVFSFileSystems.getRootUri(), "uri");

        if (null == fileSystem) {
            fileSystem = new JVFSFileSystem(this);
        }

        return fileSystem;
    }

    @Override
    public Path getPath(final URI uri) {
        return new JVFSPath(uri.getPath(), fileSystem);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.nio.file.spi.FileSystemProvider#newFileChannel(java.nio.file.Path, java.util.Set,
     * java.nio.file.attribute.FileAttribute<?>[])
     */
    @Override
    public FileChannel newFileChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        return new JVFSFileChannel(newByteChannel(path, options, attrs));
    }

    @Override
    public SeekableByteChannel newByteChannel(final Path path, final Set<? extends OpenOption> options, final FileAttribute<?>... attrs) throws IOException {
        return new JVFSSeekableByteChannel(path, options, attrs);
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Path path) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void copy(Path source, Path target, CopyOption... options) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void move(Path source, Path target, CopyOption... options) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isSameFile(Path path, Path path2) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isHidden(Path path) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FileStore getFileStore(Path path) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void checkAccess(final Path path, final AccessMode... modes) throws IOException {
        final JVFSFileSystem fs = getFilesystem(path);
        final String desired = path.toString();

        if (!fs.contains(desired)) {
            throw new NoSuchFileException(desired);
        }

        fs.checkAccess(path, modes);
    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private JVFSFileSystem getFilesystem(final Path path) {
        assert path != null : "Path must be specified";
        final FileSystem fs = path.getFileSystem();
        assert fs != null : "File system is null";

        // Could be user error in this case, passing in a Path from another provider
        if (!(fs instanceof JVFSFileSystem)) {
            throw new IllegalArgumentException("This path is not associated with a "
                    + JVFSFileSystem.class.getSimpleName());
        }

        return (JVFSFileSystem) fs;
    }
}
