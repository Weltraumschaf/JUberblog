package de.weltraumschaf.juberblog.nx;

import com.beust.jcommander.Parameter;

/**
 */
abstract class CommonCommandOptions extends CommonOptions {

    @Parameter(names = {"-v", "--verbose"}, description = "Tell you more.")
    private boolean verbose;


    public final boolean isVerbose() {
        return verbose;
    }
}
