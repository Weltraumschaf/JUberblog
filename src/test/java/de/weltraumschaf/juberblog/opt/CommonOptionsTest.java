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

import com.beust.jcommander.JCommander;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link CommonOptions}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CommonOptionsTest {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON
    protected final JCommander optionsParser = new JCommander();
    protected final CommonOptions sut;

    public CommonOptionsTest() {
        this(new CommonOptions(false, false) {
        });
    }

    protected CommonOptionsTest(final CommonOptions sut) {
        super();
        this.sut = sut;
        optionsParser.addObject(sut);
    }

    @Test
    public void defaultValues() {
        assertThat(sut.isHelp(), is(false));
        assertThat(sut.isVerbose(), is(false));
    }

    @Test
    public void emptyNull() {
        thrown.expect(NullPointerException.class);
        optionsParser.parse((String[]) null);
    }

    @Test
    public void emptyArguments() {
        optionsParser.parse(new String[]{});
        assertThat(sut.isHelp(), is(false));
        assertThat(sut.isVerbose(), is(false));
    }

    @Test
    public void helpShortArgument() {
        optionsParser.parse(new String[]{"-h"});
        assertThat(sut.isHelp(), is(true));
        assertThat(sut.isVerbose(), is(false));
    }

    @Test
    public void helpLongArgument() {
        optionsParser.parse(new String[]{"--help"});
        assertThat(sut.isHelp(), is(true));
        assertThat(sut.isVerbose(), is(false));
    }

    @Test
    public void verboseShortArgument() {
        optionsParser.parse(new String[]{"-v"});
        assertThat(sut.isHelp(), is(false));
        assertThat(sut.isVerbose(), is(false));
        assertThat(sut.isVersion(), is(true));
    }

    @Test
    public void verboseLongArgument() {
        optionsParser.parse(new String[]{"--verbose"});
        assertThat(sut.isHelp(), is(false));
        assertThat(sut.isVerbose(), is(true));
    }

    @Test
    public void helpAndVerboseShortArgument() {
        optionsParser.parse(new String[]{"-h", "-v"});
        assertThat(sut.isHelp(), is(true));
        assertThat(sut.isVersion(), is(true));
        assertThat(sut.isVerbose(), is(false));
    }

    @Test
    public void helpAndVerboseLongArgument() {
        optionsParser.parse(new String[]{"--help", "--verbose"});
        assertThat(sut.isHelp(), is(true));
        assertThat(sut.isVerbose(), is(true));
    }

    @Test
    public void helpShortAndVerboseLongArgument() {
        optionsParser.parse(new String[]{"-h", "--verbose"});
        assertThat(sut.isHelp(), is(true));
        assertThat(sut.isVerbose(), is(true));
    }

    @Test
    public void helpLongAndVerboseShortArgument() {
        optionsParser.parse(new String[]{"--help", "-v"});
        assertThat(sut.isHelp(), is(true));
        assertThat(sut.isVersion(), is(true));
        assertThat(sut.isVerbose(), is(false));
    }
}
