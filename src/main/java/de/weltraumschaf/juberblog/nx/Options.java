package de.weltraumschaf.juberblog.nx;

import com.beust.jcommander.JCommander;

/**
 * @since 1.0.0
 */
public final class Options {

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

    public static enum Command {

        NONE, INSTALL, CREATE, PUBLISH;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

    }
}
