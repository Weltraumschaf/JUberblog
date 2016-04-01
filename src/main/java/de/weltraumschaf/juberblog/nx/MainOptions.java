package de.weltraumschaf.juberblog.nx;

import com.beust.jcommander.Parameter;
import de.weltraumschaf.juberblog.core.Constants;
import de.weltraumschaf.juberblog.nx.Options.Command;

/**
 * Options for the main command.
 *
 * @since 1.0.0
 */
public final class MainOptions extends CommonOptions {

    static final String USAGE = Constants.COMMAND_NAME.toString() + " ["
        + Command.CREATE.toString() + "|"
        + Command.INSTALL.toString() + "|"
        + Command.PUBLISH.toString()
        + "] [--version] [-h|--help]";
    static final String DESCRIPTION = "";
    static final String EXAMPLE = "";

    @Parameter(names = {"--version"}, description = "Show version.")
    private boolean version;

    public boolean isVersion() {
        return version;
    }
}
