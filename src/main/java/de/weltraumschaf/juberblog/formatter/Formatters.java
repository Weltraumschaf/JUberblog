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
package de.weltraumschaf.juberblog.formatter;

import de.weltraumschaf.juberblog.model.Feed;
import de.weltraumschaf.juberblog.model.Post;
import de.weltraumschaf.juberblog.model.SiteMap;
import freemarker.template.Configuration;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Factory to create formatters.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Formatters {

    /**
     * Hidden for pure static factory.
     */
    private Formatters() {
        super();
    }

    /**
     * Create a formatter for {@link Type#SITE type site}.
     *
     * @param templateConfiguration must not be {@code null}
     * @param markdown must not be {@code null}
     * @return never {@code null}
     * @throws IOException on any template or markdown IO error
     */
    public static HtmlFormatter createSiteFormatter(
        final Configuration templateConfiguration,
        final String markdown,
        final Path templateDir) throws IOException {
        return new SiteFormatter(templateConfiguration, markdown, templateDir);
    }

    /**
     * Create a formatter for {@link Type#POST type post}.
     *
     * @param templateConfiguration must not be {@code null}
     * @param markdown must not be {@code null}
     * @return never {@code null}
     * @throws IOException on any template or markdown IO error
     */
    public static HtmlFormatter createPostFormatter(
        final Configuration templateConfiguration,
        final String markdown,
        final Path templateDir) throws IOException {
        return new PostFormatter(templateConfiguration, markdown, templateDir);
    }

    /**
     * Create a formatter for {@link Type#FEED type site}.
     *
     * @param templateConfiguration must not be {@code null}
     * @param feed must not be {@code null}
     * @return never {@code null}
     * @throws IOException on any template or
     */
    public static Formatter createFeedFormatter(
        final Configuration templateConfiguration,
        final Feed feed,
        final Path dirs) throws IOException {
        return new FeedFormatter(templateConfiguration, feed, dirs);
    }

    /**
     * Create a formatter for {@link Type#SITE_MAP type site}.
     *
     * @param templateConfiguration must not be {@code null}
     * @param siteMap must not be {@code null}
     * @return never {@code null}
     * @throws IOException on any template or markdown IO error
     */
    public static Formatter createSiteMapFormatter(
        final Configuration templateConfiguration,
        final SiteMap siteMap,
        final Path templateDir) throws IOException {
        return new SiteMapFormatter(templateConfiguration, siteMap, templateDir);
    }

    /**
     * Create a formatter for the home site.
     *
     * @param templateConfiguration must not be {@code null}
     * @param pages must not be {@code null}
     * @return never {@code null}
     * @throws IOException on any template or markdown IO error
     */
    public static Formatter createHomeSiteFormatter(
        final Configuration templateConfiguration, final List<Post> pages, final String headline, final String description, final String version, final Path templateDir) throws IOException {
        return new HomeSiteFormatter(templateConfiguration, pages, headline, description, version, templateDir);
    }

    /**
     * Type of formatters.
     *
     * XXX Necessary?
     */
    public enum Type {
        /** For blog posts. */
        POST,
        /** For static blog sites (about me or such). */
        SITE,
        /** For the RSS feed. */
        FEED,
        /** For the site map XML. */
        SITE_MAP;
    }
}
