package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.validate.Validate;

/**
 * Enumerates all available configuration option names.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public enum BlogConfigurationKeys {
    /**
     * Name of blog headline property.
     */
    TITLE ( "title"),
    /**
     * Name of blog description property.
     */
    DESCRIPTION ( "description"),
    /**
     * Name of site base URI property.
     */
    SITE_URI ( "siteUrl"),
    /**
     * Name of site language property.
     */
    LANGUAGE ("language"),
    /**
     * Name of data directory property.
     */
    DATA_DIR ( "dataDirectory"),
    /**
     * Name of template directory property.
     */
    TEMPLATE_DIR ( "templateDirectory"),
    /**
     * Name of published public directory property.
     */
    PUBLIC_DIR ( "publicDirectory"),
    /**
     * Name of encoding property.
     */
    ENCODING ( "encoding");

    /**
     * The literal configuration key.
     */
    private final String key;

    /**
     * Dedicated constructor.
     *
     * @param key must not be {@code null} or empty
     */
    BlogConfigurationKeys(final String key) {
        this.key = Validate.notEmpty(key, "key");
    }

    /**
     * Get the literal key as string.
     *
     * @return never {@code null} or empty
     */
    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return getKey();
    }
}
