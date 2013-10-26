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

import freemarker.template.TemplateException;
import java.io.IOException;

/**
 * Implementations can format them self to an string with specific encoding.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Formatter {

    /**
     * Formats the object.
     *
     * @return never {@code null}
     * @throws IOException on any I/O error
     * @throws TemplateException on any template error
     */
    String format() throws IOException, TemplateException;

    /**
     * Set the site encoding.
     *
     * @param encoding must not be {@literal null}
     */
    void setEncoding(String encoding);
}
