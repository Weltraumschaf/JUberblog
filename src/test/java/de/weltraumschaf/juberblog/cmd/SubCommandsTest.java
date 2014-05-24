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
package de.weltraumschaf.juberblog.cmd;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.cmd.create.CreateSubCommand;
import de.weltraumschaf.juberblog.cmd.install.InstallSubCommand;
import de.weltraumschaf.juberblog.cmd.publish.PublishSubCommand;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link SubCommands}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SubCommandsTest {

    //CHECKSTYLE:OFF
    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    @Test
    public void testToString() {
        assertThat(SubCommands.CREATE.toString(), is(equalTo("create")));
        assertThat(SubCommands.INSTALL.toString(), is(equalTo("install")));
        assertThat(SubCommands.PUBLISH.toString(), is(equalTo("publish")));
    }

    @Test
    public void forSubCommandName_throwExceptionIfNameIsNull() {
        thrown.expect(NullPointerException.class);
        SubCommands.forSubCommandName(null);
    }

    @Test
    public void forSubCommandName_throwExceptionIfNameIsEmpty() {
        thrown.expect(IllegalArgumentException.class);
        SubCommands.forSubCommandName("");
    }

    @Test
    public void forSubCommandName_throwExceptionIfNameIsUnknown() {
        thrown.expect(IllegalArgumentException.class);
        SubCommands.forSubCommandName("foobar");
    }

    @Test
    public void forSubCommandName() {
        assertThat(SubCommands.forSubCommandName("create"), is(SubCommands.CREATE));
        assertThat(SubCommands.forSubCommandName("install"), is(SubCommands.INSTALL));
        assertThat(SubCommands.forSubCommandName("publish"), is(SubCommands.PUBLISH));
    }

    @Test
    public void create_throwsExceptionIfTypeIsNull() {
        thrown.expect(NullPointerException.class);
        SubCommands.create(null, mock(IO.class));
    }

    @Test
    public void create_throwsExceptionIfIoIsNull() {
        thrown.expect(NullPointerException.class);
        SubCommands.create(SubCommands.CREATE, null);
    }

    @Test
    public void create() {
        assertThat(SubCommands.create(SubCommands.CREATE, mock(IO.class)), is(instanceOf(CreateSubCommand.class)));
        assertThat(SubCommands.create(SubCommands.INSTALL, mock(IO.class)), is(instanceOf(InstallSubCommand.class)));
        assertThat(SubCommands.create(SubCommands.PUBLISH, mock(IO.class)), is(instanceOf(PublishSubCommand.class)));
    }
}
