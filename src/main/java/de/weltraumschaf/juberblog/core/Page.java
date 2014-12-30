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
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

/**
 * Describes a published page.
 * <p>
 * This class is immutable by design.
 * </p>
 *
 * XXX: Move into publish package.
 * 
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Page {

    /**
     * The page title.
     */
    private final String title;
    /**
     * The page URL.
     */
    private final URI link;
    /**
     * The page description.
     */
    private final String description;
    /**
     * When the page was published.
     */
    private final DateTime publishingDate;
    /**
     * Type of page.
     */
    private final Type type;

    /**
     * Dedicated constructor.
     *
     * @param title must not be {@code null} or empty
     * @param link title must not be {@code null}
     * @param description title must not be {@code null}
     * @param publishingDate title must not be {@code null}
     * @param type title must not be {@code null}
     */
    public Page(
            final String title,
            final URI link,
            final String description,
            final DateTime publishingDate,
            final Type type) {
        super();
        this.title = Validate.notEmpty(title, "title");
        this.link = Validate.notNull(link, "link");
        this.description = Validate.notNull(description, "description");
        this.publishingDate = Validate.notNull(publishingDate, "publishingDate");
        this.type = Validate.notNull(type, "type");
    }

    /**
     * Get the page tile.
     *
     * @return never {@code null} or empty
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the page URL.
     *
     * @return never {@code null}
     */
    public URI getLink() {
        return link;
    }

    /**
     * Get the page description.
     *
     * @return never {@code null}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the page's publishing date.
     *
     * @return never {@code null}
     */
    public DateTime getPublishingDate() {
        return publishingDate;
    }

    /**
     * Get the page type.
     *
     * @return never {@code null}
     */
    public Type getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, link, description, publishingDate, type);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Page)) {
            return false;
        }

        final Page other = (Page) obj;
        return Objects.equals(title, other.title)
                && Objects.equals(link, other.link)
                && Objects.equals(description, other.description)
                && Objects.equals(publishingDate, other.publishingDate)
                && Objects.equals(type, other.type);
    }

    @Override
    public String toString() {
        return "Page{"
                + "title=" + title + ", "
                + "link=" + link + ", "
                + "description=" + description + ", "
                + "publishingDate=" + publishingDate + ", "
                + "type=" + type
                + '}';
    }

    /**
     * Convenience type to collect pages.
     */
    public static final class Pages extends ArrayList<Page> {

        /**
         * Default constructor.
         */
        public Pages() {
            super();
        }

    }

    /**
     * Type of pages.
     */
    public enum Type {

        /**
         * A blog post.
         */
        POST,
        /**
         * A regular site.
         */
        SITE;
    }

    /**
     * A comparator to sort pages ascending by date.
     */
    public static final class SortByDateAscending implements Comparator<Page> {

        /**
         * Delegate for Joda time objects.
         */
        private final Comparator<Object> jodaCompare = DateTimeComparator.getInstance();

        @Override
        public int compare(final Page o1, final Page o2) {
            return jodaCompare.compare(o1.getPublishingDate(), o2.getPublishingDate());
        }

    }
}
