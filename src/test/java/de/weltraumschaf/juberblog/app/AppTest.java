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
package de.weltraumschaf.juberblog.app;

import de.weltraumschaf.juberblog.app.App;
import de.weltraumschaf.juberblog.core.SubCommand;
import de.weltraumschaf.juberblog.core.Options;
import de.weltraumschaf.juberblog.core.Constants;
import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.system.Environments.Env;
import de.weltraumschaf.juberblog.app.App.SubCommandName;
import de.weltraumschaf.juberblog.create.CreateSubCommand;
import de.weltraumschaf.juberblog.install.InstallSubCommand;
import de.weltraumschaf.juberblog.publish.PublishSubCommand;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link App}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AppTest {

    @Test
    public void isEnvDebug_true() {
        final Env env = mock(Env.class);
        when(env.get(Constants.ENVIRONMENT_VARIABLE_DEBUG.toString())).thenReturn("true");

        final App sut = new App(new String[0], env);

        assertThat(sut.isEnvDebug(), is(true));
    }

    @Test
    public void isEnvDebug_empty() {
        final Env env = mock(Env.class);
        when(env.get(Constants.ENVIRONMENT_VARIABLE_DEBUG.toString())).thenReturn("");

        final App sut = new App(new String[0], env);

        assertThat(sut.isEnvDebug(), is(false));
    }

    @Test
    public void isEnvDebug_any() {
        final Env env = mock(Env.class);
        when(env.get(Constants.ENVIRONMENT_VARIABLE_DEBUG.toString())).thenReturn("foobar");

        final App sut = new App(new String[0], env);

        assertThat(sut.isEnvDebug(), is(false));
    }

    @Test
    public void createSubcommand_create() throws ApplicationException {
        final Options options = new Options();
        final IO io = mock(IO.class);

        final SubCommand cmd = App.createSubcommand("create", options, io);

        assertThat(cmd, is(instanceOf(CreateSubCommand.class)));
        assertThat(cmd.options(), is(sameInstance(options)));
        assertThat(cmd.io(), is(sameInstance(io)));
    }

    @Test
    public void createSubcommand_install() throws ApplicationException {
        final Options options = new Options();
        final IO io = mock(IO.class);

        final SubCommand cmd = App.createSubcommand("install", options, io);

        assertThat(cmd, is(instanceOf(InstallSubCommand.class)));
        assertThat(cmd.options(), is(sameInstance(options)));
        assertThat(cmd.io(), is(sameInstance(io)));
    }

    @Test
    public void createSubcommand_publish() throws ApplicationException {
        final Options options = new Options();
        final IO io = mock(IO.class);

        final SubCommand cmd = App.createSubcommand("publish", options, io);

        assertThat(cmd, is(instanceOf(PublishSubCommand.class)));
        assertThat(cmd.options(), is(sameInstance(options)));
        assertThat(cmd.io(), is(sameInstance(io)));
    }

    @Test
    public void SubCommandName_betterValueOf() {
        assertThat(SubCommandName.betterValueOf("CREATE"), is(App.SubCommandName.CREATE));
        assertThat(SubCommandName.betterValueOf("create"), is(App.SubCommandName.CREATE));
        assertThat(SubCommandName.betterValueOf("CreATe"), is(App.SubCommandName.CREATE));

        assertThat(SubCommandName.betterValueOf("INSTALL"), is(App.SubCommandName.INSTALL));
        assertThat(SubCommandName.betterValueOf("install"), is(App.SubCommandName.INSTALL));
        assertThat(SubCommandName.betterValueOf("iNStall"), is(App.SubCommandName.INSTALL));

        assertThat(SubCommandName.betterValueOf("PUBLISH"), is(App.SubCommandName.PUBLISH));
        assertThat(SubCommandName.betterValueOf("publish"), is(App.SubCommandName.PUBLISH));
        assertThat(SubCommandName.betterValueOf("pUblISh"), is(App.SubCommandName.PUBLISH));

        assertThat(SubCommandName.betterValueOf("foobar"), is(App.SubCommandName.UNKNOWN));
    }
}
