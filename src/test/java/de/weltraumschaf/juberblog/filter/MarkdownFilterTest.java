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

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.pegdown.PegDownProcessor;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link MarkdownFilter}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class MarkdownFilterTest {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    @Test
    public void constructWithNullThrowsExcpetion() {
        thrown.expect(NullPointerException.class);
        new MarkdownFilter(null);
    }

    @Test
    public void apply_withNullThrowsException() {
        thrown.expect(NullPointerException.class);
        new MarkdownFilter().apply(null);
    }

    @Test
    public void apply_empty() {
        assertThat(new MarkdownFilter().apply(""), is(equalTo("")));
    }

    @Test
    public void apply() {
        final PegDownProcessor peg = spy(new PegDownProcessor());
        final MarkdownFilter sut = new MarkdownFilter(peg);
        sut.apply("foobar");
        verify(peg, times(1)).markdownToHtml("foobar");
    }
}
