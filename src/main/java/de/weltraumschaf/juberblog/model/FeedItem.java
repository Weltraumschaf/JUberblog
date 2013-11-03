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

import de.weltraumschaf.commons.guava.Objects;
import org.apache.commons.lang3.Validate;

/**
 * Models a RSS feed item.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class FeedItem {

    /**
     * Title of blog post.
     */
    private final String title;
    /**
     * URI of blog post.
     */
    private final String link;
    /**
     * Description of blog post.
     */
    private final String description;
    /**
     * Publishing date of blog post.
     */
    private final String pubDate;
    /**
     * Publishing date of blog post.
     */
    private final String dcDate;

    /**
     * Dedicated constructor.
     *
     * @param title must not be {@literal null} or empty
     * @param link must not be {@literal null} or empty
     * @param description must not be {@literal null} or empty
     * @param pubDate must not be {@literal null} or empty
     * @param dcDate must not be {@literal null} or empty
     */
    public FeedItem(String title, String link, String description, String pubDate, String dcDate) {
        super();
        Validate.notEmpty(title, "Title must not be null or empty!");
        Validate.notEmpty(link, "Link must not be null or empty!");
        Validate.notEmpty(description, "Description must not be null or empty!");
        Validate.notEmpty(pubDate, "PubDate must not be null or empty!");
        Validate.notEmpty(dcDate, "DcDate must not be null or empty!");
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.dcDate = dcDate;
    }

    /**
     * Get the blog post title.
     *
     * @return Never {@literal null} or empty
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the blog post URI.
     *
     * @return Never {@literal null} or empty
     */
    public String getLink() {
        return link;
    }

    /**
     * Get the blog post description.
     *
     * @return Never {@literal null} or empty
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the blog post publishing date.
     *
     * @return Never {@literal null} or empty
     */
    public String getPubDate() {
        return pubDate;
    }

    /**
     * Get the blog post publishing date.
     *
     * @return Never {@literal null} or empty
     */
    public String getDcDate() {
        return dcDate;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title, link, description, pubDate, dcDate);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof FeedItem)) {
            return false;
        }

        final FeedItem other = (FeedItem) obj;
        return Objects.equal(title, other.title)
                && Objects.equal(link, other.link)
                && Objects.equal(description, other.description)
                && Objects.equal(pubDate, other.pubDate)
                && Objects.equal(dcDate, other.dcDate);
    }
}
