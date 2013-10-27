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
package de.weltraumschaf.juberblog.files;

import com.google.common.base.Objects;
import java.io.File;
import org.apache.commons.lang3.Validate;

/**
 * Abstracts a published file.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PublishedFile {

    /**
     * Base URI to decorate the file base name.
     */
    private final String baseUri;
    /**
     * Originating file.
     */
    private final File file;

    /**
     * Dedicated constructor.
     *
     * @param baseUri must not be {@code null}
     * @param file must not be {@code null}
     */
    public PublishedFile(final String baseUri, final File file) {
        super();
        Validate.notNull(baseUri, "BaseUri must not be null!");
        Validate.notNull(file, "File must not be null!");
        this.baseUri = baseUri;
        this.file = file;
    }

    /**
     * Get combination of {@link #baseUri} and file name.
     *
     * @return never {@code null}
     */
    public String getUri() {
        return baseUri + file.getName();
    }

    /**
     * @see java.io.File.lastModified():long
     * @return A <code>long</code> value representing the time the file was
     *          last modified, measured in milliseconds since the epoch
     *          (00:00:00 GMT, January 1, 1970), or <code>0L</code> if the
     *          file does not exist or if an I/O error occurs
     */
    public long getModificationTime() {
        return file.lastModified();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(baseUri, file.getAbsolutePath());
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof PublishedFile)) {
            return false;
        }

        final PublishedFile other = (PublishedFile) obj;
        return Objects.equal(baseUri, other.baseUri)
            && Objects.equal(file.getAbsolutePath(), other.file.getAbsolutePath());
    }


}
