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
package de.weltraumschaf.juberblog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

/**
 * Blog configuration.
 *
 * Abstracts the configuration properties file.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class BlogConfiguration {

    /**
     * Name of blog headline  property.
     */
    private static final String HEADLINE = "headline";
    /**
     * Name of blog description property.
     */
    private static final String DESCRIPTION = "description";
    /**
     * Name of site base URI property.
     */
    private static final String SITE_URI = "siteUrl";
    /**
     * Name of site language property.
     */
    private static final String LANGUAGE = "language";
    /**
     * Name of data directory property.
     */
    private static final String DATA_DIR = "dataDir";
    /**
     * Name of template directory property.
     */
    private static final String TEMPLATE_DIR = "tplDir";
    /**
     * Name of published htdocs directory property.
     */
    private static final String HTDOCS = "htdocs";
    /**
     * Name of Twitter consumer key property.
     */
    private static final String TWITTER_CONSUMER_KEY = "twitter.consumer_key";
    /**
     * Name of Twitter consumer secret property.
     */
    private static final String TWITTER_CONSUMER_SECRET = "twitter.consumer_secret";
    /**
     * Name of Twitter OAuth token property.
     */
    private static final String TWITTER_OAUTH_TOKEN = "twitter.oauth_token";
    /**
     * Name of Twitter OAuth secret property.
     */
    private static final String TWITTER_OAUTH_TOKEN_SECRET = "twitter.oauth_token_secret";
    /**
     * Name of Bitly username property.
     */
    private static final String BITLY_USERNAME = "bitly.username";
    /**
     * Name of Bitly API key property.
     */
    private static final String BITLY_APIKEY = "bitly.apikey";
    /**
     * Name of blog API URI property.
     */
    private static final String API_URI = "api.url";
    /**
     * Name of rating feature switch property.
     */
    private static final String FEATURE_RATING = "features.rating";
    /**
     * Name of comments feature switch property.
     */
    private static final String FEATURE_COMMENTS = "features.comments";
    /**
     * Used to load and parse file.
     */
    private Properties properties = new Properties();
    /**
     * Absolute path to property file.
     */
    private final String fileName;

    public BlogConfiguration(final Properties p) {
        this("");
        Validate.notNull(p);
        properties = p;
    }
    /**
     * Dedicated constructor.
     *
     * @param fileName must not be {@literal null} or empty
     */
    public BlogConfiguration(final String fileName) {
        super();
        Validate.notNull(fileName, "File name must not be null!");
        this.fileName = fileName;
    }

    /**
     * Load the properties from file.
     *
     * @throws IOException if configuration file can not be loaded
     */
    public void load() throws IOException {
        InputStream in = null;

        try {
            in = new FileInputStream(new File(fileName));
            properties.load(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
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
    public String getHeadline() {
        return getProperty(HEADLINE);
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
    public String getBaseUri() {
        return getProperty(SITE_URI);
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
     * Get the Twitter consumer key.
     *
     * @return never {@literal null}
     */
    public String getTwitterConsumerKey() {
        return getProperty(TWITTER_CONSUMER_KEY);
    }

    /**
     * Get the Twitter consumer secret.
     *
     * @return never {@literal null}
     */
    public String getTwitterConsumerSecret() {
        return getProperty(TWITTER_CONSUMER_SECRET);
    }

    /**
     * Get the Twitter OAuth token.
     *
     * @return never {@literal null}
     */
    public String getTwitterOAuthToken() {
        return getProperty(TWITTER_OAUTH_TOKEN);
    }

    /**
     * Get the Twitter OAuth secret.
     *
     * @return never {@literal null}
     */
    public String getTwitterOAuthTokenSecret() {
        return getProperty(TWITTER_OAUTH_TOKEN_SECRET);
    }

    /**
     * Get the Bitly username.
     *
     * @return never {@literal null}
     */
    public String getBitlyUsername() {
        return getProperty(BITLY_USERNAME);
    }

    /**
     * Get the Bitly API key.
     *
     * @return never {@literal null}
     */
    public String getBitlyApikey() {
        return getProperty(BITLY_APIKEY);
    }

    /**
     * Get the blog API URI.
     *
     * @return never {@literal null}
     */
    public String getApiUri() {
        return getProperty(API_URI);
    }

    /**
     * Get the if rating feature is enabled.
     *
     * @return never {@literal null}
     */
    public boolean getFeatureRating() {
        return "true".equalsIgnoreCase(getProperty(FEATURE_RATING)) ? true : false;
    }

    /**
     * Get the if comment feature is enabled.
     *
     * @return never {@literal null}
     */
    public boolean getFeatureComments() {
        return "true".equalsIgnoreCase(getProperty(FEATURE_COMMENTS)) ? true : false;
    }

}
