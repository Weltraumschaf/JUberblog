package de.weltraumschaf.juberblog.options;

import com.beust.jcommander.Parameter;

/**
 * General options available for all commands.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
abstract class CommonCommandOptions extends CommonOptions {

    @SuppressWarnings( {"CanBeFinal", "unused"})
    @Parameter(names = {"-v", "--verbose"}, description = "Tell you more.")
    private boolean verbose;

    public final boolean isVerbose() {
        return verbose;
    }

    static String usage() {
        return new StringBuilder()
            .append("[-v|--verbose]")
            .append(' ')
            .append(CommonOptions.usage())
            .toString();
    }
}
