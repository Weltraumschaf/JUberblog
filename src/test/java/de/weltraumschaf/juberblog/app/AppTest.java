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

import de.weltraumschaf.juberblog.cmd.SubCommand;
import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.commons.system.Environments;
import de.weltraumschaf.commons.testing.CapturedOutput;
import de.weltraumschaf.juberblog.IntegrationTestCase;
import de.weltraumschaf.juberblog.app.App.Factory;
import de.weltraumschaf.juberblog.core.Constants;
import de.weltraumschaf.juberblog.core.ExitCodeImpl;
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
public class AppTest extends IntegrationTestCase {

    private static final String NL = String.format("%n");
    private static final String EXPECTED_VERSION = "1.0.0-SNAPSHOT";
    private static final String EXPECTED_HELP = "Usage: juberblog create|install|publish [-h] [-v] -c <file> -l <dir>" + NL
            + NL
            + "Commandline tool to manage your blog." + NL
            + NL
            + "Options" + NL
            + NL
            + "  -c, --config        Config file to use." + NL
            + "  -h, --help          Show this help." + NL
            + "  -l, --location      Location of the blog installation." + NL
            + "  -v, --version       Show the version." + NL
            + NL
            + "Example" + NL
            + NL
            + "  TODO" + NL
            + NL
            + NL;

    @Rule
    public final CapturedOutput output = new CapturedOutput();

    @Test
    public void showUsageIfNoArgument() throws Exception {
        output.expectErr("Usage: juberblog create|install|publish [-h] [-v]");

        App.main(createApp(new String[0]));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    public void showUsageIfBadSubCommand() {
        output.expectErr("FATAL: Bad arguments!" + NL + "Usage: juberblog create|install|publish [-h] [-v]");

        App.main(createApp(new String[]{"foobar"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    public void showUsageIfBadArgument() {
        output.expectErr("FATAL: Bad arguments!" + NL + "Usage: juberblog create|install|publish [-h] [-v]");

        App.main(createApp(new String[]{"--foobar"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    public void showVersionForShortOption() throws Exception {
        output.expectErr("");
        output.expectOut(EXPECTED_VERSION);

        App.main(createApp(new String[]{"-v"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showVersionForLongOption() throws Exception {
        output.expectErr("");
        output.expectOut(EXPECTED_VERSION);

        App.main(createApp(new String[]{"--version"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showVersionForCreateSubCommandShortOption() throws Exception {
        output.expectErr("");
        output.expectOut(EXPECTED_VERSION);

        App.main(createApp(new String[]{"create", "-v"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showVersionForCreateSubCommandLongOption() throws Exception {
        output.expectErr("");
        output.expectOut(EXPECTED_VERSION);

        App.main(createApp(new String[]{"create", "--version"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showVersionForInstallSubCommandShortOption() throws Exception {
        output.expectErr("");
        App.main(createApp(new String[]{"install", "-v"}));

        output.expectOut(EXPECTED_VERSION);

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showVersionForInstallSubCommandLongOption() throws Exception {
        output.expectErr("");
        output.expectOut(EXPECTED_VERSION);

        App.main(createApp(new String[]{"install", "--version"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showVersionForPublishSubCommandShortOption() throws Exception {
        output.expectErr("");
        output.expectOut(EXPECTED_VERSION);

        App.main(createApp(new String[]{"publish", "-v"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showVersionForPublishSubCommandLongOption() throws Exception {
        output.expectErr("");
        output.expectOut(EXPECTED_VERSION);

        App.main(createApp(new String[]{"publish", "--version"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForShortOption() throws Exception {
        output.expectErr("");
        output.expectOut(EXPECTED_HELP);

        App.main(createApp(new String[]{"-h"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForLongOption() throws Exception {
        output.expectErr("");
        output.expectOut(EXPECTED_HELP);

        App.main(createApp(new String[]{"--help"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForCreateSubCommandShortOption() throws Exception {
        output.expectErr("");
        output.expectOut(EXPECTED_HELP);

        App.main(createApp(new String[]{"create", "-h"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForCreateSubCommandLongOption() throws Exception {
        output.expectErr("");
        output.expectOut(EXPECTED_HELP);

        App.main(createApp(new String[]{"create", "--help"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForInstallSubCommandShortOption() throws Exception {
        output.expectErr("");
        output.expectOut(EXPECTED_HELP);

        App.main(createApp(new String[]{"install", "-h"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForInstallSubCommandLongOption() throws Exception {
        output.expectErr("");
        output.expectOut(EXPECTED_HELP);

        App.main(createApp(new String[]{"install", "--help"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForPublishSubCommandShortOption() throws Exception {
        output.expectErr("");
        output.expectOut(EXPECTED_HELP);

        App.main(createApp(new String[]{"publish", "-h"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForPublishSubCommandLongOption() throws Exception {
        output.expectErr("");
        output.expectOut(EXPECTED_HELP);

        App.main(createApp(new String[]{"publish", "--help"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
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
    public void badArgument_CREATE() throws Exception {
        output.expectErr("FATAL: Unknown option: -x" + NL
                + "Usage: juberblog create|install|publish [-h] [-v] -c <file> -l <dir>");

        App.main(createApp(new String[]{"create", "-x"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    public void badArgument_INSTALL() throws Exception {
        output.expectErr("FATAL: Unknown option: -x" + NL
                + "Usage: juberblog create|install|publish [-h] [-v] -c <file> -l <dir>");

        App.main(createApp(new String[]{"install", "-x"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    public void badArgument_PUBLISH() throws Exception {
        output.expectErr("FATAL: Unknown option: -x" + NL
                + "Usage: juberblog create|install|publish [-h] [-v] -c <file> -l <dir>");

        App.main(createApp(new String[]{"publish", "-x"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    public void execute_CREATE() throws Exception {
        final App sut = createApp(new String[]{"create", "-c", createPath("config.properties").toString()});
        final Factory factory = mock(Factory.class);
        final SubCommand cmd = mock(SubCommand.class);
        when(factory.forName(eq(SubCommand.Name.CREATE), any(JUberblog.class))).thenReturn(cmd);
        sut.injectFactory(factory);

        App.main(sut);

        verify(cmd, times(1)).execute();
        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void execute_INSTALL() throws Exception {
        output.expectErr("");

        final App sut = createApp(new String[]{"install", "-c", createPath("config.properties").toString()});
        final Factory factory = mock(Factory.class);
        final SubCommand cmd = mock(SubCommand.class);
        when(factory.forName(eq(SubCommand.Name.INSTALL), any(JUberblog.class))).thenReturn(cmd);
        sut.injectFactory(factory);

        App.main(sut);

        verify(cmd, times(1)).execute();
        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void execute_PUBLISH() throws Exception {
        output.expectErr("");

        final App sut = createApp(new String[]{"publish", "-c", createPath("config.properties").toString()});
        final Factory factory = mock(Factory.class);
        final SubCommand cmd = mock(SubCommand.class);
        when(factory.forName(eq(SubCommand.Name.PUBLISH), any(JUberblog.class))).thenReturn(cmd);
        sut.injectFactory(factory);

        App.main(sut);

        verify(cmd, times(1)).execute();
        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

}
