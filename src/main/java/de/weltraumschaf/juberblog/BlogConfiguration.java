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
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class BlogConfiguration {

    private static final String HEADLINE = "headline";
    private static final String DESCRIPTION = "description";
    private static final String SITE_URI = "siteUrl";
    private static final String LANGUAGE = "language";
    private static final String DATA_DIR = "dataDir";
    private static final String TEMPLATE_DIR = "tplDir";
    private static final String HTDOCS = "htdocs";
    private static final String TWITTER_CONSUMER_KEY = "twitter.consumer_key";
    private static final String TWITTER_CONSUMER_SECRET = "twitter.consumer_secret";
    private static final String TWITTER_OAUTH_TOKEN = "twitter.oauth_token";
    private static final String TWITTER_OAUTH_TOKEN_SECRET = "twitter.oauth_token_secret";
    private static final String BITLY_USERNAME = "bitly.username";
    private static final String BITLY_APIKEY = "bitly.apikey";
    private static final String API_URI = "api.url";
    private static final String FEATURE_RATING = "features.rating";
    private static final String FEATURE_COMMENTS = "features.comments";
    private final Properties properties = new Properties();
    private final String fileName;

    public BlogConfiguration(final String fileName) {
        super();
        Validate.notEmpty(fileName, "File name must not be null or empty!");
        this.fileName = fileName;
    }

    public void load() throws IOException {
        InputStream in = null;

        try {
            in = new FileInputStream(new File(fileName));
            properties.load(in);
            IOUtils.closeQuietly(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    private String getProperty(final String name) {
        return properties.getProperty(name, "");
    }

    public String getHeadline() {
        return getProperty(HEADLINE);
    }

    public String getDescription() {
        return getProperty(DESCRIPTION);
    }

    public String getSiteUri() {
        return getProperty(SITE_URI);
    }

    public String getLanguage() {
        return getProperty(LANGUAGE);
    }

    public String getDataDir() {
        return getProperty(DATA_DIR);
    }

    public String getTemplateDir() {
        return getProperty(TEMPLATE_DIR);
    }

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
