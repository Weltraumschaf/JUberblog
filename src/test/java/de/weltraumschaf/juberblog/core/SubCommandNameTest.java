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

import de.weltraumschaf.juberblog.core.SubCommand.SubCommandName;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SubCommandNameTest {

    @Test
    public void betterValueOf() {
        assertThat(SubCommandName.betterValueOf("CREATE"), is(SubCommandName.CREATE));
        assertThat(SubCommandName.betterValueOf("create"), is(SubCommandName.CREATE));
        assertThat(SubCommandName.betterValueOf("CreATe"), is(SubCommandName.CREATE));

        assertThat(SubCommandName.betterValueOf("INSTALL"), is(SubCommandName.INSTALL));
        assertThat(SubCommandName.betterValueOf("install"), is(SubCommandName.INSTALL));
        assertThat(SubCommandName.betterValueOf("iNStall"), is(SubCommandName.INSTALL));

        assertThat(SubCommandName.betterValueOf("PUBLISH"), is(SubCommandName.PUBLISH));
        assertThat(SubCommandName.betterValueOf("publish"), is(SubCommandName.PUBLISH));
        assertThat(SubCommandName.betterValueOf("pUblISh"), is(SubCommandName.PUBLISH));

        assertThat(SubCommandName.betterValueOf("foobar"), is(SubCommandName.UNKNOWN));
    }

    @Test
    public void isSubCommand() {
        assertThat(SubCommandName.isSubCommand("CREATE"), is(true));
        assertThat(SubCommandName.isSubCommand("create"), is(true));
        assertThat(SubCommandName.isSubCommand("CreATe"), is(true));

        assertThat(SubCommandName.isSubCommand("INSTALL"), is(true));
        assertThat(SubCommandName.isSubCommand("install"), is(true));
        assertThat(SubCommandName.isSubCommand("iNStall"), is(true));

        assertThat(SubCommandName.isSubCommand("PUBLISH"), is(true));
        assertThat(SubCommandName.isSubCommand("publish"), is(true));
        assertThat(SubCommandName.isSubCommand("pUblISh"), is(true));

        assertThat(SubCommandName.isSubCommand("foobar"), is(false));
    }
}
