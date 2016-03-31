package de.weltraumschaf.juberblog.nx;

import com.beust.jcommander.Parameter;

/**
 * @since 1.0.0
 */
abstract class CommonOptions {

    @Parameter(names = {"-h", "--help"}, description = "Show help.", help = true)
    private boolean help;

    public final boolean isHelp() {
        return help;
    }

}
