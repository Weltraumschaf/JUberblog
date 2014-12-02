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
package de.weltraumschaf.juberblog.file;

import de.weltraumschaf.commons.validate.Validate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Abstracts a file from file system which contains blog data.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class DataFile {

    /**
     * Absolute file name of data file.
     */
    private final String absoluteFileName;
    /**
     * Lazy computed and not included in {@link #hashCode()} and {@link #equals(java.lang.Object)}.
     */
    private Path path;
    /**
     * Lazy computed and not included in {@link #hashCode()} and {@link #equals(java.lang.Object)}.
     */
    private String bareName;
    /**
     * Lazy computed and not included in {@link #hashCode()} and {@link #equals(java.lang.Object)}.
     */
    private String content;

    /**
     * Dedicated constructor.
     *
     * @param absoluteFileName must not be {@code null} or empty
     */
    public DataFile(final String absoluteFileName) {
        super();
        this.absoluteFileName = Validate.notEmpty(absoluteFileName, "absoluteFileName");
    }

    /**
     * Creates path object for file.
     *
     * @return never {@code null}, same instance
     */
    public Path getPath() {
        if (null == path) {
            path = Paths.get(absoluteFileName);
        }

        return path;
    }

    /**
     * Computes the bare file name.
     * <p>
     * The method strips the leading time stamp and the trailing file extension.
     * </p>
     * <p>
     * Example: For a file name like {@code /foo/bar/baz/2014-05-30T21.29.20_This-is-the-First-Post.md} the bare name
     * is {@code This-is-the-First-Post}.
     * </p>
     * @return never {@code null}, same instance
     */
    public String getBareName() {
        if (null == bareName) {
            final int firstDashPosition = absoluteFileName.lastIndexOf("_");
            final int lastDotPosition = absoluteFileName.lastIndexOf(".");
            bareName = absoluteFileName.substring(firstDashPosition + 1, lastDotPosition);
        }

        return bareName;
    }

    /**
     * Reads the file content.
     *
     * @param encoding must not be {@code null} or empty
     * @return never {@code null}, same instance
     * @throws IOException if file can't be read
     */
    public String readContent(final String encoding) throws IOException {
        if (null == content) {
            content = new String(Files.readAllBytes(getPath()), Validate.notEmpty(encoding, "encoding"));
        }

        return content;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.absoluteFileName);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof DataFile)) {
            return false;
        }

        final DataFile other = (DataFile) obj;

        return Objects.equals(this.absoluteFileName, other.absoluteFileName);
    }

    @Override
    public String toString() {
        return "DataFile{" + "absoluteFileName=" + absoluteFileName + '}';
    }

}
