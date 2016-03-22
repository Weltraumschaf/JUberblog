package de.weltraumschaf.juberblog.app;

import de.weltraumschaf.juberblog.cmd.SubCommand;
import com.beust.jcommander.Parameter;
import de.weltraumschaf.commons.jcommander.JCommanderImproved;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.core.Constants;
import java.util.Objects;

/**
 * The command line options of the application.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class Options {

    /**
     * New line string.
     */
    public static final String NL = Constants.DEFAULT_NEW_LINE.toString();

    /**
     * Command line usage.
     */
    private static final String USAGE
            = SubCommand.Name.CREATE.name().toLowerCase() + "|"
            + SubCommand.Name.INSTALL.name().toLowerCase() + "|"
            + SubCommand.Name.PUBLISH.name().toLowerCase()
            + " -l <dir> [-c <file>] [-h] [-v]";
    /**
     * Help description.
     */
    private static final String DESCRIPTION = "Commandline tool to manage your blog.";
    /**
     * Help example.
     */
    private static final String EXAMPLE
            = "Publish the whole blog:" + NL
            + "    " + Constants.COMMAND_NAME.toString() + " " + SubCommand.Name.PUBLISH.name().toLowerCase()
            + " -l path/to/your/blog -c path/to/your/config.properties";

    /**
     * Command line options parser.
     */
    private static final JCommanderImproved<Options> PROVIDER
            = new JCommanderImproved<>(Constants.COMMAND_NAME.toString(), Options.class);

    /**
     * Option if help is wanted.
     */
    @Parameter(
            names = {"-h", "--help"},
            description = "Show this help.")
    private boolean help;
    /**
     * Option if version is wanted.
     */
    @Parameter(
            names = {"-v", "--version"},
            description = "Show the version.")
    private boolean version;
    /**
     * Whether to force installation.
     */
    @Parameter(
            names = {"-f", "--force"},
            description = "Forced instsallation will install into non empty direcotry and overwrites existing files.")
    private boolean force;
    /**
     * Whether to update installation.
     */
    @Parameter(
            names = {"-u", "--update"},
            description = "Updates an installation. Already existing files will be backed up.")
    private boolean update;
    /**
     * Verbose flag.
     */
    @Parameter(names = {"--verbose"}, description = "Tell you more.")
    private boolean verbose;
    /**
     * Draft flag.
     */
    @Parameter(names = {"-d", "--draft" }, description = "Create site/post as draft.")
    private boolean draft;

    /**
     * Site flag.
     */
    @Parameter(names = {"-s", "--site" }, description = "Create site.")
    private boolean site;
    /**
     * Where is the blog installed.
     */
    @Parameter(
            names = {"-l", "--location"},
            description = "Location of the blog installation.")
    private String location;
    /**
     * Configuration file argument.
     */
    @Parameter(
            names = {"-c", "--config"},
            description = "Config file to use.")
    private String configurationFile;

    /**
     * convenience constructor for empty object.
     */
    public Options() {
        this("", "");
    }

    /**
     * Dedicated constructor.
     *
     * @param location must not be {@code null}
     * @param configurationFile must not be {@code null}
     */
    public Options(final String location, final String configurationFile) {
        super();
        this.location = Validate.notNull(location, "location");
        this.configurationFile = Validate.notNull(configurationFile, "configurationFile");
    }

    /**
     * Convenience method to gather the CLI options.
     *
     * @param args must not be {@code null}
     * @return never {@code null}
     */
    public static Options gatherOptions(final String[] args) {
        return PROVIDER.gatherOptions(args);
    }

    /**
     * Convenience method to get the help message.
     *
     * @return never {@code null} or empty
     */
    public static String helpMessage() {
        return PROVIDER.helpMessage(USAGE, DESCRIPTION, EXAMPLE);
    }

    /**
     * Convenience method to get the usage.
     *
     * @return never {@code null} or empty
     */
    public static String usage() {
        return String.format("Usage: %s %s", Constants.COMMAND_NAME.toString(), USAGE);
    }

    /**
     * Whether to display the help message.
     *
     * @return {@code true} for show, else {@code false}
     */
    public boolean isHelp() {
        return help;
    }

    /**
     * Whether to display the version message.
     *
     * @return {@code true} for show, else {@code false}
     */
    public boolean isVersion() {
        return version;
    }

    /**
     * Whether to force installation.
     *
     * @return {@code true} for force, else {@code false}
     */
    public boolean isForce() {
        return force;
    }

    /**
     * Whether to update installation.
     *
     * @return {@code true} for update, else {@code false}
     */
    public boolean isUpdate() {
        return update;
    }

    /**
     * Whether to display verbose messages.
     *
     * @return {@code true} for verbose output, else {@code false}
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * Whether to create a draft.
     *
     * @return {@code true} for draft, {@code false} for not
     */
    public boolean isDraft() {
        return draft;
    }

    /**
     * Whether to create a site or a post.
     *
     * @return {@code true} for site, {@code false} for post
     */
    public boolean isSite() {
        return site;
    }

    /**
     * Get the location option.
     *
     * @return never {@code null}
     */
    public String getLocation() {
        return location;
    }

    /**
     * Get the configuration file option.
     *
     * @return never {@code null}
     */
    public String getConfigurationFile() {
        return configurationFile;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                configurationFile,
                help,
                location,
                force,
                update,
                verbose,
                version,
                draft,
                site);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Options)) {
            return false;
        }

        final Options other = (Options) obj;
        return Objects.equals(configurationFile, other.configurationFile)
                && Objects.equals(help, other.help)
                && Objects.equals(location, other.location)
                && Objects.equals(force, other.force)
                && Objects.equals(update, other.update)
                && Objects.equals(verbose, other.verbose)
                && Objects.equals(version, other.version)
                && Objects.equals(draft, other.draft)
                && Objects.equals(site, other.site);
    }

}
