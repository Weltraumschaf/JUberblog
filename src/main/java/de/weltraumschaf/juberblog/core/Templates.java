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
import java.nio.file.Path;
import java.util.Objects;

/**
 * Contains paths to all necessary templates.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Templates {

    /**
     * Outer layout template.
     */
    private final Path layoutTemplate;
    /**
     * Inner template for posts.
     */
    private final Path postTemplate;
    /**
     * Inner template for sites.
     */
    private final Path siteTemplate;
    /**
     * Template for feed XML.
     */
    private final Path feedTemplate;
    /**
     * Inner template for index.
     */
    private final Path indexTemplate;
    /**
     * Template for site map XML.
     */
    private final Path siteMapTemplate;

    /**
     * Dedicated constructor.
     *
     * @param layoutTemplate must not be {@code null}
     * @param postTemplate must not be {@code null}
     * @param siteTemplate must not be {@code null}
     * @param feedTemplate must not be {@code null}
     * @param indexTemplate must not be {@code null}
     * @param siteMapTemplate must not be {@code null}
     */
    public Templates(
            final Path layoutTemplate,
            final Path postTemplate,
            final Path siteTemplate,
            final Path feedTemplate,
            final Path indexTemplate,
            final Path siteMapTemplate) {
        super();
        this.layoutTemplate = Validate.notNull(layoutTemplate, "layoutTemplate");
        this.postTemplate = Validate.notNull(postTemplate, "postTemplate");
        this.siteTemplate = Validate.notNull(siteTemplate, "siteTemplate");
        this.feedTemplate = Validate.notNull(feedTemplate, "feedTemplate");
        this.indexTemplate = Validate.notNull(indexTemplate, "indexTemplate");
        this.siteMapTemplate = Validate.notNull(siteMapTemplate, "siteMapTemplate");
    }

    /**
     * Get the outer layout template.
     *
     * @return never {@code null}
     */
    public Path getLayoutTemplate() {
        return layoutTemplate;
    }

    /**
     * Get the inner post template.
     *
     * @return never {@code null}
     */
    public Path getPostTemplate() {
        return postTemplate;
    }

    /**
     * Get the inner site template.
     *
     * @return never {@code null}
     */
    public Path getSiteTemplate() {
        return siteTemplate;
    }

    /**
     * Get the feed XML template.
     *
     * @return never {@code null}
     */
    public Path getFeedTemplate() {
        return feedTemplate;
    }

    /**
     * Get the inner index template.
     *
     * @return never {@code null}
     */
    public Path getIndexTemplate() {
        return indexTemplate;
    }

    /**
     * Get the site map XML template.
     *
     * @return never {@code null}
     */
    public Path getSiteMapTemplate() {
        return siteMapTemplate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                layoutTemplate.toString(),
                postTemplate.toString(),
                siteTemplate.toString(),
                feedTemplate.toString(),
                indexTemplate.toString(),
                siteMapTemplate.toString()
        );
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Templates)) {
            return false;
        }

        final Templates other = (Templates) obj;
        return Objects.equals(layoutTemplate.toString(), other.layoutTemplate.toString())
                && Objects.equals(postTemplate.toString(), other.postTemplate.toString())
                && Objects.equals(siteTemplate.toString(), other.siteTemplate.toString())
                && Objects.equals(feedTemplate.toString(), other.feedTemplate.toString())
                && Objects.equals(indexTemplate.toString(), other.indexTemplate.toString())
                && Objects.equals(siteMapTemplate.toString(), other.siteMapTemplate.toString());
    }

    @Override
    public String toString() {
        return "Templates{"
                + "layoutTemplate=" + layoutTemplate.toString() + ", "
                + "postTemplate=" + postTemplate.toString() + ", "
                + "siteTemplate=" + siteTemplate.toString() + ", "
                + "feedTemplate=" + feedTemplate.toString() + ", "
                + "indexTemplate=" + indexTemplate.toString() + ", "
                + "siteMapTemplate=" + siteMapTemplate.toString()
                + '}';
    }

}
