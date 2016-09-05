package de.weltraumschaf.juberblog.options;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import de.weltraumschaf.juberblog.options.Options.Command;

/**
 * Options for the create command.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
@Parameters(commandDescription = "Creates blog entities (sites/pages).")
public final class CreateOptions extends CommonCommandOptions implements OptionsWithConfig {

    static final String EXAMPLE = "TODO Write examples.";

    @SuppressWarnings( {"CanBeFinal", "unused"})
    @Parameter(names = {"-c", "--config"}, description = "Config file to use.", required = true)
    private String config;
    @SuppressWarnings( {"CanBeFinal", "unused"})
    @Parameter(names = {"-t", "--title"}, description = "Title of the blog post.")
    private String title;
    @SuppressWarnings( {"CanBeFinal", "unused"})
    @Parameter(names = {"-d", "--draft"}, description = "Will mark the file name as draft.")
    private boolean draft;
    @SuppressWarnings( {"CanBeFinal", "unused"})
    @Parameter(names = {"-s", "--site"}, description = "Will create a site instead of a post.")
    private boolean site;

    @Override
    public String getConfig()  {
        return config;
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

    @SuppressWarnings("StringBufferReplaceableByString")
    public static String usage() {
        return new StringBuilder()
            .append(Command.CREATE)
            .append(' ')
            .append("-c|--config <file> [-t|--title <title>] [-d|--draft] [-s|--site]")
            .append(' ')
            .append(CommonCommandOptions.usage())
            .toString();
    }
}
