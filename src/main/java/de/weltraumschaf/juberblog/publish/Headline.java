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

package de.weltraumschaf.juberblog.publish;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.core.Constants;

/**
 * Parses the first headline of a given Markdown string.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class Headline {

    /**
     * Markdown head lines start with this character.
     */
    private static final char HEADLINE_TOKEN = '#';

    /**
     * Finds the first headline in a Markdown string.
     *
     * @param markdown must not be {@code null}
     * @return never {@code null} maybe empty
     */
    public String find(final String markdown) {
        Validate.notNull(markdown, "Markdown must not be null!");

        for (final String line : markdown.split(Constants.DEFAULT_NEW_LINE.toString())) {
            if (line.startsWith(String.valueOf(HEADLINE_TOKEN))) {
                return trim(line);
            }
        }

        return "";
    }

    /**
     * Removes the {@link #HEADLINE_TOKEN} from beginning of string and removes leading and trailing white spaces.
     *
     * @param line must not be {@code null}
     * @return never {@code null} maybe empty
     */
    String trim(final String line) {
        int begin = 0;

        for (int i = 0; i < line.length(); ++i) {
            if (line.charAt(i) == HEADLINE_TOKEN) {
                begin = i + 1;
            }
        }

        return line.substring(begin).trim();
    }

}
