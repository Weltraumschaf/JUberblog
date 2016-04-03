package de.weltraumschaf.juberblog.options;

import com.beust.jcommander.Parameter;

/**
 * General available options.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
abstract class CommonOptions {

    @Parameter(names = {"-h", "--help"}, description = "Show help.", help = true)
    private boolean help;

    public final boolean isHelp() {
        return help;
    }

}
