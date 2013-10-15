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
    private final Properties properties = new Properties();
    /**
     * Absolute path to property file.
     */
    private final String fileName;

    /**
     * Dedicated constructor.
     *
     * @param fileName must not be {@code null} or empty
     */
    public BlogConfiguration(final String fileName) {
        super();
        Validate.notEmpty(fileName, "File name must not be null or empty!");
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
     * @return never {@code null}
     */
    private String getProperty(final String name) {
        Validate.notEmpty(name, "Name must not be null or empty!");
        return properties.getProperty(name, "");
    }

    /**
     * Get the site main headline.
     *
     * @return never {@code null}
     */
    public String getHeadline() {
        return getProperty(HEADLINE);
    }

    /**
     * Get the site main description.
     *
     * @return never {@code null}
     */
    public String getDescription() {
        return getProperty(DESCRIPTION);
    }

    /**
     * Get the site base URI.
     *
     * @return never {@code null}
     */
    public String getSiteUri() {
        return getProperty(SITE_URI);
    }

    /**
     * Get the site language.
     *
     * @return never {@code null}
     */
    public String getLanguage() {
        return getProperty(LANGUAGE);
    }

    /**
     * Get the data directory.
     *
     * @return never {@code null}
     */
    public String getDataDir() {
        return getProperty(DATA_DIR);
    }

    /**
     * Get the template directory.
     *
     * @return never {@code null}
     */
    public String getTemplateDir() {
        return getProperty(TEMPLATE_DIR);
    }

    /**
     * Get the htdocs directory.
     *
     * @return never {@code null}
     */
    public String getHtdocs() {
        return getProperty(HTDOCS);
    }

    public String getTwitterConsumerKey() {
        return getProperty(TWITTER_CONSUMER_KEY);
    }

    public String getTwitterConsumerSecret() {
        return getProperty(TWITTER_CONSUMER_SECRET);
    }

    public String getTwitterOAuthToken() {
        return getProperty(TWITTER_OAUTH_TOKEN);
    }

    public String getTwitterOAuthTokenSecret() {
        return getProperty(TWITTER_OAUTH_TOKEN_SECRET);
    }

    public String getBitlyUsername() {
        return getProperty(BITLY_USERNAME);
    }

    public String getBitlyApikey() {
        return getProperty(BITLY_APIKEY);
    }

    public String getApiUri() {
        return getProperty(API_URI);
    }

    public boolean getFeatureRating() {
        return "true".equalsIgnoreCase(getProperty(FEATURE_RATING)) ? true : false;
    }

    public boolean getFeatureComments() {
        return "true".equalsIgnoreCase(getProperty(FEATURE_COMMENTS)) ? true : false;
    }

}
