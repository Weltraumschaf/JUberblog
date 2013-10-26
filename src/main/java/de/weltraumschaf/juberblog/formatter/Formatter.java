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
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Formatter {

    /**
     * Formats the input file to string.
     *
     * FIXME Remove this method.
     *
     * @param template must not be {@literal null}
     * @return never {@literal null}
     * @throws IOException if markdown or any template can't be read
     * @throws TemplateException the template produce any parse errors
     */
    @Deprecated
    String format(InputStream template) throws IOException, TemplateException;

    /**
     * Formats the input string to string.
     *
     * FIXME Remove this method.
     *
     * @param template must not be {@literal null}
     * @return never {@literal null}
     * @throws IOException if markdown or any template can't be read
     * @throws TemplateException the template produce any parse errors
     */
    @Deprecated
    String format(String template) throws IOException, TemplateException;

    String format() throws IOException, TemplateException;

    /**
     * Set the site encoding.
     *
     * @param encoding must not be {@literal null}
     */
    void setEncoding(String encoding);
}
