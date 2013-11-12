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
import java.nio.channels.SeekableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Holds the administrative data of a file entry in the virtual file system.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class JVFSFileEntry {

    /**
     * Holds the file data.
     */
    private final SeekableByteChannel content = new JVFSSeekableByteChannel();
    /**
     * Absolute path name of the file system entry.
     */
    private final String path;
    /**
     * Whether it is a directory or not.
     */
    private final boolean direcotry;
    /**
     * Last modification time.
     */
    private long lastModifiedTime;
    /**
     * Last access time.
     */
    private long lastAccessTime;
    /**
     * Creation time.
     */
    private long creationTime;
    /**
     * Whether it is readable.
     */
    private boolean readable;
    /**
     * Whether it is writable.
     */
    private boolean writable;
    /**
     * Whether it is executable.
     */
    private boolean executable;
    /**
     * Whether it is hidden.
     */
    private boolean hidden;

    /**
     * Copy constructor.
     *
     * Hidden: Use {@link #copy()} to create a copy of an entry.
     *
     * @param src must not be {@code null}
     */
    private JVFSFileEntry(final JVFSFileEntry src) {
        this(src.path, src.direcotry);
        this.lastModifiedTime = src.lastModifiedTime;
        this.lastAccessTime = src.lastAccessTime;
        this.creationTime = src.creationTime;
        this.readable = src.readable;
        this.writable = src.writable;
        this.executable = src.executable;
        this.hidden = src.hidden;
    }

    /**
     * Dedicated constructor.
     *
     * Hidden: Use either {@link #newDir(java.lang.String)} or {@link #newFile(java.lang.String)}.
     *
     * @param path must not be {@code null} or empty
     * @param direcotry {@code true} if it is a directory, else {@code false}
     */
    private JVFSFileEntry(final String path, final boolean direcotry) {
        super();
        assert path != null : "path must not be null";
        assert !path.isEmpty() : "path must not be empty";
        this.path = path;
        this.direcotry = direcotry;
    }

    /**
     * Creates a new directory entry.
     *
     * @param path must not be {@literal null} or empty
     * @return never {@literal null}
     */
    public static JVFSFileEntry newDir(final String path) {
        return new JVFSFileEntry(path, true);
    }

    /**
     * Creates a new file entry.
     *
     * @param path must not be {@literal null} or empty
     * @return never {@literal null}
     */
    public static JVFSFileEntry newFile(final String path) {
        return new JVFSFileEntry(path, false);
    }

    /**
     * Creates complete deep copy.
     *
     * @return never {@literal null}
     */
    public JVFSFileEntry copy() {
        return new JVFSFileEntry(this);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof JVFSFileEntry)) {
            return false;
        }

        final JVFSFileEntry other = (JVFSFileEntry) obj;
        return path.equals(other.path);
    }

    @Override
    public String toString() {
        return path;
    }

    /**
     * Get the absolute path.
     *
     * @return never {@code null} or empty
     */
    String getPath() {
        return path;
    }

    /**
     * Whether it is a directory.
     *
     * @return {@literal true} means it is a directory, {@literal false} means file
     */
    boolean isDirectory() {
        return direcotry;
    }

    /**
     * Whether the entry is hidden or not.
     *
     * @return {@code true} if it is hidden, else {@code false}
     */
    boolean isHidden() {
        return hidden;
    }

    /**
     * Get last modification time in seconds.
     *
     * @return non negative number
     */
    long getLastModifiedTime() {
        return lastModifiedTime;
    }

    /**
     * Set last modification time in seconds.
     *
     * @param timestamp must be non negative
     */
    void setLastModifiedTime(final long timestamp) {
        JVFSAssertions.greaterThanEqual(timestamp, 0, "timestamp");
        this.lastModifiedTime = timestamp;
    }

    /**
     * Get last access time in seconds.
     *
     * @return non negative number
     */
    long getLastAccessTime() {
        return lastAccessTime;
    }

    /**
     * Set last access time in seconds.
     *
     * @param timestamp must be non negative
     */
    void setLastAccessTime(final long timestamp) {
        JVFSAssertions.greaterThanEqual(timestamp, 0, "timestamp");
        this.lastAccessTime = timestamp;
    }

    /**
     * Get last creation in seconds.
     *
     * @return non negative number
     */
    long getCreationTime() {
        return creationTime;
    }

    /**
     * Set creation time in seconds.
     *
     * @param timestamp must be non negative
     */
    void setCreationTime(final long timestamp) {
        JVFSAssertions.greaterThanEqual(timestamp, 0, "timestamp");
        this.creationTime = timestamp;
    }

    /**
     * Whether the file is readable.
     *
     * @return {@code true} if readable, else {@code false}
     */
    boolean isReadable() {
        return readable;
    }

    /**
     * Whether the file is writable.
     *
     * @return {@code true} if writable, else {@code false}
     */
    boolean isWritable() {
        return writable;
    }

    /**
     * Whether the file is executable.
     *
     * @return {@code true} if executable, else {@code false}
     */
    boolean isExecutable() {
        return executable;
    }

    /**
     * If it has child entries if it is a directory.
     *
     * @return {@literal true} if it is an empty directory or file, else {@literal false}
     */
    boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Whether it is a symbolic link.
     *
     * @return {@literal true} if it is a symbolic link, else {@literal false}
     */
    boolean isSymbolicLink() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Get the size of the file content in bytes.
     *
     * @return -1 if it is a directory, else size
     */
    long size() {
        if (this.isDirectory()) {
            return -1L;
        }
        try {
            return content.size();
        } catch (IOException ex) {
            return -1L;
        }
    }

    /**
     * Get the file content as seekable byte channel.
     *
     * @return never {@code null}
     */
    SeekableByteChannel getContent() {
        return content;
    }

}
