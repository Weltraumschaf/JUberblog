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

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Post {

    private final String url;
    private final String title;
    private final String publishingDate;

    public Post(String url, String title, String publishingDate) {
        this.url = url;
        this.title = title;
        this.publishingDate = publishingDate;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getPublishingDate() {
        return publishingDate;
    }
}
