package de.weltraumschaf.juberblog.cmd;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.juberblog.core.BlogConfiguration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Templates;
import de.weltraumschaf.juberblog.options.Options;

/**
 * Implementations are a subcommand of the main application.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public interface SubCommand {

    /**
     * Get the sub command options.
     *
     * @return never {@code null}
     */
    Options options();

    /**
     * Used for CLI IO.
     *
     * @return never {@code null}
     */
    IO io();

    /**
     * Get all important templates.
     *
     * @return never {@code null}
     */
    Templates templates();

    /**
     * Get all important directories.
     *
     * @return never {@code null}
     */
    Directories directories();

    /**
     * Get all important configurations.
     *
     * @return never {@code null}
     */
    BlogConfiguration configuration();

    Version version();

    /**
     * Executes the sub command.
     *
     * @throws Exception on any error during execution
     */
    void execute() throws Exception;

}
