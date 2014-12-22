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

package de.weltraumschaf.juberblog.file;

import de.weltraumschaf.juberblog.BaseTestCase;
import java.net.URISyntaxException;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link DataFile}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class DataFileTest extends BaseTestCase {

    private final DataFile sut = new DataFile(createPath("posts/2014-05-30T21.29.20_This-is-the-First-Post.md").toString());

    public DataFileTest() throws URISyntaxException {
        super();
    }

    @Test
    public void getBaseName() {
        assertThat(sut.getBaseName(), is("2014-05-30T21.29.20_This-is-the-First-Post.md"));
    }

    @Test
    public void getBareName() {
        assertThat(sut.getBareName(), is("This-is-the-First-Post"));
    }

    @Test
    public void getCreationDate() {
        assertThat(sut.getCreationDate(), is(new DateTime("2014-05-30T21:29:20")));
    }
}
