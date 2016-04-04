package de.weltraumschaf.juberblog.app;

import de.weltraumschaf.juberblog.cmd.SubCommand;
import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.commons.system.Environments;
import de.weltraumschaf.commons.testing.rules.CapturedOutput;
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
    public void showUsageIfNoArgument() throws Exception {
        output.expectErr("FATAL: Bad arguments!" + NL
            + "Usage: create|install|publish [--version] [-h|--help]" + NL);
        output.expectOut(is(""));

        App.main(createApp(new String[0]));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    public void showUsageIfBadSubCommand() {
        output.expectErr("FATAL: Bad arguments (cause: Expected a command, got foobar)!" + NL
            + "Usage: create|install|publish [--version] [-h|--help]" + NL);
        output.expectOut(is(""));

        App.main(createApp(new String[]{"foobar"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    public void showUsageIfBadArgument() {
        output.expectErr("FATAL: Bad arguments (cause: Unknown option: --foobar)!" + NL
            + "Usage: create|install|publish [--version] [-h|--help]" + NL);
        output.expectOut(is(""));

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
        output.expectOut(is("Usage: juberblog create|install|publish [--version] [-h|--help]" + NL
            + NL
            + "Commandline tool to manage your blog." + NL
            + NL
            + "Options" + NL
            + NL
            + "  -h, --help          Show help." + NL
            + "      --version       Show version." + NL
            + NL
            + "Example" + NL
            + NL
            + "  TODO Add some examples." + NL
            + NL
            + NL));

        App.main(createApp(new String[]{"-h"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForLongOption() throws Exception {
        output.expectErr(is(""));
        output.expectOut(is("Usage: juberblog create|install|publish [--version] [-h|--help]" + NL
            + NL
            + "Commandline tool to manage your blog." + NL
            + NL
            + "Options" + NL
            + NL
            + "  -h, --help          Show help." + NL
            + "      --version       Show version." + NL
            + NL
            + "Example" + NL
            + NL
            + "  TODO Add some examples." + NL
            + NL
            + NL));

        App.main(createApp(new String[]{"--help"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForCreateSubCommandShortOption() throws Exception {
        output.expectErr(is(""));
        output.expectOut(is("Usage: juberblog create -c|--config <file> [-t|--title <title>] [-d|--draft] [-s|--site]" + NL
            + NL
            + "Creates blog entities (sites/pages).\n\nOptions" + NL
            + NL
            + "  -c, --config        Config file to use." + NL
            + "  -d, --draft         Will mark the file name as draft." + NL
            + "  -h, --help          Show help." + NL
            + "  -s, --site          Will create a site instead of a post." + NL
            + "  -t, --title         Title of the blog post." + NL
            + "  -v, --verbose       Tell you more." + NL
            + NL
            + "Example" + NL
            + NL
            + "  TODO Write examples." + NL
            + NL
            + NL));

        App.main(createApp(new String[]{"create", "-h"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForCreateSubCommandLongOption() throws Exception {
        output.expectErr(is(""));
        output.expectOut(is("Usage: juberblog create -c|--config <file> [-t|--title <title>] [-d|--draft] [-s|--site]" + NL
            + NL
            + "Creates blog entities (sites/pages).\n\nOptions" + NL
            + NL
            + "  -c, --config        Config file to use." + NL
            + "  -d, --draft         Will mark the file name as draft." + NL
            + "  -h, --help          Show help." + NL
            + "  -s, --site          Will create a site instead of a post." + NL
            + "  -t, --title         Title of the blog post." + NL
            + "  -v, --verbose       Tell you more." + NL
            + NL
            + "Example" + NL
            + NL
            + "  TODO Write examples." + NL
            + NL
            + NL));

        App.main(createApp(new String[]{"create", "--help"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForInstallSubCommandShortOption() throws Exception {
        output.expectErr(is(""));
        output.expectOut(is("Usage: juberblog install -l|--location <directory>" + NL
            + NL
            + "Installs a fresh blog." + NL
            + NL
            + "Options" + NL
            + NL
            + "  -f, --force         Force the installation which overwrites exsiting files." + NL
            + "  -h, --help          Show help." + NL
            + "  -l, --location      Where to install the scaffold." + NL
            + "  -u, --update        Updates files which makes backups of exisiting files." + NL
            + "  -v, --verbose       Tell you more.\n"
            + NL
            + "Example" + NL
            + NL
            + "  TODO Write examples." + NL
            + NL
            + NL));

        App.main(createApp(new String[]{"install", "-h"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForInstallSubCommandLongOption() throws Exception {
        output.expectErr(is(""));
        output.expectOut(is("Usage: juberblog install -l|--location <directory>" + NL
            + NL
            + "Installs a fresh blog." + NL
            + NL
            + "Options" + NL
            + NL
            + "  -f, --force         Force the installation which overwrites exsiting files." + NL
            + "  -h, --help          Show help." + NL
            + "  -l, --location      Where to install the scaffold." + NL
            + "  -u, --update        Updates files which makes backups of exisiting files." + NL
            + "  -v, --verbose       Tell you more.\n"
            + NL
            + "Example" + NL
            + NL
            + "  TODO Write examples." + NL
            + NL
            + NL));

        App.main(createApp(new String[]{"install", "--help"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForPublishSubCommandShortOption() throws Exception {
        output.expectErr(is(""));
        output.expectOut(is("Usage: juberblog publish -c|--config <file> [-p|--purge] [-q|--quiet] [-s|--site] [-d|--draft]" + NL
            + NL
            + "Publishes the blog." + NL
            + NL
            + "Options" + NL
            + NL
            + "  -c, --config        Config file to use." + NL
            + "  -d, --draft         Publish drafts." + NL
            + "  -h, --help          Show help." + NL
            + "  -p, --purge         Regenerate all blog posts." + NL
            + "  -q, --quiet         Be quiet and don't post to social networks." + NL
            + "  -s, --site          Generate static sites." + NL
            + "  -v, --verbose       Tell you more." + NL
            + NL
            + "Example" + NL
            + NL
            + "  TODO Write examples." + NL
            + NL
            + NL));

        App.main(createApp(new String[]{"publish", "-h"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void showHelpForPublishSubCommandLongOption() throws Exception {
        output.expectErr(is(""));
        output.expectOut(is("Usage: juberblog publish -c|--config <file> [-p|--purge] [-q|--quiet] [-s|--site] [-d|--draft]" + NL
            + NL
            + "Publishes the blog." + NL
            + NL
            + "Options" + NL
            + NL
            + "  -c, --config        Config file to use." + NL
            + "  -d, --draft         Publish drafts." + NL
            + "  -h, --help          Show help." + NL
            + "  -p, --purge         Regenerate all blog posts." + NL
            + "  -q, --quiet         Be quiet and don't post to social networks." + NL
            + "  -s, --site          Generate static sites." + NL
            + "  -v, --verbose       Tell you more." + NL
            + NL
            + "Example" + NL
            + NL
            + "  TODO Write examples." + NL
            + NL
            + NL));

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
        output.expectErr(is("FATAL: Bad arguments (cause: Unknown option: -x)!" + NL
            + "Usage: publish -c|--config <file> [-p|--purge] [-q|--quiet] [-s|--site] [-d|--draft]" + NL));

        App.main(createApp(new String[]{"publish", "-x"}));

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    public void missingConfig_CREATE() throws Exception {
        output.expectErr(is("FATAL: Bad arguments (cause: The following option is required: -c, --config )!" + NL
            + "Usage: create -c|--config <file> [-t|--title <title>] [-d|--draft] [-s|--site]" + NL));

        final App sut = createApp(new String[]{"create"});

        App.main(sut);

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    public void execute_CREATE() throws Exception {
        final App sut = createApp(new String[]{
            "create",
            "-c", createPath("config.properties").toString()});
        final Factory factory = mock(Factory.class);
        final SubCommand cmd = mock(SubCommand.class);
        when(factory.forName(eq(Command.CREATE), any(JUberblog.class))).thenReturn(cmd);
        sut.injectFactory(factory);

        App.main(sut);

        verify(cmd, times(1)).execute();
        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

    @Test
    public void missingConfig_INSTALL() throws Exception {
        output.expectErr(is("FATAL: Bad arguments (cause: The following option is required: -l, --location )!" + NL
            + "Usage: install -l|--location <directory>" + NL));

        final App sut = createApp(new String[]{
            "install"});

        App.main(sut);

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    public void execute_INSTALL() throws Exception {
        output.expectErr(is(""));

        final App sut = createApp(new String[]{
            "install",
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
    public void missingConfig_PUBLISH() throws Exception {
        output.expectErr(is("FATAL: Bad arguments (cause: The following option is required: -c, --config )!" +NL
            + "Usage: publish -c|--config <file> [-p|--purge] [-q|--quiet] [-s|--site] [-d|--draft]" + NL));

        final App sut = createApp(new String[]{
            "publish"});

        App.main(sut);

        verify(getExiter(), times(1)).exit(ExitCodeImpl.BAD_ARGUMENT);
    }

    @Test
    public void execute_PUBLISH() throws Exception {
        output.expectErr(is(""));

        final App sut = createApp(new String[]{
            "publish",
            "-c", createPath("config.properties").toString()});
        final Factory factory = mock(Factory.class);
        final SubCommand cmd = mock(SubCommand.class);
        when(factory.forName(eq(Command.PUBLISH), any(JUberblog.class))).thenReturn(cmd);
        sut.injectFactory(factory);

        App.main(sut);

        verify(cmd, times(1)).execute();
        verify(getExiter(), times(1)).exit(ExitCodeImpl.OK.getCode());
    }

}
