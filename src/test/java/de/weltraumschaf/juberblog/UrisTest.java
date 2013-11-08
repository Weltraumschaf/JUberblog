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
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link Uris}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class UrisTest {

    private final Uris sut = new Uris("http://www.foobar.com/");

    public UrisTest() throws URISyntaxException {
        super();
    }

    @Test
    public void uris() {
        assertThat(sut.posts().toString(), is(equalTo("http://www.foobar.com/posts/")));
        assertThat(sut.sites().toString(), is(equalTo("http://www.foobar.com/sites/")));
        assertThat(sut.drafts().toString(), is(equalTo("http://www.foobar.com/drafts/")));
        assertThat(sut.draftPosts().toString(), is(equalTo("http://www.foobar.com/drafts/posts/")));
        assertThat(sut.draftSites().toString(), is(equalTo("http://www.foobar.com/drafts/sites/")));
    }

}
