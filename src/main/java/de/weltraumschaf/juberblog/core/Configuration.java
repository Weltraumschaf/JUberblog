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
package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.validate.Validate;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

/**
 * Blog configuration.
 *
 * Abstracts the configuration properties file.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Configuration {

    /**
     * Name of blog headline property.
     */
    static final String TITLE = "title";
    /**
     * Name of blog description property.
     */
    static final String DESCRIPTION = "description";
    /**
     * Name of site base URI property.
     */
    static final String SITE_URI = "siteUrl";
    /**
     * Name of site language property.
     */
    static final String LANGUAGE = "language";
    /**
     * Name of data directory property.
     *
     * TODO: Rename to dataDirectory.
     */
    static final String DATA_DIR = "dataDir";
    /**
     * Name of template directory property.
     *
     * TODO: Rename to temlateDirectory.
     */
    static final String TEMPLATE_DIR = "tplDir";
    /**
     * Name of published htdocs directory property.
     *
     * TODO: Rename to publicDirectory.
     */
    static final String HTDOCS = "htdocs";
    /**
     * Name of encoding property.
     */
    static final String ENCODING = "encoding";
    /**
     * Used to load and parse file.
     */
    private final Properties properties;

    /**
     * Convenience constructor to inject properties.
     *
     * @param filename must not be {@literal null} or empty
     * @throws IOException if file can't be load
     */
    public Configuration(final String filename) throws IOException {
        this(load(filename));
    }

    /**
     *
     * Dedicated constructor.
     *
     * @param p must not be {@code null}
     */
    public Configuration(final Properties p) {
        super();
        properties = validate(Validate.notNull(p));
    }

    /**
     * Load the properties from file.
     *
     * @param filename must not be {@literal null} or empty
     * @return never {@code null}
     * @throws IOException if configuration file can not be loaded
     */
    static Properties load(final String filename) throws IOException {
        Validate.notNull(filename, "filename");
        final Properties properties = new Properties();

        try (final InputStream in = new FileInputStream(new File(filename))) {
            properties.load(in);
            return properties;
        }
    }

    /**
     * Validates that all important properties are set and not empty.
     * <p>
     * Throws an {@link IllegalArgumentException} if a property is {@code null} or empty.
     * </p>
     *
     * @param input must not be {@code null}
     * @return same as input
     */
    static Properties validate(final Properties input) {
        for (final String propertyName : Arrays.asList(
                DATA_DIR,
                DESCRIPTION,
                ENCODING,
                HTDOCS,
                LANGUAGE,
                SITE_URI,
                TEMPLATE_DIR,
                TITLE
        )) {
            try {
                Validate.notEmpty(input.getProperty(propertyName));
            } catch (final IllegalArgumentException | NullPointerException ex) {
                throw new IllegalArgumentException(String.format(
                        "The configuration property '%s' must not be empty or missing!",
                        propertyName));
            }
        }

        return input;
    }

    /**
     * Get property with empty string as default.
     *
     * @param name must not be {code null} or empty
     * @return never {@literal null}
     */
    private String getProperty(final String name) {
        Validate.notEmpty(name, "Name must not be null or empty!");
        return properties.getProperty(name, "");
    }

    /**
     * Get the site main headline.
     *
     * @return never {@literal null}
     */
    public String getTitle() {
        return getProperty(TITLE);
    }

    /**
     * Get the site main description.
     *
     * @return never {@literal null}
     */
    public String getDescription() {
        return getProperty(DESCRIPTION);
    }

    /**
     * Get the site base URI.
     *
     * @return never {@literal null}
     */
    public URI getBaseUri() {
        return URI.create(getProperty(SITE_URI));
    }

    /**
     * Get the site language.
     *
     * @return never {@literal null}
     */
    public String getLanguage() {
        return getProperty(LANGUAGE);
    }

    /**
     * Get the data directory.
     *
     * @return never {@literal null}
     */
    public String getDataDir() {
        return getProperty(DATA_DIR);
    }

    /**
     * Get the template directory.
     *
     * @return never {@literal null}
     */
    public String getTemplateDir() {
        return getProperty(TEMPLATE_DIR);
    }

    /**
     * Get the htdocs directory.
     *
     * @return never {@literal null}
     */
    public String getHtdocs() {
        return getProperty(HTDOCS);
    }

    /**
     * Get the encoding.
     *
     * @return never {@literal null}
     */
    public String getEncoding() {
        return getProperty(ENCODING);
    }

    @Override
    public String toString() {
        return "Configuration{" + "properties=" + properties + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Configuration)) {
            return false;
        }

        final Configuration other = (Configuration) obj;
        return Objects.equals(properties, other.properties);
    }

}
