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
 * Tests for {@link PublishOptions}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PublishOptionsTest extends CreateAndPublishOptionsTest {

    public PublishOptionsTest() {
        super(new PublishOptions());
    }

    private PublishOptions sut() {
        return (PublishOptions) sut;
    }

    @Test
    public void purgeShortArgument() {
        optionsParser.parse(new String[]{"-p"});
        assertThat(sut().isPurge(), is(true));
    }

    @Test
    public void purgeLongArgument() {
        optionsParser.parse(new String[]{"--purge"});
        assertThat(sut().isPurge(), is(true));
    }

    @Test
    public void quietShortArgument() {
        optionsParser.parse(new String[]{"-q"});
        assertThat(sut().isQuiet(), is(true));
    }

    @Test
    public void quietLongArgument() {
        optionsParser.parse(new String[]{"--quiet"});
        assertThat(sut().isQuiet(), is(true));
    }

    @Test
    public void sitesShortArgument() {
        optionsParser.parse(new String[]{"-s"});
        assertThat(sut().isSites(), is(true));
    }

    @Test
    public void sitesLongArgument() {
        optionsParser.parse(new String[]{"--sites"});
        assertThat(sut().isSites(), is(true));
    }

}