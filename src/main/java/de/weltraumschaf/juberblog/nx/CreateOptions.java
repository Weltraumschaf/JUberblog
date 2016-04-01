package de.weltraumschaf.juberblog.nx;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import de.weltraumschaf.juberblog.core.Constants;
import de.weltraumschaf.juberblog.nx.Options.Command;

/**
 * Options for the create command.
 *
 * @since 1.0.0
 */
@Parameters(commandDescription = "Creates blog entities (sites/pages)")
public final class CreateOptions extends CommonCommandOptions {

    static final String USAGE = Constants.COMMAND_NAME.toString() + " " + Command.CREATE.toString()
        + " -c|--config <file> [-t|--title <title>] [-d|--draft] [-s|--site]";
    static final String DESCRIPTION = "";
    static final String EXAMPLE = "";

    @Parameter(names = {"-c", "--config"}, description = "Config file to use.", required = true)
    private String comfig;
    @Parameter(names = {"-t", "--title"}, description = "Title of the blog post.")
    private String title;
    @Parameter(names = {"-d", "--draft"}, description = "Will mark the file name as draft.")
    private boolean draft;
    @Parameter(names = {"-s", "--site"}, description = "Will create a site instead of a post.")
    private boolean site;

    public String getComfig() {
        return comfig;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDraft() {
        return draft;
    }

    public boolean isSite() {
        return site;
    }

}
