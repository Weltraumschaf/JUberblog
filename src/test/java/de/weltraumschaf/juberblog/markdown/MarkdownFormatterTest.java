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
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link MarkdownFormatter}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class MarkdownFormatterTest {

    private static final String FIXTURE_PACKAGE = "/de/weltraumschaf/juberblog/markdown";

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    private final MarkdownFormatter fmt = new MarkdownFormatter();

    @Test
    public void format_throwsExceptionIfInputStreamIsNull() throws IOException {
        thrown.expect(NullPointerException.class);
        fmt.format(null);
    }

    @Test
    public void format() throws IOException {
        final InputStream markdownFile = getClass().getResourceAsStream(FIXTURE_PACKAGE + "/post.md");
        final InputStream htmlFile = getClass().getResourceAsStream(FIXTURE_PACKAGE + "/post.html");
        assertThat(fmt.format(markdownFile), is(equalTo(IOUtils.toString(htmlFile))));
        IOUtils.closeQuietly(markdownFile);
        IOUtils.closeQuietly(htmlFile);
    }
}
