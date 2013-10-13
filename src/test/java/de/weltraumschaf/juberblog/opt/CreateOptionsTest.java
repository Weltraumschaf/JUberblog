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

package de.weltraumschaf.juberblog.opt;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link CreateOptions}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CreateOptionsTest extends CreateAndPublishOptionsTest {

    public CreateOptionsTest() {
        super(new CreateOptions());
    }

    private CreateOptions sut() {
        return (CreateOptions) sut;
    }

    @Test
    @Override
    public void defaultValues() {
        super.defaultValues();
    }

    @Test
    public void siteShortArgument() {
        optionsParser.parse(new String[]{"-s"});
        assertThat(sut().isSite(), is(true));
    }

    @Test
    public void siteLongArgument() {
        optionsParser.parse(new String[]{"--site"});
        assertThat(sut().isSite(), is(true));
    }

    @Test
    public void titleShortArgument() {
        optionsParser.parse(new String[]{"-t", "foobar"});
        assertThat(sut().getTitle(), is(equalTo("foobar")));
    }

    @Test
    public void titleLongArgument() {
        optionsParser.parse(new String[]{"--title", "foobar"});
        assertThat(sut().getTitle(), is(equalTo("foobar")));
    }
}