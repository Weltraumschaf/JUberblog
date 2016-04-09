package de.weltraumschaf.juberblog.cmd;

/**
 * Implementations are a subcommand of the main application.
 *
 * @since 1.0.0
 * @author Sven Strittmatter
 */
public interface SubCommand {

    /**
     * Executes the sub command.
     *
     * @throws Exception on any error during execution
     */
    void execute() throws Exception;

}
