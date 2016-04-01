package de.weltraumschaf.juberblog.nx;

import de.weltraumschaf.juberblog.nx.Options.Command;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 */
public final class OptionsTest {

    private final Options sut = new Options();

    @Test
    public void helpOnMainCommand() {
        sut.parse("-h");

        assertThat(sut.getParsedCommand(), is(Command.NONE));
        assertThat(sut.getMain().isHelp(), is(true));
        assertThat(sut.getInstall().isHelp(), is(false));
        assertThat(sut.getCreate().isHelp(), is(false));
        assertThat(sut.getPublish().isHelp(), is(false));
    }

    @Test
    public void helpOnInstallCommand() {
        sut.parse("install", "-h");

        assertThat(sut.getParsedCommand(), is(Command.INSTALL));
        assertThat(sut.getMain().isHelp(), is(false));
        assertThat(sut.getInstall().isHelp(), is(true));
        assertThat(sut.getCreate().isHelp(), is(false));
        assertThat(sut.getPublish().isHelp(), is(false));
    }

    @Test
    public void helpOnCreateCommand() {
        sut.parse("create", "-h");

        assertThat(sut.getParsedCommand(), is(Command.CREATE));
        assertThat(sut.getMain().isHelp(), is(false));
        assertThat(sut.getInstall().isHelp(), is(false));
        assertThat(sut.getCreate().isHelp(), is(true));
        assertThat(sut.getPublish().isHelp(), is(false));
    }

    @Test
    public void helpOnPublishCommand() {
        sut.parse("publish", "-h");

        assertThat(sut.getParsedCommand(), is(Command.PUBLISH));
        assertThat(sut.getMain().isHelp(), is(false));
        assertThat(sut.getInstall().isHelp(), is(false));
        assertThat(sut.getCreate().isHelp(), is(false));
        assertThat(sut.getPublish().isHelp(), is(true));
    }

    @Test
    public void versionOnMainCommand() {
        sut.parse("--version");

        assertThat(sut.getParsedCommand(), is(Command.NONE));
        assertThat(sut.getMain().isVersion(), is(true));
    }

    @Test
    public void installShortOptions() {
        sut.parse("install", "-l", "location", "-v");

        assertThat(sut.getParsedCommand(), is(Command.INSTALL));
        assertThat(sut.getInstall().getLocation(), is("location"));
        assertThat(sut.getInstall().isVerbose(), is(true));
    }

    @Test
    public void installLongOptions() {
        sut.parse("install", "--location", "location", "--verbose");

        assertThat(sut.getParsedCommand(), is(Command.INSTALL));
        assertThat(sut.getInstall().getLocation(), is("location"));
        assertThat(sut.getInstall().isVerbose(), is(true));
    }

    @Test
    public void createShortOptions() {
        sut.parse("create", "-c", "config", "-t", "title", "-d", "-s", "-v");

        assertThat(sut.getParsedCommand(), is(Command.CREATE));
        assertThat(sut.getCreate().getComfig(), is("config"));
        assertThat(sut.getCreate().getTitle(), is("title"));
        assertThat(sut.getCreate().isDraft(), is(true));
        assertThat(sut.getCreate().isSite(), is(true));
        assertThat(sut.getCreate().isVerbose(), is(true));
    }

    @Test
    public void createLongOptions() {
        sut.parse("create", "--config", "config", "--title", "title", "--draft", "--site", "--verbose");

        assertThat(sut.getParsedCommand(), is(Command.CREATE));
        assertThat(sut.getCreate().getComfig(), is("config"));
        assertThat(sut.getCreate().getTitle(), is("title"));
        assertThat(sut.getCreate().isDraft(), is(true));
        assertThat(sut.getCreate().isSite(), is(true));
        assertThat(sut.getCreate().isVerbose(), is(true));
    }

    @Test
    public void publishShortOptions() {
        sut.parse("publish", "-c", "config", "-p", "-q", "-s", "-d", "-v");

        assertThat(sut.getParsedCommand(), is(Command.PUBLISH));
        assertThat(sut.getPublish().getConfig(), is("config"));
        assertThat(sut.getPublish().isPurge(), is(true));
        assertThat(sut.getPublish().isQuiet(), is(true));
        assertThat(sut.getPublish().isSite(), is(true));
        assertThat(sut.getPublish().isDraft(), is(true));
        assertThat(sut.getPublish().isVerbose(), is(true));
    }

    @Test
    public void publishLongOptions() {
        sut.parse("publish", "--config", "config", "--purge", "--quiet", "--site", "--draft", "--verbose");

        assertThat(sut.getParsedCommand(), is(Command.PUBLISH));
        assertThat(sut.getPublish().getConfig(), is("config"));
        assertThat(sut.getPublish().isPurge(), is(true));
        assertThat(sut.getPublish().isQuiet(), is(true));
        assertThat(sut.getPublish().isSite(), is(true));
        assertThat(sut.getPublish().isDraft(), is(true));
        assertThat(sut.getPublish().isVerbose(), is(true));
    }

    @Test
    public void usage() {
        assertThat(
            sut.usage(),
            is("juberblog [create|install|publish] [--version] [-h|--help]"));
    }

    @Test
    public void usage_none() {
        assertThat(
            sut.usage(Command.NONE),
            is("juberblog [create|install|publish] [--version] [-h|--help]"));
    }

    @Test
    public void usage_install() {
        assertThat(
            sut.usage(Command.INSTALL),
            is("juberblog install -l|--location <directory>"));
    }

    @Test
    public void usage_create() {
        assertThat(
            sut.usage(Command.CREATE),
            is("juberblog create -c|--config <file> [-t|--title <title>] [-d|--draft] [-s|--site]"));
    }

    @Test
    public void usage_publish() {
        assertThat(
            sut.usage(Command.PUBLISH),
            is("juberblog publish -c|--config <file> [-p|--purge] [-q|--quiet] [-s|--site] [-d|--draft]"));
    }
}
