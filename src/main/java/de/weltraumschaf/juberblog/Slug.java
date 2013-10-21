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

import org.apache.commons.lang3.Validate;

/**
 * Generate slug for speaking URIs.
 *
 * Examples:
 * <pre>
 * "This is an example" -> "This-is-an-example"
 * "This is-an example" -> "This-is_-_an-example"
 * "This is   an   example" -> "This-is-an-example"
 * </pre>
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Slug {

    /**
     * Generate the slug.
     *
     * @param in must not be {@code null} or empty
     * @return never {@code null}
     */
    public String generate(final String in) {
        Validate.notEmpty(in, "Input must not be null or empty!");
        return squashDashes(in.replaceAll("-", "_-_").replaceAll(" ", "-"));
    }

    /**
     * Squashes multiple dashes to one ({@code "---" -> "-"}.
     *
     * @param in must not be {@code null}
     * @return never {@code null}
     */
    private String squashDashes(final String in) {
        Validate.notNull(in, "In must not be null!");
        String out = in;

        while (out.contains("--")) {
            out = out.replaceAll("--", "-");
        }

        return out;
    }

}
