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

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link Headline}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class HeadlineTest {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    private final Headline sut = new Headline();

    @Test
    public void find_throwsExceptionIfNull() {
        thrown.expect(NullPointerException.class);
        sut.find(null);
    }

    @Test
    public void find() {
        assertThat(sut.find(""), is(equalTo("")));
        assertThat(sut.find("# foo bar baz"), is(equalTo("foo bar baz")));
        assertThat(sut.find("## foo bar baz"), is(equalTo("foo bar baz")));
        assertThat(sut.find("##### foo bar baz"), is(equalTo("foo bar baz")));
        assertThat(sut.find("Lorem dolor\n"
                + "# foo bar baz\n"
                + "Lorem ipsum dolor sit amen"), is(equalTo("foo bar baz")));
        assertThat(sut.find("Lorem dolor\n"
                + "# foo bar baz\n"
                + "Lorem ipsum dolor sit amen\n"
                + "## snafu\n"
                + "sed diam voluptua"), is(equalTo("foo bar baz")));
        assertThat(sut.find("Lorem dolor\n"
                + "Lorem ipsum dolor sit amen\n"
                + "sed diam voluptua"), is(equalTo("")));
    }

    @Test
    public void trim() {
        assertThat(sut.trim(""), is(equalTo("")));
        assertThat(sut.trim("#"), is(equalTo("")));
        assertThat(sut.trim("     "), is(equalTo("")));
        assertThat(sut.trim("##     "), is(equalTo("")));
        assertThat(sut.trim("foobar"), is(equalTo("foobar")));
        assertThat(sut.trim("   foobar   "), is(equalTo("foobar")));
        assertThat(sut.trim("#foobar"), is(equalTo("foobar")));
        assertThat(sut.trim("#  foobar"), is(equalTo("foobar")));
        assertThat(sut.trim("#### foobar"), is(equalTo("foobar")));
    }

}
