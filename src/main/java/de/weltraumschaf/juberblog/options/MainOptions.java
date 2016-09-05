package de.weltraumschaf.juberblog.options;

import com.beust.jcommander.Parameter;
import de.weltraumschaf.juberblog.options.Options.Command;

/**
 * Options for the main command.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public final class MainOptions extends CommonOptions {

    static final String DESCRIPTION = "Commandline tool to manage your blog.";
    static final String EXAMPLE = "TODO Add some examples.";

    @SuppressWarnings( {"CanBeFinal", "unused"})
    @Parameter(names = {"--version"}, description = "Show version.")
    private boolean version;

    public boolean isVersion() {
        return version;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    public static String usage() {
        return new StringBuilder()
            .append(Command.CREATE)
            .append('|')
            .append(Command.INSTALL)
            .append('|')
            .append(Command.PUBLISH)
            .append(' ')
            .append("[--version]")
            .append(' ')
            .append(CommonOptions.usage())
            .toString();
    }
}
