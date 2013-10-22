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

import com.beust.jcommander.internal.Maps;
import java.util.Map;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link MetaData}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class MetaDataTest {

    private final Map<String, String> fixture = Maps.newHashMap();

    @Test
    public void getDefaults() {
        final MetaData sut = new MetaData(fixture);
        assertThat(sut.getNavi(), is(equalTo("")));
        assertThat(sut.getKeywords(), is(equalTo("")));
        assertThat(sut.getDescription(), is(equalTo("")));
    }

    @Test
    public void getValues() {
        fixture.put("Navi", "foo");
        fixture.put("Description", "bar");
        fixture.put("Keywords", "baz");
        final MetaData sut = new MetaData(fixture);
        assertThat(sut.getNavi(), is(equalTo("foo")));
        assertThat(sut.getDescription(), is(equalTo("bar")));
        assertThat(sut.getKeywords(), is(equalTo("baz")));
    }
}
