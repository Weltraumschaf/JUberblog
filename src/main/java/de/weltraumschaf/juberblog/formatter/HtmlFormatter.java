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

/**
 * Implementors read an Markdown file input stream and format it to an string.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface HtmlFormatter extends Formatter {

    /**
     * Set the site title.
     *
     * TODO Rename to setHeadline().
     *
     * @param title must not be {@literal null}
     */
    void setTitle(String title);
    /**
     * Set the site description.
     *
     * @param description must not be {@literal null}
     */
    void setDescription(String description);
    /**
     * Set the site keywords.
     *
     * @param keywords must not be {@literal null}
     */
    void setKeywords(String keywords);
    /**
     * Set the site base URI.
     *
     * @param baseUri must not be {@literal null}
     */
    void setBaseUri(String baseUri);
    /**
     * Set the JUberblog software version.
     *
     * @param version must not be {@literal null}
     */
    void setVersion(String version);
    /**
     * Set the whole blogs headline from blog configuration.
     *
     * @param headline must not be {@literal null}
     */
    void setHeadline(String headline);
}
