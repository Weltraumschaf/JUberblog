package de.weltraumschaf.juberblog.options;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * Options for the install command.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
@Parameters(commandDescription = "Installs a fresh blog.")
public final class InstallOptions extends CommonCommandOptions {

    static final String EXAMPLE = "TODO Write examples.";

    @Parameter(names = {"-l", "--location"}, description = "Where to install the scaffold.", required = true)
    private String location = "";
    @Parameter(names = {"-f", "--force"}, description = "Force the installation which overwrites existing files.")
    private boolean force;
    @Parameter(names = {"-u", "--update"}, description = "Updates files which makes backup of existing files.")
    private boolean update;

    public String getLocation() {
        return location;
    }

    public boolean isForce() {
        return force;
    }

    public boolean isUpdate() {
        return update;
    }

    public static String usage() {
        return new StringBuilder()
            .append(Options.Command.INSTALL)
            .append(' ')
            .append("-l|--location <directory> [-f|--force] [-u|--update]")
            .append(' ')
            .append(CommonCommandOptions.usage())
            .toString();
    }
}
