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
 * Tests for {@link Slug}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SlugTest {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON
    private final Slug sut = new Slug();

    @Test
    public void generate_throwsExcpetionIfNull() {
        thrown.expect(NullPointerException.class);
        sut.generate(null);
    }

    @Test
    public void generate_throwsExcpetionIfEmpty() {
        thrown.expect(IllegalArgumentException.class);
        sut.generate("");
    }

    @Test
    public void generate() {
        assertThat(sut.generate("This is an example"), is(equalTo("This-is-an-example")));
        assertThat(sut.generate("This is-an example"), is(equalTo("This-is-an-example")));
        assertThat(sut.generate("This    is  an   example"), is(equalTo("This-is-an-example")));
        assertThat(
                sut.generate("a.b,c:d;e'f\"g/h?i[j]k\\l{m}n|o!p@q#r$s%t^u&v*w(x)y_z+1-2=3`4~5–6"),
                is(equalTo("abcdefghijklmnopqrstuvwxyz1-23456")));
    }

    @Test
    public void normalize() {
        assertThat(
            Slug.normalize("This is an example with ß and Ü-Umlaut"),
            is(equalTo("This is an example with sz and Ue-Umlaut")));
    }

    @Test
    public void slugify() {
        assertThat(
            Slug.slugify("This is an example with ß and Ü-Umlaut"),
            is(equalTo("This-is-an-example-with-sz-and-Ue-Umlaut")));
    }
}
