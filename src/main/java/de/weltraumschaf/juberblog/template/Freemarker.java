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

import java.io.IOException;

/**
 * Helper to wrap Freemarker.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class Freemarker {

    /**
     * Hidden for pure static class.
     */
    private Freemarker() {
        super();
    }

    /**
     * Creates an original Freemarker template with empty name and null configuration.
     *
     * @param template must not be {@code null}
     * @return always new instance
     * @throws IOException if any IO error happens
     */
    public static freemarker.template.Template createTemplate(final String template) throws IOException {
        return new freemarker.template.Template("", template, null);
    }
}
