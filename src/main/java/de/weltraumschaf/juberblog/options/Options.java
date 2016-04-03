package de.weltraumschaf.juberblog.options;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterDescription;
import de.weltraumschaf.commons.jcommander.JCommanderImproved;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.core.Constants;
import java.util.List;

/**
 * Facade to get CLI options.
 *
 * @since 1.0.0
 */
public final class Options {

    /**
     * Holds the main program options.
     */
    private MainOptions main;
    /**
     * Holds the install command options.
     */
    private InstallOptions install;
    /**
     * Holds the create command options.
     */
    private CreateOptions create;
    /**
     * Holds the publish command options.
     */
    private PublishOptions publish;
    /**
     * Holds the argument parser.
     */
    private JCommander parser;

    /**
     * Dedicated constructor.
     */
    public Options() {
        super();
        reset();
    }

    /**
     * Parse the CLI arguments.
     * <p>
     * This method {@link #reset() reset} the instance.
     * </p>
     *
     * @param args must not be {@code null}
     */
    public void parse(final String... args) {
        reset();
        parser.parse(Validate.notNull(args, "args"));
    }

    /**
     * Determine which command was parsed.
     *
     * @return never {@code null}
     */
    public Command getParsedCommand() {
        final String parsedCommand = parser.getParsedCommand();

        if (null == parsedCommand) {
            return Command.NONE;
        }

        return Command.valueOf(parsedCommand.toUpperCase());
    }

    /**
     * Get the main program options.
     *
     * @return never {@code null}
     */
    public MainOptions getMain() {
        return main;
    }

    /**
     * Get the install command options.
     *
     * @return never {@code null}
     */
    public InstallOptions getInstall() {
        return install;
    }

    /**
     * Get the create command options.
     *
     * @return never {@code null}
     */
    public CreateOptions getCreate() {
        return create;
    }

    /**
     * Get the publish command options.
     *
     * @return never {@code null}
     */
    public PublishOptions getPublish() {
        return publish;
    }

    /**
     * Get the main usage.
     * <p>
     * Shorthand for {@link #usage(de.weltraumschaf.juberblog.options.Options.Command)} with {@link Command#NONE}.
     * </p>
     *
     * @return never {@code null} or empty
     */
    public String usage() {
        return usage(Command.NONE);
    }

    /**
     * Get usage for command.
     *
     * @param cmd may be {@code null}
     * @return never {@code null} or empty
     */
    public String usage(final Command cmd) {
        switch (cmd) {
            case INSTALL:
                return InstallOptions.USAGE;
            case CREATE:
                return CreateOptions.USAGE;
            case PUBLISH:
                return PublishOptions.USAGE;
            case NONE:
            default:
                return MainOptions.USAGE;
        }
    }

    /**
     * Get the main help.
     * <p>
     * Shorthand for {@link #help(de.weltraumschaf.juberblog.options.Options.Command)} with {@link Command#NONE}.
     * </p>
     *
     * @return never {@code null} or empty
     */
    public String help() {
        return help(Command.NONE);
    }

    /**
     * Get help for command.
     *
     * @param cmd may be {@code null}
     * @return never {@code null} or empty
     */
    public String help(final Command cmd) {
        final String usage;
        final String description;
        final String example;
        final List<ParameterDescription> parameters;

        switch (cmd) {
            case INSTALL:
                usage = InstallOptions.USAGE;
                description = parser.getCommandDescription(cmd.toString());
                example = InstallOptions.EXAMPLE;
                parameters = parser.getCommands().get(cmd.toString()).getParameters();
                break;
            case CREATE:
                usage = CreateOptions.USAGE;
                description = parser.getCommandDescription(cmd.toString());
                example = CreateOptions.EXAMPLE;
                parameters = parser.getCommands().get(cmd.toString()).getParameters();
                break;
            case PUBLISH:
                usage = PublishOptions.USAGE;
                description = parser.getCommandDescription(cmd.toString());
                example = PublishOptions.EXAMPLE;
                parameters = parser.getCommands().get(cmd.toString()).getParameters();
                break;
            case NONE:
            default:
                usage = MainOptions.USAGE;
                description = MainOptions.DESCRIPTION;
                example = MainOptions.EXAMPLE;
                parameters = parser.getParameters();
                break;
        }

        return JCommanderImproved.helpMessage(
            usage,
            description,
            example,
            Constants.COMMAND_NAME.toString(),
            parameters);
    }

    /**
     * Resets the instance because it holds state from parsing.
     */
    private void reset() {
        main = new MainOptions();
        install = new InstallOptions();
        create = new CreateOptions();
        publish = new PublishOptions();
        parser = new JCommander(main);
        parser.addCommand(Command.INSTALL.toString(), install);
        parser.addCommand(Command.CREATE.toString(), create);
        parser.addCommand(Command.PUBLISH.toString(), publish);
    }

    /**
     * Available commands.
     */
    public static enum Command {
        /**
         * Dummy for no command, but main program.
         */
        NONE,
        /**
         * Install command.
         */
        INSTALL,
        /**
         * Create command.
         */
        CREATE,
        /**
         * Publish command.
         */
        PUBLISH;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

    }
}
