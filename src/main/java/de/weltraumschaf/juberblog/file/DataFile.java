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
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.joda.time.DateTime;

/**
 * Abstracts a file from file system which contains blog data.
 * <p>
 * A data file is expected to have a file name in this format:
 * {@code YYYY-MM-DDTHH.MM.DD_This-is-the-First-Post.md}.
 * </p>
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class DataFile {

    /**
     * Separates the creation date from the rest of the file name.
     */
    private static final String DATE_BARE_NAME_SEPARATOR = "_";
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
     * Lazy computed file base name.
     */
    private String baseName;
    /**
     * Lazy computed from the file name.
     */
    private DateTime creationDate;
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
     * get the base name part of the absolute file name.
     *
     * @return never {@code null}, same instance
     */
    public String getBaseName() {
        if (null == baseName) {
            final int lastSlashPosition = absoluteFileName.lastIndexOf(FileSystems.getDefault().getSeparator());
            baseName = absoluteFileName.substring(lastSlashPosition + 1);
        }

        return baseName;
    }

    /**
     * Computes the bare file name.
     * <p>
     * The method strips the leading time stamp and the trailing file extension.
     * </p>
     * <p>
     * Example: For a file name like {@code /foo/bar/baz/2014-05-30T21.29.20_This-is-the-First-Post.md} the bare name is
     * {@code This-is-the-First-Post}.
     * </p>
     *
     * @return never {@code null}, same instance
     */
    public String getBareName() {
        if (null == bareName) {
            final int firstDashPosition = getBaseName().indexOf(DATE_BARE_NAME_SEPARATOR);
            final int lastDotPosition = getBaseName().lastIndexOf(".");
            bareName = getBaseName().substring(firstDashPosition + 1, lastDotPosition);
        }

        return bareName;
    }

    /**
     * Get creation date.
     *
     * @return never {@code null}, same instance
     */
    public DateTime getCreationDate() {
        if (null == creationDate) {
            final int firstDashPosition = getBaseName().indexOf(DATE_BARE_NAME_SEPARATOR);
            final String dateTime = getBaseName().substring(0, firstDashPosition).replaceAll("\\.", ":");
            creationDate = new DateTime(dateTime);
        }

        return creationDate;
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
