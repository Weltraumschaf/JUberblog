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

package de.weltraumschaf.juberblog.files;

import java.io.FilenameFilter;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link HtmlFilenameFilter}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class HtmlFilenameFilterTest {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON
    private final FilenameFilter sut = new HtmlFilenameFilter();

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
        assertThat(sut.accept(null, "foobar.html.txt"), is(false));
        assertThat(sut.accept(null, "fobar.html"), is(true));
        assertThat(sut.accept(null, "fobar.txt.html"), is(true));
    }

}
