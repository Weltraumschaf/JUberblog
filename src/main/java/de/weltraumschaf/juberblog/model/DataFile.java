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
import org.apache.commons.lang3.Validate;

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

    /**
     * Separates the timestamp from the beginning of the file base name.
     */
    private static final String TIMESTAMP_SEP = ".";
    /**
     * Signals that the {@link #creationTime} is not initialized.
     */
    private static final int UNITIALIZED = -1;
    /**
     * The original absolute file name.
     */
    private final File file;
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

    /**
     * Dedicated constructor.
     *
     * @param file must not be {@code null} or empty
     */
    public DataFile(final File file) {
        super();
        Validate.notNull(file, "Filename must not be null or empty!");
        this.file = file;
    }

    /**
     * Get the absolute file name.
     *
     * @return never {@code null} or empty
     */
    public String getFilename() {
        return file.getAbsolutePath();
    }

    /**
     * Get the file base name.
     *
     * The base name is the part after the last OS dependent directory separator.
     *
     * @return never {@code null}
     */
    public String getBasename() {
        if (null == basename) {
            final int pos = getFilename().lastIndexOf(Constants.DIR_SEP.toString()) + 1;
            basename = getFilename().substring(pos);
        }

        return basename;
    }

    /**
     * Get the creation time stamp extracted from the file name.
     *
     * @return greater -1
     */
    public long getCreationTime() {
        if (UNITIALIZED == creationTime) {
            final String name = getBasename();
            final int pos = name.indexOf(TIMESTAMP_SEP);
            creationTime = Long.valueOf(name.substring(0, pos));
        }

        return creationTime;
    }

    /**
     * Get the slug part of the file name.
     *
     * The slug part is the part between the timestamp and the file extension.
     *
     * @return never {@code null}
     */
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
        return file.toString();
    }

    @Override
    public int hashCode() {
        return file.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof DataFile)) {
            return false;
        }

        final DataFile other = (DataFile) obj;
        return getFilename().equals(other.getFilename());
    }

}
