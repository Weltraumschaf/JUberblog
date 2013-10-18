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
package de.weltraumschaf.juberblog.filter;

import org.apache.commons.lang3.Validate;
import org.pegdown.PegDownProcessor;

/**
 * Processes an Markdown string to an HTML string.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class MarkdownFilter implements Filter {

    /**
     * Returned if Pegdown returns {@code null}.
     */
    private static final String EMPTY = "";
    /**
     * Default processor.
     */
    private static final PegDownProcessor DEFAULT_PROCESSOR = new PegDownProcessor();
    /**
     * Markdown processor.
     */
    private final PegDownProcessor peg;

    /**
     * Initializes the Pegdown processor with {@link #DEFAULT_PROCESSOR}.
     */
    public MarkdownFilter() {
        this(DEFAULT_PROCESSOR);
    }

    /**
     * Dedicated constructor.
     *
     * @param peg must not be {@code null}
     */
    public MarkdownFilter(final PegDownProcessor peg) {
        super();
        Validate.notNull(peg, "Peg must not be null!");
        this.peg = peg;
    }

    @Override
    public String apply(final String input) {
        Validate.notNull(input, "Input must not be null!");
        final String html = peg.markdownToHtml(input);
        return html == null ? EMPTY : html;
    }
}
