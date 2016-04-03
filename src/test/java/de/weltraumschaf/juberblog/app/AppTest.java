package de.weltraumschaf.juberblog.app;

import de.weltraumschaf.juberblog.cmd.SubCommand;
import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.commons.system.Environments;
import de.weltraumschaf.commons.testing.CapturedOutput;
import de.weltraumschaf.juberblog.IntegrationTestCase;
import de.weltraumschaf.juberblog.app.App.Factory;
import de.weltraumschaf.juberblog.core.Constants;
import de.weltraumschaf.juberblog.core.ExitCodeImpl;
import de.weltraumschaf.juberblog.options.Options.Command;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link App}.
 *
 * @author Sven Strittmatter
 */
public class AppTest extends IntegrationTestCase {

    private static final String NL = String.format("%n");
    private static final String EXPECTED_VERSION = "1.0.0-SNAPSHOT";

    @Rule
    public final CapturedOutput output = new CapturedOutput();
    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test
    @Ignore
    public void showUsageIfNoArgument() throws Exception {
        output.expectErr("Usage: juberblog create|install|publish -l <dir> [-c <file>] [-h] [-v]");

        App.main(createApp(new String[0]));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    @Ignore
    public void showUsageIfBadSubCommand() {
        output.expectErr("FATAL: Bad arguments!" + NL + "Usage: juberblog create|install|publish -l <dir> [-c <file>] [-h] [-v]");

        App.main(createApp(new String[]{"foobar"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    @Ignore
    public void showUsageIfBadArgument() {
        output.expectErr("FATAL: Bad arguments!" + NL + "Usage: juberblog create|install|publish -l <dir> [-c <file>] [-h] [-v]");

        App.main(createApp(new String[]{"--foobar"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    public void showVersionForLongOption() throws Exception {
        output.expectErr(is(""));
        output.expectOut(EXPECTED_VERSION);

        App.main(createApp(new String[]{"--version"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForShortOption() throws Exception {
        output.expectErr(is(""));
        output.expectOut("");

        App.main(createApp(new String[]{"-h"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForLongOption() throws Exception {
        output.expectErr(is(""));
        output.expectOut("");

        App.main(createApp(new String[]{"--help"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    @Ignore
    public void showHelpForCreateSubCommandShortOption() throws Exception {
        output.expectErr(is(""));
        output.expectOut("");

        App.main(createApp(new String[]{"create", "-h"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    @Ignore
    public void showHelpForCreateSubCommandLongOption() throws Exception {
        output.expectErr(is(""));
        output.expectOut("");

        App.main(createApp(new String[]{"create", "--help"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    @Ignore
    public void showHelpForInstallSubCommandShortOption() throws Exception {
        output.expectErr(is(""));
        output.expectOut("");

        App.main(createApp(new String[]{"install", "-h"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    @Ignore
    public void showHelpForInstallSubCommandLongOption() throws Exception {
        output.expectErr(is(""));
        output.expectOut("");

        App.main(createApp(new String[]{"install", "--help"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    @Ignore
    public void showHelpForPublishSubCommandShortOption() throws Exception {
        output.expectErr(is(""));
        output.expectOut("");

        App.main(createApp(new String[]{"publish", "-h"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    @Ignore
    public void showHelpForPublishSubCommandLongOption() throws Exception {
        output.expectErr(is(""));
        output.expectOut("");

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
        output.expectErr("FATAL: Bad arguments (cause: Unknown option: -x)!" + NL
            + "Usage: create -c|--config <file> [-t|--title <title>] [-d|--draft] [-s|--site]" + NL);

        App.main(createApp(new String[]{"create", "-x"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    public void badArgument_INSTALL() throws Exception {
        output.expectErr("FATAL: Bad arguments (cause: Unknown option: -x)!" + NL
            + "Usage: install -l|--location <directory>" + NL);

        App.main(createApp(new String[]{"install", "-x"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    public void badArgument_PUBLISH() throws Exception {
        output.expectErr("FATAL: Bad arguments (cause: Unknown option: -x)!" + NL
            + "Usage: publish -c|--config <file> [-p|--purge] [-q|--quiet] [-s|--site] [-d|--draft]" + NL);

        App.main(createApp(new String[]{"publish", "-x"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    @Ignore
    public void missingConfig_CREATE() throws Exception {
        output.expectErr(equalTo("FATAL: No configuration file given!\n"
            + "Usage: juberblog create|install|publish -l <dir> [-c <file>] [-h] [-v]\n"));

        final App sut = createApp(new String[]{
            "create",
            "-l", tmp.getRoot().toPath().toString()});

        App.main(sut);

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    @Ignore
    public void execute_CREATE() throws Exception {
        final App sut = createApp(new String[]{
            "create",
            "-c", createPath("config.properties").toString(),
            "-l", tmp.getRoot().toPath().toString()});
        final Factory factory = mock(Factory.class);
        final SubCommand cmd = mock(SubCommand.class);
        when(factory.forName(eq(Command.CREATE), any(JUberblog.class))).thenReturn(cmd);
        sut.injectFactory(factory);

        App.main(sut);

        verify(cmd, times(1)).execute();
        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    @Ignore
    public void missingLocation_INSTALL() throws Exception {
        output.expectErr(equalTo("FATAL: No location directory given!\n"
            + "Usage: juberblog create|install|publish -l <dir> [-c <file>] [-h] [-v]\n"));

        final App sut = createApp(new String[]{
            "install",
            "-c", createPath("config.properties").toString()});

        App.main(sut);

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    @Ignore
    public void execute_INSTALL() throws Exception {
        output.expectErr(equalTo(""));

        final App sut = createApp(new String[]{
            "install",
            "-c", createPath("config.properties").toString(),
            "-l", tmp.getRoot().toPath().toString()});
        final Factory factory = mock(Factory.class);
        final SubCommand cmd = mock(SubCommand.class);
        when(factory.forName(eq(Command.INSTALL), any(JUberblog.class))).thenReturn(cmd);
        sut.injectFactory(factory);

        App.main(sut);

        verify(cmd, times(1)).execute();
        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    @Ignore
    public void missingLocation_PUBLISH() throws Exception {
        output.expectErr(equalTo("FATAL: No location directory given!\n"
            + "Usage: juberblog create|install|publish -l <dir> [-c <file>] [-h] [-v]\n"));

        final App sut = createApp(new String[]{
            "publish",
            "-c", createPath("config.properties").toString()});

        App.main(sut);

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    @Ignore
    public void missingConfig_PUBLISH() throws Exception {
        output.expectErr(equalTo("FATAL: No configuration file given!\n"
            + "Usage: juberblog create|install|publish -l <dir> [-c <file>] [-h] [-v]\n"));

        final App sut = createApp(new String[]{
            "publish",
            "-l", tmp.getRoot().toPath().toString()});

        App.main(sut);

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    @Ignore
    public void execute_PUBLISH() throws Exception {
        output.expectErr(equalTo(""));

        final App sut = createApp(new String[]{
            "publish",
            "-c", createPath("config.properties").toString(),
            "-l", tmp.getRoot().toPath().toString()});
        final Factory factory = mock(Factory.class);
        final SubCommand cmd = mock(SubCommand.class);
        when(factory.forName(eq(Command.PUBLISH), any(JUberblog.class))).thenReturn(cmd);
        sut.injectFactory(factory);

        App.main(sut);

        verify(cmd, times(1)).execute();
        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

}
