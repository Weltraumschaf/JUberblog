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

import com.google.common.base.Objects;
import java.net.URI;
import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;

/**
 * Immutable model to represent a HTML page.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Page {
    /**
     * Title of page.
     */
    private final String title;
    /**
     * URI of page.
     */
    private final URI link;
    /**
     * Description of page.
     */
    private final String description;
    /**
     * Publishing date of page.
     */
    private final DateTime publishingDate;
    private final boolean newPublished;

    /**
     * Dedicated constructor.
     *
     * @param title must not be {@code null}
     * @param link must not be {@code null}
     * @param description must not be {@code null}
     * @param publishingDate must not be {@code null}
     */
    private Page(final String title, final URI link, final String description, final DateTime publishingDate, final boolean newPublished) {
        super();
        Validate.notNull(title, "Title must not be null!");
        Validate.notNull(link, "Link must not be null!");
        Validate.notNull(description, "Description must not be null!");
        Validate.notNull(publishingDate, "PublishingDate must not be null!");
        this.title = title;
        this.link = link;
        this.description = description;
        this.publishingDate = publishingDate;
        this.newPublished = newPublished;
    }

    public static Page newPublishedPage(final String title, final URI link, final String description, final DateTime publishingDate) {
        return new Page(title, link, description, publishingDate, true);
    }

    public static Page newExistingPage(final String title, final URI link, final String description, final DateTime publishingDate) {
        return new Page(title, link, description, publishingDate, false);
    }

    public boolean isNewPublished() {
        return newPublished;
    }

    /**
     * Get the title.
     *
     * @return never {@code null}
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the URI.
     *
     * @return never {@code null}
     */
    public URI getLink() {
        return link;
    }

    /**
     * Get the description.
     *
     * @return never {@code null}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the publishing date.
     *
     * @return never {@code null}
     */
    public DateTime getPublishingDate() {
        return publishingDate;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title, link, description, publishingDate, newPublished);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Page)) {
            return false;
        }

        final Page other = (Page) obj;
        return Objects.equal(title, other.title)
                && Objects.equal(link, other.link)
                && Objects.equal(description, other.description)
                && Objects.equal(publishingDate, other.publishingDate)
                && Objects.equal(newPublished, other.newPublished);
    }

}
