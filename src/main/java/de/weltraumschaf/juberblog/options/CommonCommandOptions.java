package de.weltraumschaf.juberblog.options;

import com.beust.jcommander.Parameter;

/**
 * General options available for all commands.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
abstract class CommonCommandOptions extends CommonOptions {

    @Parameter(names = {"-v", "--verbose"}, description = "Tell you more.")
    private boolean verbose;


    public final boolean isVerbose() {
        return verbose;
    }
}
