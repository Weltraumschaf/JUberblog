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
import java.io.InputStream;

/**
 * Implementors read an Markdown file input stream and format it to an string.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Formatter {

    /**
     * Formats the input file to string.
     *
     * @param markdownFile must not be {@code null}
     * @return never {@code null}
     * @throws IOException if markdown or any template can't be read
     * @throws TemplateException the template produce any parse errors
     */
    String format(final InputStream markdownFile) throws IOException, TemplateException;
    /**
     * Formats the input string to string.
     *
     * @param markdown must not be {@code null}
     * @return never {@code null}
     * @throws IOException if markdown or any template can't be read
     * @throws TemplateException the template produce any parse errors
     */
    String format(final String markdown) throws IOException, TemplateException;

}
