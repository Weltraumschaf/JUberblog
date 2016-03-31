package de.weltraumschaf.juberblog.nx;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * Options for the install command.
 *
 * @since 1.0.0
 */
@Parameters(commandDescription = "Installs a fresh blog")
public final class InstallOptions extends CommonCommandOptions {

    @Parameter(names = {"-l", "--location"}, description = "Where to install the scaffold.", required = true)
    private String location;

    public String getLocation() {
        return location;
    }

}
