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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.guava.Objects;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.Constants;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.joda.time.DateTime;

/**
 * Holds the published pages.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class PublishedPages {

    /**
     * Factory to create JSON serializer with registered type adapter.
     */
    private static  final GsonBuilder GSON = new GsonBuilder();
    static {
        GSON.registerTypeAdapter(DateTime.class, new DateTimeSerializer());
        GSON.registerTypeAdapter(DateTime.class, new DateTimeDeserializer());
    }

    /**
     * Holds the data.
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

    /**
     * Count of pages.
     *
     * @return not negative
     */
    public int size() {
        return data.size();
    }

    /**
     * Tells if there are no pagesat all.
     *
     * @return {@code true} if {@link #size()} {@code == 0}, else {@code false}
     */
    public boolean isEmpty() {
        return data.isEmpty();
    }

    /**
     * Returns a page by its key.
     * <p>
     * Throws an {@link IllegalArgumentException} if a non existing key is requested.
     * </p>
     *
     * @param key must not be {@code nul} or empty
     * @return never {@code null}
     */
    public Page get(final String key) {
        if (data.containsKey(key)) {
        return data.get(Validate.notEmpty(key, "key"));
        }

        throw new IllegalArgumentException(String.format("There is no page for key '%s'!", key));
    }

    /**
     * Get a set of keys for which pages are stored.
     *
     * @return never {@code null}, unmodifiable
     */
    public Set<String> keySet() {
        return Collections.unmodifiableSet(data.keySet());
    }

    /**
     * Get a collection of the stored pages.
     *
     * @return never {@code null}, unmodifiable
     */
    public Collection<Page> values() {
        return Collections.unmodifiableCollection(data.values());
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

    /**
     * Saves the whole object serialized as JSON to a file.
     *
     * @param file must not be {@code null}
     * @param pages must not be {@code null}
     * @throws IOException if file can't be saved
     */
    public static void save(final Path file, final PublishedPages pages) throws IOException {
        Validate.notNull(file, "file");
        Validate.notNull(pages, "pages");

        final Gson serializer = GSON.create();
        final String json = serializer.toJson(pages);
        Files.write(file, json.getBytes(Constants.DEFAULT_ENCODING.toString()));
    }

    /**
     * Loads the whole object from JSON file.
     *
     * @param file must not be {@code null}
     * @return never {@code null}, always new instance
     * @throws IOException if file can't be read
     */
    public static PublishedPages load(final Path file) throws IOException {
        Validate.notNull(file, "file");

        final byte[] content = Files.readAllBytes(file);
        final Gson serializer = GSON.create();
        return serializer.fromJson(new String(content, Constants.DEFAULT_ENCODING.toString()), PublishedPages.class);
    }

    /**
     * Custom serializer for Joda Time.
     */
    private static final class DateTimeSerializer implements JsonSerializer<DateTime> {

        @Override
        public JsonElement serialize(
            final DateTime src,
            final Type typeOfSrc,
            final JsonSerializationContext context)
        {
            return new JsonPrimitive(src.toString());
        }
    }

    /**
     * Custom deserializer for Joda Time.
     */
    private static final class DateTimeDeserializer implements JsonDeserializer<DateTime> {

        @Override
        public DateTime deserialize(
            final JsonElement json,
            final Type typeOfT,
            final JsonDeserializationContext context) throws JsonParseException
        {
            return new DateTime(json.getAsJsonPrimitive().getAsString());
        }
    }

}
