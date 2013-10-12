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
package de.weltraumschaf.juberblog.markdown;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.pegdown.PegDownProcessor;

/**
 * Formats an <a href="http://daringfireball.net/projects/markdown/">Markdown</a> input stream.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class MarkdownFormatter {

    /**
     * Formats given input stream from Markdown to HTML.
     *
     * @param markdownFile must not be {@code null}
     * @return never {@code null}
     * @throws IOException if can't read from stream
     */
    public String format(final InputStream markdownFile) throws IOException {
        Validate.notNull(markdownFile, "Markdown file must not be null!");
        final String markdownContent = IOUtils.toString(markdownFile);
        final PegDownProcessor peg = new PegDownProcessor();
        final String html = peg.markdownToHtml(markdownContent);
        return html == null ? "" : html;
    }
}
