package de.weltraumschaf.juberblog.nx;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * Options for the publish command.
 *
 * @since 1.0.0
 */
@Parameters(commandDescription = "Publishes the blog")
public final class PublishOptions extends CommonCommandOptions {

    @Parameter(names = {"-c", "--config"}, description = "Config file to use.", required = true)
    private String config;
    @Parameter(names = {"-p", "--purge"}, description = "Regenerate all blog posts.")
    private boolean purge;
    @Parameter(names = {"-q", "--quiet"}, description = "Be quiet and don't post to social networks.")
    private boolean quiet;
    @Parameter(names = {"-s", "--sites"}, description = "Generate static sites.")
    private boolean sites;
    @Parameter(names = {"-d", "--drafts"}, description = "Publish drafts.")
    private boolean drafts;

    public String getConfig() {
        return config;
    }

    public boolean isPurge() {
        return purge;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public boolean isSites() {
        return sites;
    }

    public boolean isDrafts() {
        return drafts;
    }

}
