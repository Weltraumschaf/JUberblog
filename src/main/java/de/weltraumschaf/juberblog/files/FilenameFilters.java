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

package de.weltraumschaf.juberblog.files;

import java.io.FilenameFilter;

/**
 * Factory to create file name filter.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class FilenameFilters {

    /**
     * Hidden for pure static factory.
     */
    private FilenameFilters() {
        super();
    }

    /**
     * Used to find only Markdown files.
     *
     * @return never {@literal null}, always new instance
     */
    public static FilenameFilter findMarkdownFiles() {
        return new MarkdownFilenameFilter();
    }

    /**
     * Used to find only HTML files.
     *
     * @return never {@literal null}, always new instance
     */
    public static FilenameFilter findHtmlFiles() {
        return new HtmlFilenameFilter();
    }

}
