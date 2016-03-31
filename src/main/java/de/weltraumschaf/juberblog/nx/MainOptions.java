package de.weltraumschaf.juberblog.nx;

import com.beust.jcommander.Parameter;

/**
 * Options for the main command.
 *
 * @since 1.0.0
 */
public final class MainOptions extends CommonOptions {

    @Parameter(names = {"--version"}, description = "Show version.")
    private boolean version;

    public boolean isVersion() {
        return version;
    }
}
