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
package de.weltraumschaf.juberblog.cmd.install;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link Scaffold}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class ScaffoldTest {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    @Test
    public void JarResource_newSourceJar_nullAsPath() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Parameter 'path' must not be null or empty!");

        Scaffold.JarResource.newSourceJar(null);
    }

    @Test
    public void JarResource_newSourceJar_emptyAsPath() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Parameter 'path' must not be null or empty!");

        Scaffold.JarResource.newSourceJar("");
    }

    @Test
    public void JarResource_newSourceJar_noBangInPath() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Path does not contain '!'!");

        Scaffold.JarResource.newSourceJar("/foo/bar/baz");
    }

    @Test
    public void JarResource_newSourceJar_emptyBeforeBangInPath() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The string before the '!' must not be null or empty!");

        Scaffold.JarResource.newSourceJar("!/foo/bar/baz");
    }

    @Test
    public void JarResource_newSourceJar_emptyAfterBangInPath() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The string after the '!' must not be null or empty!");

        Scaffold.JarResource.newSourceJar("/foo/bar/baz!");
    }

    @Test
    public void JarResource_newSourceJar() {
        final Scaffold.JarResource src = Scaffold.JarResource.newSourceJar("/foo/bar/baz!/snafu/bla/blub");

        assertThat(src.getJarLocation(), is(equalTo("/foo/bar/baz")));
        assertThat(src.getResourceLocation(), is(equalTo("/snafu/bla/blub")));
    }
}
