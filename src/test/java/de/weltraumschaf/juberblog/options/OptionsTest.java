package de.weltraumschaf.juberblog.options;

import de.weltraumschaf.juberblog.options.Options.Command;
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
        assertThat(sut.getCreate().getConfig(), is("config"));
        assertThat(sut.getCreate().getTitle(), is("title"));
        assertThat(sut.getCreate().isDraft(), is(true));
        assertThat(sut.getCreate().isSite(), is(true));
        assertThat(sut.getCreate().isVerbose(), is(true));
    }

    @Test
    public void createLongOptions() {
        sut.parse("create", "--config", "config", "--title", "title", "--draft", "--site", "--verbose");

        assertThat(sut.getParsedCommand(), is(Command.CREATE));
        assertThat(sut.getCreate().getConfig(), is("config"));
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
            is("create|install|publish [--version] [-h|--help]"));
    }

    @Test
    public void usage_none() {
        assertThat(
            sut.usage(Command.NONE),
            is(sut.usage()));
    }

    @Test
    public void usage_install() {
        assertThat(
            sut.usage(Command.INSTALL),
            is("install -l|--location <directory> [-f|--force] [-u|--update] [-v|--verbose] [-h|--help]"));
    }

    @Test
    public void usage_create() {
        assertThat(
            sut.usage(Command.CREATE),
            is("create -c|--config <file> [-t|--title <title>] [-d|--draft] [-s|--site] [-v|--verbose] [-h|--help]"));
    }

    @Test
    public void usage_publish() {
        assertThat(
            sut.usage(Command.PUBLISH),
            is("publish -c|--config <file> [-p|--purge] [-q|--quiet] [-s|--site] [-d|--draft] [-v|--verbose] [-h|--help]"));
    }

    @Test
    public void help() {
        assertThat(
            sut.help(),
            is("Usage: juberblog create|install|publish [--version] [-h|--help]\n"
                + "\n"
                + "Commandline tool to manage your blog.\n"
                + "\n"
                + "Options\n"
                + "\n"
                + "  -h, --help          Show help.\n"
                + "      --version       Show version.\n"
                + "\n"
                + "Example\n"
                + "\n"
                + "  TODO Add some examples.\n"
                + "\n"));
    }

    @Test
    public void help_none() {
        assertThat(sut.help(Command.NONE), is(sut.help()));
    }

    @Test
    public void help_install() {
        assertThat(
            sut.help(Command.INSTALL),
            is("Usage: juberblog " + InstallOptions.usage() + "\n"
                + "\n"
                + "Installs a fresh blog.\n"
                + "\n"
                + "Options\n"
                + "\n"
                + "  -f, --force         Force the installation which overwrites exsiting files.\n"
                + "  -h, --help          Show help.\n"
                + "  -l, --location      Where to install the scaffold.\n"
                + "  -u, --update        Updates files which makes backups of exisiting files.\n"
                + "  -v, --verbose       Tell you more.\n"
                + "\n"
                + "Example\n"
                + "\n"
                + "  TODO Write examples.\n"
                + "\n"));
    }

    @Test
    public void help_create() {
        assertThat(
            sut.help(Command.CREATE),
            is("Usage: juberblog " + CreateOptions.usage() + "\n"
                + "\n"
                + "Creates blog entities (sites/pages).\n"
                + "\n"
                + "Options\n"
                + "\n"
                + "  -c, --config        Config file to use.\n"
                + "  -d, --draft         Will mark the file name as draft.\n"
                + "  -h, --help          Show help.\n"
                + "  -s, --site          Will create a site instead of a post.\n"
                + "  -t, --title         Title of the blog post.\n"
                + "  -v, --verbose       Tell you more.\n"
                + "\n"
                + "Example\n"
                + "\n"
                + "  TODO Write examples.\n"
                + "\n"));
    }

    @Test
    public void help_publish() {
        assertThat(
            sut.help(Command.PUBLISH),
            is("Usage: juberblog " + PublishOptions.usage() + "\n"
                + "\n"
                + "Publishes the blog.\n"
                + "\n"
                + "Options\n"
                + "\n"
                + "  -c, --config        Config file to use.\n"
                + "  -d, --draft         Publish drafts.\n"
                + "  -h, --help          Show help.\n"
                + "  -p, --purge         Regenerate all blog posts.\n"
                + "  -q, --quiet         Be quiet and don't post to social networks.\n"
                + "  -s, --site          Generate static sites.\n"
                + "  -v, --verbose       Tell you more.\n"
                + "\n"
                + "Example\n"
                + "\n"
                + "  TODO Write examples.\n"
                + "\n"));
    }
}
