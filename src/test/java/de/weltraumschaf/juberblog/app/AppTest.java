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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link App}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AppTest {

    private static final String EXPECTED_VERSION = "1.0.0-SNAPSHOT";
    private static final String EXPECTED_HELP = "Usage: juberblog create|install|publish [-h] [-v]\n"
            + "\n"
            + "Commandline tool to manage your blog.\n"
            + "\n"
            + "Options\n"
            + "\n"
            + "  -v, --version       \n"
            + "  -h, --help          \n"
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
        App.main(createSut(new String[0]));

        output.expectErr("Usage: juberblog create|install|publish [-h] [-v]");
    }

    @Test
    public void showVersionForShortOption() throws Exception {
        App.main(createSut(new String[]{"-v"}));

        output.expectOut(EXPECTED_VERSION);
    }

    @Test
    public void showVersionForLongOption() throws Exception {
        App.main(createSut(new String[]{"--version"}));

        output.expectOut(EXPECTED_VERSION);
    }

    @Test
    public void showVersionForCreateSubCommandShortOption() throws Exception {
        App.main(createSut(new String[]{"create", "-v"}));

        output.expectOut(EXPECTED_VERSION);
    }

    @Test
    public void showVersionForCreateSubCommandLongOption() throws Exception {
        App.main(createSut(new String[]{"create", "--version"}));

        output.expectOut(EXPECTED_VERSION);
    }

    @Test
    public void showVersionForInstallSubCommandShortOption() throws Exception {
        App.main(createSut(new String[]{"install", "-v"}));

        output.expectOut(EXPECTED_VERSION);
    }

    @Test
    public void showVersionForInstallSubCommandLongOption() throws Exception {
        App.main(createSut(new String[]{"install", "--version"}));

        output.expectOut(EXPECTED_VERSION);
    }

    @Test
    public void showVersionForPublishSubCommandShortOption() throws Exception {
        App.main(createSut(new String[]{"publish", "-v"}));

        output.expectOut(EXPECTED_VERSION);
    }

    @Test
    public void showVersionForPublishSubCommandLongOption() throws Exception {
        App.main(createSut(new String[]{"publish", "--version"}));

        output.expectOut(EXPECTED_VERSION);
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

}
