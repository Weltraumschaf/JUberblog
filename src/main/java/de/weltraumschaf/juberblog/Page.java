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

import org.joda.time.DateTime;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Page {

    private final String title;
    private final String link; // TODO USe URI.
    private final String description;
    private final DateTime publishingDate;

    public Page(String title, String link, String description, DateTime publishingDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.publishingDate = publishingDate;
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

}
