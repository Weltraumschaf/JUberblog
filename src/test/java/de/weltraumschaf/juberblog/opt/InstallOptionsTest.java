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
 * Tests for {@link InstallOptions}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class InstallOptionsTest extends CommonOptionsTest {

public InstallOptionsTest() {
        super(new InstallOptions());
    }

    private InstallOptions sut() {
        return (InstallOptions) sut;
    }

    @Test
    @Override
    public void defaultValues() {
        super.defaultValues();
        assertThat(sut().getLocation(), is(equalTo("")));
    }

    @Test
    public void locationShortArgument() {
        optionsParser.parse(new String[]{"-l", "foobar"});
        assertThat(sut().getLocation(), is(equalTo("foobar")));
    }

    @Test
    public void locationLongArgument() {
        optionsParser.parse(new String[]{"--location", "foobar"});
        assertThat(sut().getLocation(), is(equalTo("foobar")));
    }

}