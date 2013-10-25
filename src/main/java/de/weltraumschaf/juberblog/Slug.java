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

import java.text.Normalizer;
import java.text.Normalizer.Form;
import org.apache.commons.lang3.StringUtils;
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
 * Removes: .,:;'"/?[]\{}|!@#$%^&*()_+-=`~–
 *
 * From <a href="http://stackoverflow.com/questions/1611979/remove-all-non-word-characters-from-a-string-in-java-
 * leaving-accented-charact">Stefan Haberl</a>
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Slug {

    /**
     * Characters to be replaced.
     */
    private static final String[] SEARCH = {"Ä", "ä", "Ö", "ö", "Ü", "ü", "ß"};
    /**
     * Replacements.
     */
    private static final String[] REPLACEMENT = {"Ae", "ae", "Oe", "oe", "Ue", "ue", "sz"};
    /**
     * Default for replacement and return.
     */
    private static final String EMPTY = "";
    /**
     * Clean slug.
     */
    private static final String SLUGIFY_REGEX = "[.:;&=<>/]";
    /**
     * Normalize.
     */
    private static final String NORMALIZER_REGEX = "[^\\p{Alnum}- ]";

    /**
     * Generate the slug.
     *
     * @param in must not be {@code null} or empty
     * @return never {@code null}
     */
    public String generate(final String in) {
        Validate.notEmpty(in, "Input must not be null or empty!");
        return squashDashes(slugify(in));
    }

    /**
     * Normalizes a String by removing all accents to original 127 US-ASCII
     * characters. This method handles German umlauts and "sharp-s" correctly
     *
     * @param s
     *            The String to normalize
     * @return The normalized String
     */
    public static String normalize(final String s) {
        if (s == null) {
            return EMPTY;
        }

        final String n = StringUtils.replaceEachRepeatedly(s, SEARCH, REPLACEMENT);
        return Normalizer.normalize(n, Form.NFD).replaceAll(NORMALIZER_REGEX, EMPTY);
    }

    /**
     * Returns a clean representation of a String which might be used safely
     * within an URL. Slugs are a more human friendly form of URL encoding a
     * String.
     * <p>
     * The method first normalizes a String, then converts it to lowercase and
     * removes ASCII characters, which might be problematic in URLs:
     * <ul>
     * <li>all whitespaces
     * <li>dots ('.')
     * <li>(semi-)colons (';' and ':')
     * <li>equals ('=')
     * <li>ampersands ('&')
     * <li>slashes ('/')
     * <li>angle brackets ('<' and '>')
     * </ul>
     *
     * @param s
     *            The String to slugify
     * @return The slugified String
     * @see #normalize(String)
     */
    public static String slugify(final String s) {
        if (s == null) {
            return EMPTY;
        }

        return normalize(s).replaceAll("[\\s]", "-").replaceAll(SLUGIFY_REGEX, EMPTY);
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
