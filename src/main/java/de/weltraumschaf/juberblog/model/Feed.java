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

import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.guava.Objects;
import de.weltraumschaf.commons.validate.Validate;
import java.util.List;

/**
 * Models a RSS feed.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Feed {

    /**
     * Feed title.
     */
    private final String title;
    /**
     * Feed URI.
     */
    private final String link;
    /**
     * Feed description.
     */
    private final String description;
    /**
     * Feed language.
     */
    private final String language;
    /**
     * Feed generation.
     */
    private final String lastBuildDate;
    /**
     * Feed items.
     */
    private final List<FeedItem> items = Lists.newArrayList();

    /**
     * Dedicated constructor.
     *
     * @param title must not be {@literal null} or empty
     * @param link must not be {@literal null} or empty
     * @param description must not be {@literal null} or empty
     * @param language must not be {@literal null} or empty
     * @param lastBuildDate must not be {@literal null} or empty
     */
    public Feed(String title, String link, String description, String language, String lastBuildDate) {
        super();
        Validate.notEmpty(title, "Title must not be null or empty!");
        Validate.notEmpty(link, "Link must not be null or empty!");
        Validate.notEmpty(description, "Description must not be null or empty!");
        Validate.notEmpty(language, "Language must not be null or empty!");
        Validate.notEmpty(lastBuildDate, "LastBuildDate must not be null or empty!");
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.lastBuildDate = lastBuildDate;
    }

    /**
     * Get the feed title.
     *
     * @return Never {@literal null} or empty
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the feed URI.
     *
     * @return Never {@literal null} or empty
     */
    public String getLink() {
        return link;
    }

    /**
     * Get the feed description.
     *
     * @return Never {@literal null} or empty
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the feed language.
     *
     * @return Never {@literal null} or empty
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Get the last build date.
     *
     * @return Never {@literal null} or empty
     */
    public String getLastBuildDate() {
        return lastBuildDate;
    }

    /**
     * Get the feed items.
     *
     * @return never {@literal null}
     */
    public List<FeedItem> getItems() {
        return items;
    }

    /**
     * Add feed item.
     *
     * @param item must not be {@code null}
     * @return never {@code null}
     */
    public boolean add(final FeedItem item) {
        return items.add(item);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title, link, description, language, lastBuildDate, items);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Feed)) {
            return false;
        }

        final Feed other = (Feed) obj;
        return Objects.equal(title, other.title)
                && Objects.equal(link, other.link)
                && Objects.equal(description, other.description)
                && Objects.equal(language, other.language)
                && Objects.equal(lastBuildDate, other.lastBuildDate)
                && Objects.equal(items, other.items);
    }

}
