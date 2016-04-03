package de.weltraumschaf.juberblog.options;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterDescription;
import de.weltraumschaf.commons.jcommander.JCommanderImproved;
import de.weltraumschaf.juberblog.core.Constants;
import java.util.List;
import java.util.Map;

/**
 * Facade to get options.
 *
 * @since 1.0.0
 */
public final class Options {

    private static final String NL = Constants.DEFAULT_NEW_LINE.toString();

    private MainOptions main;
    private InstallOptions install;
    private CreateOptions create;
    private PublishOptions publish;
    private JCommander parser;

    public Options() {
        super();
        reset();
    }

    public void parse(final String... args) {
        reset();
        parser.parse(args);
    }

    public Command getParsedCommand() {
        final String parsedCommand = parser.getParsedCommand();

        if (null == parsedCommand) {
            return Command.NONE;
        }

        return Command.valueOf(parsedCommand.toUpperCase());
    }

    public MainOptions getMain() {
        return main;
    }

    public InstallOptions getInstall() {
        return install;
    }

    public CreateOptions getCreate() {
        return create;
    }

    public PublishOptions getPublish() {
        return publish;
    }

    public String usage() {
        return usage(Command.NONE);
    }

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

    public String help() {
        return help(Command.NONE);
    }

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

    public static enum Command {

        NONE, INSTALL, CREATE, PUBLISH;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

    }
}
