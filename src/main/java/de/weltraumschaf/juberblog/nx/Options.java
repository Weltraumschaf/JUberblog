package de.weltraumschaf.juberblog.nx;

import com.beust.jcommander.JCommander;
import de.weltraumschaf.juberblog.core.Constants;

/**
 * Facade to get options.
 *
 * @since 1.0.0
 */
public final class Options {

    private static final String NL = Constants.DEFAULT_NEW_LINE.toString();

    private final MainOptions main = new MainOptions();
    private final InstallOptions install = new InstallOptions();
    private final CreateOptions create = new CreateOptions();
    private final PublishOptions publish = new PublishOptions();
    private final JCommander parser = new JCommander(main);

    public Options() {
        super();
        parser.addCommand(Command.INSTALL.toString(), install);
        parser.addCommand(Command.CREATE.toString(), create);
        parser.addCommand(Command.PUBLISH.toString(), publish);
    }

    public void parse(final String... args) {
        parser.parse(args);
    }

    public Command getParsedCommand() {
        final String parsedCommand = parser.getParsedCommand();

        if (null == parsedCommand ) {
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

    public static enum Command {

        NONE, INSTALL, CREATE, PUBLISH;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

    }
}
