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

import de.weltraumschaf.commons.system.Environments;
import de.weltraumschaf.commons.system.NullExiter;
import de.weltraumschaf.commons.testing.CapturedOutput;
import de.weltraumschaf.juberblog.core.Constants;
import de.weltraumschaf.juberblog.core.JUberblog;
import de.weltraumschaf.juberblog.core.SubCommand;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link App}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AppTest {

    private static final String EXPECTED_VERSION = "1.0.0-SNAPSHOT";
    private static final String EXPECTED_HELP = "Usage: juberblog create|install|publish [-h] [-v] -c <file> -l <dir>\n"
            + "\n"
            + "Commandline tool to manage your blog.\n"
            + "\n"
            + "Options\n"
            + "\n"
            + "  -c, --config        Config file to use.\n"
            + "  -v, --version       Show the version.\n"
            + "  -l, --location      Location of the blog installation.\n"
            + "  -h, --help          Show this help.\n"
            + "\n"
            + "Example\n"
            + "\n"
            + "  TODO\n"
            + "\n"
            + "\n";

    @Rule
    public final CapturedOutput output = new CapturedOutput();

    private App createSut(final String[] args) {
        final App sut = new App(args);
        sut.setExiter(new NullExiter());
        return sut;
    }

    @Test
    public void showUsageIfNoArgument() throws Exception {
        output.expectErr("Usage: juberblog create|install|publish [-h] [-v]");

        App.main(createSut(new String[0]));
    }

    @Test
    public void showVersionForShortOption() throws Exception {
        output.expectErr("");
        output.expectOut(EXPECTED_VERSION);

        App.main(createSut(new String[]{"-v"}));
    }

    @Test
    public void showVersionForLongOption() throws Exception {
        output.expectOut(EXPECTED_VERSION);

        App.main(createSut(new String[]{"--version"}));
    }

    @Test
    public void showVersionForCreateSubCommandShortOption() throws Exception {
        output.expectOut(EXPECTED_VERSION);

        App.main(createSut(new String[]{"create", "-v"}));
    }

    @Test
    public void showVersionForCreateSubCommandLongOption() throws Exception {
        output.expectOut(EXPECTED_VERSION);

        App.main(createSut(new String[]{"create", "--version"}));
    }

    @Test
    public void showVersionForInstallSubCommandShortOption() throws Exception {
        App.main(createSut(new String[]{"install", "-v"}));

        output.expectOut(EXPECTED_VERSION);
    }

    @Test
    public void showVersionForInstallSubCommandLongOption() throws Exception {
        output.expectOut(EXPECTED_VERSION);

        App.main(createSut(new String[]{"install", "--version"}));
    }

    @Test
    public void showVersionForPublishSubCommandShortOption() throws Exception {
        output.expectOut(EXPECTED_VERSION);

        App.main(createSut(new String[]{"publish", "-v"}));
    }

    @Test
    public void showVersionForPublishSubCommandLongOption() throws Exception {
        output.expectOut(EXPECTED_VERSION);

        App.main(createSut(new String[]{"publish", "--version"}));
    }

    @Test
    public void showHelpForShortOption() throws Exception {
        App.main(createSut(new String[]{"-h"}));

        output.expectOut(EXPECTED_HELP);
    }

    @Test
    public void showHelpForLongOption() throws Exception {
        App.main(createSut(new String[]{"--help"}));

        output.expectOut(EXPECTED_HELP);
    }

    @Test
    public void showHelpForCreateSubCommandShortOption() throws Exception {
        App.main(createSut(new String[]{"create", "-h"}));

        output.expectOut(EXPECTED_HELP);
    }

    @Test
    public void showHelpForCreateSubCommandLongOption() throws Exception {
        App.main(createSut(new String[]{"create", "--help"}));

        output.expectOut(EXPECTED_HELP);
    }

    @Test
    public void showHelpForInstallSubCommandShortOption() throws Exception {
        App.main(createSut(new String[]{"install", "-h"}));

        output.expectOut(EXPECTED_HELP);
    }

    @Test
    public void showHelpForInstallSubCommandLongOption() throws Exception {
        App.main(createSut(new String[]{"install", "--help"}));

        output.expectOut(EXPECTED_HELP);
    }

    @Test
    public void showHelpForPublishSubCommandShortOption() throws Exception {
        App.main(createSut(new String[]{"publish", "-h"}));

        output.expectOut(EXPECTED_HELP);
    }

    @Test
    public void showHelpForPublishSubCommandLongOption() throws Exception {
        App.main(createSut(new String[]{"publish", "--help"}));

        output.expectOut(EXPECTED_HELP);
    }

    @Test
    public void isEnvDebug_true() {
        final Environments.Env env = mock(Environments.Env.class);
        when(env.get(Constants.ENVIRONMENT_VARIABLE_DEBUG.toString())).thenReturn("true");

        final App sut = new App(new String[0], env);

        assertThat(sut.isEnvDebug(), is(true));
    }

    @Test
    public void isEnvDebug_empty() {
        final Environments.Env env = mock(Environments.Env.class);
        when(env.get(Constants.ENVIRONMENT_VARIABLE_DEBUG.toString())).thenReturn("");

        final App sut = new App(new String[0], env);

        assertThat(sut.isEnvDebug(), is(false));
    }

    @Test
    public void isEnvDebug_any() {
        final Environments.Env env = mock(Environments.Env.class);
        when(env.get(Constants.ENVIRONMENT_VARIABLE_DEBUG.toString())).thenReturn("foobar");

        final App sut = new App(new String[0], env);

        assertThat(sut.isEnvDebug(), is(false));
    }

    @Test
    public void execute_CREATE() throws Exception {
        final App sut = createSut(new String[]{"create"});
        final SubCommand.Factory factory = mock(SubCommand.Factory.class);
        final SubCommand cmd = mock(SubCommand.class);
        when(factory.forName(eq(SubCommand.Name.CREATE), any(JUberblog.class))).thenReturn(cmd);
        sut.injectFactory(factory);

        App.main(sut);

        verify(cmd, times(1)).execute();
    }

    @Test
    public void execute_INSTALL() throws Exception {
        final App sut = createSut(new String[]{"install"});
        final SubCommand.Factory factory = mock(SubCommand.Factory.class);
        final SubCommand cmd = mock(SubCommand.class);
        when(factory.forName(eq(SubCommand.Name.INSTALL), any(JUberblog.class))).thenReturn(cmd);
        sut.injectFactory(factory);

        App.main(sut);

        verify(cmd, times(1)).execute();
    }

    @Test
    public void execute_PUBLISH() throws Exception {
        final App sut = createSut(new String[]{"publish"});
        final SubCommand.Factory factory = mock(SubCommand.Factory.class);
        final SubCommand cmd = mock(SubCommand.class);
        when(factory.forName(eq(SubCommand.Name.PUBLISH), any(JUberblog.class))).thenReturn(cmd);
        sut.injectFactory(factory);

        App.main(sut);

        verify(cmd, times(1)).execute();
    }

}
