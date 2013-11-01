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

package de.weltraumschaf.juberblog.model;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link DataFile}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class DataFileTest {

    private final DataFile sut = new DataFile("/foo/bar/1383315520.This-is-the-First-Post.md");

    @Test
    public void getFilename() {
        assertThat(sut.getFilename(), is(equalTo("/foo/bar/1383315520.This-is-the-First-Post.md")));
    }

    @Test
    public void getBasename() {
        assertThat(sut.getBasename(), is(equalTo("1383315520.This-is-the-First-Post.md")));
    }

    @Test
    public void getCreationTime() {
        assertThat(sut.getCreationTime(), is(equalTo(1383315520L)));
    }

    @Test
    public void getSlug() {
        assertThat(sut.getSlug(), is(equalTo("This-is-the-First-Post")));
    }
}
