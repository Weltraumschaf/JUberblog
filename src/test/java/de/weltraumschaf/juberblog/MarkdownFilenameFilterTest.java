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

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link MarkdownFilenameFilter}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class MarkdownFilenameFilterTest {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON
    private final MarkdownFilenameFilter sut = new MarkdownFilenameFilter();

    @Test
    public void accept_throwsExceptionIfNameIsNull() {
        thrown.expect(NullPointerException.class);
        sut.accept(null, null);
    }

    @Test
    public void accept() {
        assertThat(sut.accept(null, ""), is(false));
        assertThat(sut.accept(null, "fobar"), is(false));
        assertThat(sut.accept(null, "foobar.txt"), is(false));
        assertThat(sut.accept(null, "foobar.md.txt"), is(false));
        assertThat(sut.accept(null, "fobar.md"), is(true));
        assertThat(sut.accept(null, "fobar.txt.md"), is(true));
    }

}
