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

import java.net.URISyntaxException;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link DataFile}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class DataFileTest extends JUberblogTestCase {

    @Test
    public void getBareName() throws URISyntaxException {
        final DataFile sut = new DataFile(createPath("posts/2014-05-30T21.29.20_This-is-the-First-Post.md").toString());

        assertThat(sut.getBareName(), is("This-is-the-First-Post"));
    }

}
