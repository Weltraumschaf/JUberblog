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

import de.weltraumschaf.commons.guava.Objects;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.Headline;
import de.weltraumschaf.juberblog.Preprocessor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.apache.commons.io.IOUtils;

/**
 * Represents a data file.
 * <p>
 * In JUberblog the data file is the central data storage. It is based on Markdown files with some additions. On of the
 * additions is the the file name convention.
 * </p>
 * <p>
 * The file names are produced by this schema:
 * </p>
 * <pre>
 * filename := TIMESTAMP '_' SLUG '.md' ;
 * </pre>
 * <p>
 * TIMESTAMP is time stamp when the file was created and as format {@literal yyyy-mm-ddThh.mm.ss}.
 * SLUG is th slugged form of the title.
 * </p>
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class DataFile {

    /**
     * Absolute file name.
     */
    private final String fileName;
    /**
     * The base name of {@link #fileName}.
     */
    private final String baseName;
    /**
     * Files creation time.
     */
    private final long creationTime;
    /**
     * Files last modification time.
     */
    private final long modificationTime;
    /**
     * The part of the {@link #baseName} between the timestamp and the file extension.
     */
    private final String slug;
    /**
     * The page's headline.
     */
    private final String headline;
    /**
     * The page's markdown.
     */
    private final String markdown;
    /**
     * The page's meta data.
     */
    private final MetaData metadata;
    private final Type type;

    /**
     * Dedicated constructor.
     *
     * @param fileName must not be {@code null} empty
     * @param baseName must not be {@code null} empty
     * @param creationTime must not be less than 0L
     * @param modificationTime must not be less than 0L
     * @param slug must not be {@code null} empty
     * @param headline must not be {@code null} empty
     * @param markdown must not be {@code null} empty
     * @param metadata must not be {@code null}
     * @param type must not be {@code null}
     */
    public DataFile(
            final String fileName,
            final String baseName,
            final long creationTime,
            final long modificationTime,
            final String slug,
            final String headline,
            final String markdown,
            final MetaData metadata,
            final Type type) {
        this.fileName = Validate.notEmpty(fileName, "fileName");
        this.baseName = Validate.notEmpty(baseName, "baseName");
        this.creationTime = Validate.greaterThanOrEqual(creationTime, 0L, "creationTime");
        this.modificationTime = Validate.greaterThanOrEqual(modificationTime, 0L, "modificationTime");
        this.slug = Validate.notEmpty(slug, "slug");
        this.headline = Validate.notEmpty(headline, "headline");
        this.markdown = Validate.notEmpty(markdown, "markdown");
        this.metadata = Validate.notNull(metadata, "metadata");
        this.type = Validate.notNull(type, "type");
    }

    /**
     * Get the absolute file name.
     *
     * @return never {@code null} or empty
     */
    public String getFilename() {
        return fileName;
    }

    /**
     * Get the file base name.
     *
     * The base name is the part after the last OS dependent directory separator.
     *
     * @return never {@code null}
     */
    public String getBasename() {
        return baseName;
    }

    /**
     * Get the creation time stamp extracted from the file name.
     *
     * @return greater -1
     */
    public long getCreationTime() {
        return creationTime;
    }

    /**
     * Get last modification day of file.
     *
     * @return non negative long number
     */
    public long getModificationTime() {
        return modificationTime;
    }

    /**
     * Get the slug part of the file name.
     *
     * The slug part is the part between the timestamp and the file extension.
     *
     * @return never {@code null}
     */
    public String getSlug() {
        return slug;
    }

    /**
     * Get processed meta data of file.
     *
     * This method reads and parses the file content.
     *
     * @return never {@code  null}
     * @throws IOException if file can't be read
     */
    public MetaData getMetaData() throws IOException {
        return metadata;
    }

    /**
     * Get raw Markdown of file.
     *
     * This method reads and parses the file content.
     *
     * @return never {@code  null}
     * @throws IOException if file can't be read
     */
    public String getMarkdown() throws IOException {
        return markdown;
    }

    /**
     * Get processed headline of file.
     *
     * This method reads and parses the file content.
     *
     * @return never {@code  null}
     * @throws IOException if file can't be read
     */
    public String getHeadline() throws IOException {
        return headline;
    }

    public boolean isType(final Type t) {
        return type == t;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("fileName", fileName)
                .add("baseName", baseName)
                .add("creationTime", creationTime)
                .add("modificationTime", modificationTime)
                .add("slug", slug)
                .add("headline", headline)
                .add("markdown", markdown)
                .add("metadata", metadata)
                .add("type", type)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
            fileName,
            baseName,
            creationTime,
            modificationTime,
            slug,
            headline,
            markdown,
            metadata,
            type);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof DataFile)) {
            return false;
        }

        final DataFile other = (DataFile) obj;
        return Objects.equal(fileName, other.fileName)
            && Objects.equal(baseName, other.baseName)
            && Objects.equal(creationTime, other.creationTime)
            && Objects.equal(modificationTime, other.modificationTime)
            && Objects.equal(slug, other.slug)
            && Objects.equal(headline, other.headline)
            && Objects.equal(markdown, other.markdown)
            && Objects.equal(metadata, other.metadata)
            && Objects.equal(type, other.type);
    }

    /**
     * Factory method to create the {@link DataFile value object} from a real file.
     *
     * @param file must not be {@code null}
     * @param type must not be {@code null}
     * @return never {@code nul}
     * @throws IOException if input file can't be read
     */
    public static DataFile from(final Path file, final Type type) throws IOException {
        Validate.notNull(file, "file");
        final FileAttributes attributes = new FileAttributes(file);
        final DataProcessor processor = new DataProcessor(file.toFile());
        return new DataFile(
                file.toString(),
                file.toFile().getName(),
                attributes.creationTime(),
                attributes.lastModifiedTime(),
                slugify(file),
                processor.getHeadline(),
                processor.getMarkdown(),
                processor.getMetaData(),
                type);
    }

    /**
     * Removes date and extension from file name.
     *
     * @param file must not be {@code null}
     * @return never {@code null}
     */
    static String slugify(final Path file) {
        final String name = Validate.notNull(file, "file").toFile().getName();
        final int start = name.indexOf("_") + 1;
        final int stop = name.lastIndexOf(".");
        return name.substring(start, stop);
    }

    /**
     * Wrapper to easy access {@link BasicFileAttributes basic file attributes}.
     */
    static final class FileAttributes {
        /**
         * The files attributes.
         */
        private final BasicFileAttributes attributes;

        /**
         * Dedicated constructor.
         *
         * @param file must not be {@code null}
         * @throws IOException if attributes can't be read
         */
        FileAttributes(final Path file) throws IOException {
            super();
            attributes = Files.readAttributes(Validate.notNull(file, "file"), BasicFileAttributes.class);
        }

        long creationTime() {
            return attributes.creationTime().toMillis();
        }

        long lastModifiedTime() {
            return attributes.lastModifiedTime().toMillis();
        }
    }
    /**
     * Parses meta data and Markdown out of a file.
     */
    private static class DataProcessor {

        /**
         * Parses meta data.
         */
        private final Preprocessor metaDataParser = new Preprocessor();
        /**
         * Extracts headline.
         */
        private final Headline headliner = new Headline();
        /**
         * To read data from.
         */
        private final InputStream input;
        /**
         * Lazy computed so never access directly, but use {@link #getMetaData()}.
         */
        private MetaData metaData;
        /**
         * Lazy computed so never access directly, but use {@link #getMarkdown()}.
         */
        private String markdown;
        /**
         * Lazy computed so never access directly, but use {@link #getHeadline()}.
         */
        private String headline;

        /**
         * Convenience constructor.
         *
         * @param file must not be {@code null}
         * @throws FileNotFoundException if file not found
         */
        DataProcessor(final File file) throws FileNotFoundException {
            this(new FileInputStream(file));
            Validate.notNull(file, "file");
        }

        /**
         * Dedicated constructor.
         *
         * @param in must not be {@literal null}
         */
        DataProcessor(
                final InputStream in) {
            super();
            Validate.notNull(in, "Data file must not be empty!");
            this.input = in;
        }

        /**
         * Get the processed meta data.
         *
         * @return never {@literal null}
         * @throws IOException on any read error of data or template file
         */
        MetaData getMetaData() throws IOException {
            if (null == metaData) {
                readFileContent();
            }

            return metaData;
        }

        /**
         * Read the plain file content and parsed the meta data and Markdown,
         *
         * Initializes the fields {@link #metaData} and {@link #markdown}.
         *
         * @throws IOException if content can't be read
         */
        private void readFileContent() throws IOException {
            final String raw = IOUtils.toString(input);
            markdown = metaDataParser.process(raw);
            metaData = metaDataParser.getMetaData();
            IOUtils.closeQuietly(input);
        }

        /**
         * Get the raw Markdown content.
         *
         * @return never {@code null}
         * @throws IOException if content can't be read
         */
        String getMarkdown() throws IOException {
            if (null == markdown) {
                readFileContent();
            }

            return markdown;
        }

        /**
         * Get the processed headline.
         *
         * @return never {@literal null}
         * @throws IOException on any read error of data or template file
         */
        String getHeadline() throws IOException {
            if (null == headline) {
                getMetaData();
                headline = headliner.find(markdown);
            }

            return headline;
        }
    }

    public static enum Type {
        SITE, POST;
    }
}
