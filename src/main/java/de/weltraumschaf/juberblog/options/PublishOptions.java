package de.weltraumschaf.juberblog.options;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * Options for the publish command.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
@Parameters(commandDescription = "Publishes the blog.")
public final class PublishOptions extends CommonCommandOptions implements OptionsWithConfig {

    static final String EXAMPLE = "TODO Write examples.";

    @Parameter(names = {"-c", "--config"}, description = "Config file to use.", required = true)
    private String config;
    @Parameter(names = {"-p", "--purge"}, description = "Regenerate all blog posts.")
    private boolean purge;
    @Parameter(names = {"-q", "--quiet"}, description = "Be quiet and don't post to social networks.")
    private boolean quiet;
    @Parameter(names = {"-s", "--site"}, description = "Generate static sites.")
    private boolean site;
    @Parameter(names = {"-d", "--draft"}, description = "Publish drafts.")
    private boolean draft;

    @Override
    public String getConfig() {
        return config;
    }

    public boolean isPurge() {
        return purge;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public boolean isSite() {
        return site;
    }

    public boolean isDraft() {
        return draft;
    }

    public static String usage() {
        return new StringBuilder()
            .append(Options.Command.PUBLISH)
            .append(' ')
            .append("-c|--config <file> [-p|--purge] [-q|--quiet] [-s|--site] [-d|--draft]")
            .append(' ')
            .append(CommonCommandOptions.usage())
            .toString();
    }
}
