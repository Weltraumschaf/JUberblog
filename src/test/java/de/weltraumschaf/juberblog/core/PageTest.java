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
package de.weltraumschaf.juberblog.core;

import java.net.URI;
import nl.jqno.equalsverifier.EqualsVerifier;
import static org.hamcrest.Matchers.is;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link Page}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PageTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Page.class).verify();
    }

    @Test
    public void toStringContainsAllProperties() {
        final Page sut = new Page(
                "title",
                URI.create("http://juberblog.local"),
                "desc",
                new DateTime(0L),
                Page.Type.POST);

        assertThat(
                sut.toString(),
                is("Page{"
                        + "title=title, "
                        + "link=http://juberblog.local, "
                        + "description=desc, "
                        + "publishingDate=1970-01-01T01:00:00.000+01:00, "
                        + "type=POST}"));
    }

}
