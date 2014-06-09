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

import com.google.gson.Gson;
import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.guava.Objects;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.Constants;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

/**
 * Holds the published pages.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class PublishedPages {

    /**
     *Holds the data.
     * <p>
     * The key is the the filename relative to the data repository.
     * </p>
     */
    private final Map<String, Page> data = Maps.newHashMap();

    /**
     * Add collection of pages.
     *
     * @param pages must not be {@code null}
     */
    public void put(final Collection<Page> pages) {
        for (final Page page : Validate.notNull(pages, "pages")) {
            put(page);
        }
    }

    /**
     * Add single pages.
     *
     * @param page must not be {@code null}
     */
    public void put(final Page page) {
        final String name = Validate.notNull(page, "page").getFile().getFilename();
        data.put(name, page);
    }

    @Override
    public String toString() {
        return String.format("PublishedPages{pages=%s}", data);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof PublishedPages)) {
            return false;
        }

        final PublishedPages other = (PublishedPages) obj;
        return Objects.equal(data, other.data);
    }

    public static void save(final Path file, final PublishedPages pages) throws IOException {
        Validate.notNull(file, "file");
        Validate.notNull(pages, "pages");

        final Gson serializer = new Gson();
        final String json = serializer.toJson(pages);
        Files.write(file, json.getBytes(Constants.DEFAULT_ENCODING.toString()));
    }

    public static PublishedPages load(final Path file) {
        return null;
    }
}
