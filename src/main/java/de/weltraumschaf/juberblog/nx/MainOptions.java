package de.weltraumschaf.juberblog.nx;

import com.beust.jcommander.Parameter;
import de.weltraumschaf.juberblog.nx.Options.Command;

/**
 * Options for the main command.
 *
 * @since 1.0.0
 */
public final class MainOptions extends CommonOptions {

    static final String USAGE =
        Command.CREATE.toString() + "|"
        + Command.INSTALL.toString() + "|"
        + Command.PUBLISH.toString()
        + " [--version] [-h|--help]";
    static final String DESCRIPTION = "Commandline tool to manage your blog.";
    static final String EXAMPLE = "TODO Add some examples.";

    @Parameter(names = {"--version"}, description = "Show version.")
    private boolean version;

    public boolean isVersion() {
        return version;
    }
}
