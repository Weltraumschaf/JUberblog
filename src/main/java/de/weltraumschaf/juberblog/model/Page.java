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
    private final URI uri;
    /**
     * Description of page.
     */
    private final String description;
    /**
     * Publishing date of page.
     */
    private final DateTime publishingDate;
    /**
     * File from which the page is created.
     */
    private final DataFile file;
    /**
     * Change frequency of page.
     */
    private final SiteMapUrl.ChangeFrequency frequencey;
    /**
     * Priotiry of page.
     */
    private final SiteMapUrl.Priority priority;

    /**
     * Dedicated constructor.
     *
     * @param title must not be {@literal null}
     * @param link must not be {@literal null}
     * @param description must not be {@literal null}
     * @param publishingDate must not be {@literal null}
     * @param file must not be {@literal null}
     * @param frequencey must not be {@literal null}
     * @param priority must not be {@literal null}
     */
    public Page(
        final String title,
        final URI link,
        final String description,
        final DateTime publishingDate,
        final DataFile file,
        final SiteMapUrl.ChangeFrequency frequencey,
        final SiteMapUrl.Priority priority) {
        super();
        Validate.notNull(title, "Title must not be null!");
        Validate.notNull(link, "Link must not be null!");
        Validate.notNull(description, "Description must not be null!");
        Validate.notNull(publishingDate, "PublishingDate must not be null!");
        Validate.notNull(file, "File must not be null!");
        Validate.notNull(frequencey, "Frequencey must not be null!");
        Validate.notNull(priority, "Priority must not be null!");
        this.title = title;
        this.uri = link;
        this.description = description;
        this.publishingDate = publishingDate;
        this.file = file;
        this.frequencey = frequencey;
        this.priority = priority;
    }

    /**
     * Get the title.
     *
     * @return never {@literal null}
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the URI.
     *
     * @return never {@literal null}
     */
    public URI getUri() {
        return uri;
    }

    /**
     * Get the description.
     *
     * @return never {@literal null}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the publishing date.
     *
     * @return never {@literal null}
     */
    public DateTime getPublishingDate() {
        return publishingDate;
    }

    /**
     * Get the change frequency.
     *
     * @return never {@literal null}
     */
    public SiteMapUrl.ChangeFrequency getFrequencey() {
        return frequencey;
    }

    /**
     * Get the priority.
     *
     * @return [0.0 - 1.0]
     */
    public SiteMapUrl.Priority getPriority() {
        return priority;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title, uri, description, publishingDate, priority, frequencey, file);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Page)) {
            return false;
        }

        final Page other = (Page) obj;
        return Objects.equal(title, other.title)
                && Objects.equal(uri, other.uri)
                && Objects.equal(description, other.description)
                && Objects.equal(publishingDate, other.publishingDate)
                && Objects.equal(priority, other.priority)
                && Objects.equal(frequencey, other.frequencey)
                && Objects.equal(file, other.file);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("title", title)
            .add("uri", uri)
            .add("description", description)
            .add("publishingDate", publishingDate)
            .add("priority", priority)
            .add("frequencey", frequencey)
            .add("file", file)
            .toString();
    }

}
