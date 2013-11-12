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
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Represents a file channel.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class JVFSFileChannel extends FileChannel {

    /**
     * Wrapped byte channel.
     */
    private final SeekableByteChannel channel;

    /**
     * Dedicated constructor.
     *
     * @param channel must not be {@codenull}
     */
    JVFSFileChannel(final SeekableByteChannel channel) {
        super();
        JVFSAssertions.notNull(channel, "channel");
        this.channel = channel;
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        return channel.read(dst);
    }

    @Override
    public long read(final ByteBuffer[] dsts, final int offset, final int length) throws IOException {
        JVFSAssertions.notNull(dsts, "dsts");
        JVFSAssertions.greaterThan(offset, -1, "offset");
        JVFSAssertions.greaterThan(length, -1, "offset");
        final int max = Math.max(dsts.length, length);
        final long start = channel.position();

        for (int i = offset; i < max; ++i) {
            final ByteBuffer buffer = dsts[i];
            read(buffer);
        }

        return channel.position() - start;
    }

    @Override
    public int read(ByteBuffer dst, long position) throws IOException {
        channel.position(position);
        return channel.read(dst);
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        return channel.write(src);
    }

    @Override
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        JVFSAssertions.notNull(srcs, "srcs");
        JVFSAssertions.greaterThan(offset, -1, "offset");
        JVFSAssertions.greaterThan(length, -1, "offset");
        final int max = Math.max(srcs.length, length);
        final long start = channel.position();

        for (int i = offset; i < max; ++i) {
            final ByteBuffer buffer = srcs[i];
            write(buffer);
        }

        return channel.position() - start;
    }


    @Override
    public int write(ByteBuffer src, long position) throws IOException {
        channel.position(position);
        return channel.write(src);
    }

    @Override
    public long position() throws IOException {
        return channel.position();
    }

    @Override
    public FileChannel position(long newPosition) throws IOException {
        channel.position(newPosition);
        return this;
    }

    @Override
    public long size() throws IOException {
        return channel.size();
    }

    @Override
    public FileChannel truncate(long size) throws IOException {
        channel.truncate(size);
        return this;
    }

    @Override
    public void force(boolean metaData) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long transferTo(long position, long count, WritableByteChannel target) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MappedByteBuffer map(MapMode mode, long position, long size) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FileLock lock(long position, long size, boolean shared) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FileLock tryLock(long position, long size, boolean shared) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void implCloseChannel() throws IOException {
        if (!channel.isOpen()) {
            throw new IllegalStateException("Channel not open!");
        }

        channel.close();
    }

}
