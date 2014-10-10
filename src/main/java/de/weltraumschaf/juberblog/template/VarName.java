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
package de.weltraumschaf.juberblog.template;

import de.weltraumschaf.commons.validate.Validate;

/**
 * Common template variable names.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public enum VarName {

    /**
     * Variable key for title.
     */
    TITLE("title"),
    /**
     * Variable key for content.
     */
    CONTENT("content"),
    /**
     * Variable key for encoding.
     */
    ENCODING("encoding"),
    /**
     * Variable key for description.
     */
    DESCRIPTION("description"),
    /**
     * Variable key for base URI.
     */
    BASE_URI("basetUri"),
    /**
     * Variable key for keywords.
     */
    KEYWORDS("keywords"),
    /**
     * Template variable name for date formatted.
     */
    DATE_FORMATTED("date_formatted"),
    /**
     * Template variable name for previous post.
     */
    PREV_POST("prevPost"),
    /**
     * Template variable name for next post.
     */
    NEXT_POST("nextPost"),
    VERSION("version"),
    HEADLINE("headline");
    /**
     * Name of the variable in the template.
     */
    private final String name;

    /**
     * Dedicated constructor.
     *
     * @param name must not be {@code null} or empty
     */
    VarName(final String name) {
        Validate.notEmpty(name, "Name must not be null or empty!");
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
