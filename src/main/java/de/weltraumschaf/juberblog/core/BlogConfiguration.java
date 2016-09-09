package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.commons.validate.Validate;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Objects;
import java.util.Properties;

/**
 * Blog configuration.
 * <p>
 * Abstracts the configuration properties file.
 * </p>
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class BlogConfiguration {
    /**
     * Default configuration.
     */
    public static final BlogConfiguration DEFAULT;
    static {
        final Properties emptyConfig = new Properties();
        emptyConfig.setProperty(BlogConfigurationKeys.TITLE.getKey(), "Blog Title");
        emptyConfig.setProperty(BlogConfigurationKeys.DESCRIPTION.getKey(), "Blog Description");
        emptyConfig.setProperty(BlogConfigurationKeys.SITE_URI.getKey(), "http://uberblog.local/");
        emptyConfig.setProperty(BlogConfigurationKeys.LANGUAGE.getKey(), "en");
        emptyConfig.setProperty(BlogConfigurationKeys.DATA_DIR.getKey(), "data");
        emptyConfig.setProperty(BlogConfigurationKeys.TEMPLATE_DIR.getKey(), "templates");
        emptyConfig.setProperty(BlogConfigurationKeys.PUBLIC_DIR.getKey(), "public");
        emptyConfig.setProperty(BlogConfigurationKeys.ENCODING.getKey(), "utf-8");
        DEFAULT = new BlogConfiguration(emptyConfig);
    }

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
    public BlogConfiguration(final String filename) throws IOException {
        this(load(filename));
    }

    /**
     *
     * Dedicated constructor.
     *
     * @param p must not be {@code null}
     */
    public BlogConfiguration(final Properties p) {
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
    private static Properties load(final String filename) throws IOException {
        Validate.notEmpty(filename, "filename");
        final Properties properties = new Properties();

        try (final InputStream in = new FileInputStream(new File(filename))) {
            properties.load(in);
        }

        return properties;
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
        for (final BlogConfigurationKeys propertyName : BlogConfigurationKeys.values()) {
            try {
                Validate.notEmpty(input.getProperty(propertyName.getKey()));
            } catch (final IllegalArgumentException | NullPointerException ex) {
                throw new IllegalArgumentException(String.format(
                        "The configuration property '%s' must not be empty or missing!",
                        propertyName.getKey()));
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
    private String getProperty(final BlogConfigurationKeys name) {
        Validate.notEmpty(name.getKey(), "Name must not be null or empty!");
        return properties.getProperty(name.getKey(), "");
    }

    /**
     * Get the site main headline.
     *
     * @return never {@literal null}
     */
    public String getTitle() {
        return getProperty(BlogConfigurationKeys.TITLE);
    }

    /**
     * Get the site main description.
     *
     * @return never {@literal null}
     */
    public String getDescription() {
        return getProperty(BlogConfigurationKeys.DESCRIPTION);
    }

    /**
     * Get the site base URI.
     *
     * @return never {@literal null}
     */
    public URI getBaseUri() {
        return URI.create(getProperty(BlogConfigurationKeys.SITE_URI));
    }

    /**
     * Get the site language.
     *
     * @return never {@literal null}
     */
    public String getLanguage() {
        return getProperty(BlogConfigurationKeys.LANGUAGE);
    }

    /**
     * Get the data directory.
     *
     * @return never {@literal null}
     */
    public String getDataDir() {
        return getProperty(BlogConfigurationKeys.DATA_DIR);
    }

    /**
     * Get the template directory.
     *
     * @return never {@literal null}
     */
    public String getTemplateDir() {
        return getProperty(BlogConfigurationKeys.TEMPLATE_DIR);
    }

    /**
     * Get the htdocs directory.
     *
     * @return never {@literal null}
     */
    public String getHtdocs() {
        return getProperty(BlogConfigurationKeys.PUBLIC_DIR);
    }

    /**
     * Get the encoding.
     *
     * @return never {@literal null}
     */
    public String getEncoding() {
        return getProperty(BlogConfigurationKeys.ENCODING);
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
        if (!(obj instanceof BlogConfiguration)) {
            return false;
        }

        final BlogConfiguration other = (BlogConfiguration) obj;
        return Objects.equals(properties, other.properties);
    }

}
