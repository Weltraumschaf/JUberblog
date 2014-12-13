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

package de.weltraumschaf.juberblog.app;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link Arguments}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class ArgumentsTest {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    @Test
    public void throwExceptionIfconstructWithNull() {
        thrown.expect(NullPointerException.class);
        new Arguments(null);
    }

    @Test
    public void getFirstArgument() {
        assertThat(new Arguments(new String[] {}).getFirstArgument(), is(equalTo("")));
        assertThat(new Arguments(new String[] {"foo"}).getFirstArgument(), is(equalTo("foo")));
        assertThat(new Arguments(new String[] {"foo", "bar"}).getFirstArgument(), is(equalTo("foo")));
        assertThat(new Arguments(new String[] {"foo", "bar", "baz"}).getFirstArgument(), is(equalTo("foo")));
    }

    @Test
    public void getTailArguments() {
        assertThat(new Arguments(new String[] {}).getTailArguments(), is(equalTo(new String[] {})));
        assertThat(new Arguments(new String[] {"foo"}).getTailArguments(), is(equalTo(new String[] {})));
        assertThat(new Arguments(new String[] {"foo", "bar"}).getTailArguments(),
                is(equalTo(new String[] {"bar"})));
        assertThat(new Arguments(new String[] {"foo", "bar", "baz"}).getTailArguments(),
                is(equalTo(new String[] {"bar", "baz"})));
    }

    @Test
    public void size() {
        assertThat(new Arguments(new String[] {}).size(), is(equalTo(0)));
        assertThat(new Arguments(new String[] {"foo"}).size(), is(equalTo(1)));
        assertThat(new Arguments(new String[] {"foo", "bar"}).size(), is(equalTo(2)));
        assertThat(new Arguments(new String[] {"foo", "bar", "baz"}).size(), is(equalTo(3)));
    }

    @Test
    public void testToString() {
        assertThat(new Arguments(new String[] {"foo", "bar", "baz"}).toString(),
                is(equalTo("first: foo, tail: [bar, baz]")));
    }

}
