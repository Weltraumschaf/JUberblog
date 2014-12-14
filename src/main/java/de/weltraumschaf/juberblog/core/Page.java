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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Page {

    private final String title;
    private final String link; // TODO Use URI.
    private final String description;
    private final DateTime publishingDate;
    private final Type type;

    public Page(final String title, final String link, final String description, final DateTime publishingDate, final Type type) {
        super();
        this.title = Validate.notEmpty(title, "title");
        this.link = Validate.notNull(link, "link");
        this.description = Validate.notNull(description, "description");
        this.publishingDate = Validate.notNull(publishingDate, "publishingDate");
        this.type = Validate.notNull(type, "type");
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public DateTime getPublishingDate() {
        return publishingDate;
    }

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

    public static final class Pages extends ArrayList<Page> {

        public Pages() {
            super();
        }

    }

    public enum Type {

        POST, SITE;
    }

    public static final class SortByDateAscending implements Comparator<Page> {

        private final Comparator<Object> jodaCompare = DateTimeComparator.getInstance();

        @Override
        public int compare(final Page o1, final Page o2) {
            return jodaCompare.compare(o1.getPublishingDate(), o2.getPublishingDate());
        }

    }
}
