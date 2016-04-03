package de.weltraumschaf.juberblog.options;

import com.beust.jcommander.Parameter;

/**
 * General options available for all commands.
 */
abstract class CommonCommandOptions extends CommonOptions {

    @Parameter(names = {"-v", "--verbose"}, description = "Tell you more.")
    private boolean verbose;


    public final boolean isVerbose() {
        return verbose;
    }
}
