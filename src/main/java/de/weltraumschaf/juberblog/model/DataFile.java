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
package de.weltraumschaf.juberblog.model;

import de.weltraumschaf.juberblog.Constants;
import java.io.File;

/**
 * Represents a data file.
 *
 * In JUberblog the data file is the central data storage. It is based on Markdown files with some additions. On of the
 * additions is the the file name convention.
 *
 * The file names are produced by this schema:
 * <pre>
 * filename := TIMESTAMP '.' SLUG '.md' ;
 * </pre>
 *
 * TIMESTAMP is the Unix time stamp when the file was created. SLUG is th slugged form of the title.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class DataFile {

    private static final String TIMESTAMP_SEP = ".";
    private static final int UNITIALIZED = -1;
    private final String filename;
    /**
     * Lazy computed.
     */
    private String basename;
    /**
     * Lazy computed.
     */
    private long creationTime = UNITIALIZED;
    /**
     * Lazy computed.
     */
    private String slug;

    public DataFile(final File file) {
        this(file.getAbsolutePath());
    }

    public DataFile(final String filename) {
        super();
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public String getBasename() {
        if (null == basename) {
            final int pos = filename.lastIndexOf(Constants.DIR_SEP.toString()) + 1;
            basename = filename.substring(pos);
        }

        return basename;
    }

    public long getCreationTime() {
        if (UNITIALIZED == creationTime) {
            final String name = getBasename();
            final int pos = name.indexOf(TIMESTAMP_SEP);
            creationTime = Long.valueOf(name.substring(0, pos));
        }

        return creationTime;
    }

    public String getSlug() {
        if (null == slug) {
            final String name = getBasename();
            final int start = name.indexOf(TIMESTAMP_SEP) + 1;
            final int stop = name.lastIndexOf(".");
            slug = name.substring(start, stop);
        }

        return slug;
    }

    @Override
    public String toString() {
        return filename.toString();
    }

    @Override
    public int hashCode() {
        return filename.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof DataFile)) {
            return false;
        }

        final DataFile other = (DataFile) obj;
        return filename.equals(other.getFilename());
    }


}
