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
import java.nio.channels.SeekableByteChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class JVFSSeekableByteChannel implements SeekableByteChannel {

    private static final int INITIAL_SIZE = 128;

    private final Path path;
    private final Set<? extends OpenOption> options;
    private final FileAttribute<?>[] atributes;
    private List<Byte> data = new ArrayList<Byte>();
    private volatile boolean open;
    private volatile int position;

    public JVFSSeekableByteChannel(final Path path, final Set<? extends OpenOption> options, final FileAttribute<?>[] attrs) {
        super();
        JVFSAssertions.notNull(path, "path");
        JVFSAssertions.notNull(options, "options");
        JVFSAssertions.notNull(attrs, "attrs");
        this.path = path;
        this.options = options;
        this.atributes = attrs;
        this.open = true;
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        for (final Byte b : data.subList(position, data.size())) {
            dst.put(b);
            position++;
        }

        return position;
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        JVFSAssertions.notNull(src, "src");

        while (src.hasRemaining()) {
            data.add(position, src.get());
            position++;
        }

        return position;
    }

    @Override
    public long position() throws IOException {
        return position;
    }

    @Override
    public SeekableByteChannel position(final long newPosition) throws IOException {
        position= (int) newPosition;
        return this;
    }

    @Override
    public long size() throws IOException {
        return data.size();
    }

    @Override
    public SeekableByteChannel truncate(final long size) throws IOException {
        data = data.subList(0, (int) size);
        return this;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void close() throws IOException {
        if (!open) {
            throw new IllegalStateException("Channel not open!");
        }

        open = false;
    }

}
