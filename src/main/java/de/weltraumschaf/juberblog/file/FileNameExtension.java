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
package de.weltraumschaf.juberblog.file;

import de.weltraumschaf.commons.validate.Validate;

/**
 * Enumerates some common file name extensions.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public enum FileNameExtension {

    /**
     * Extension for Markdown.
     */
    MARKDOWN(".md"),
    /**
     * Extension for HTML.
     */
    HTML(".html"),
    /**
     * Extension von XML.
     */
    XML(".xml");

    /**
     * The extension literal.
     */
    private final String extension;

    /**
     * Dedicated constructor.
     *
     * @param fileNameExtension must not be {@code null} or empty
     */
    private FileNameExtension(final String fileNameExtension) {
        this.extension = Validate.notEmpty(fileNameExtension, "fileNameExtension");
    }

    /**
     * Get the extension literal with leading dot.
     *
     * @return never {@code null} or empty
     */
    public String getExtension() {
        return extension;
    }
}
