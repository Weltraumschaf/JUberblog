/*
 *  LICENSE
 *
 * "THE BEER-WARE LICENSE" (Revision 43):
 * "Sven Strittmatter" <weltraumschaf@googlemail.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it,
 * you can buy me a non alcohol-free beer in return.
 *
 * Copyright (C) 2012 "Sven Strittmatter" <weltraumschaf@googlemail.com>
 */
package de.weltraumschaf.juberblog.app;

import de.weltraumschaf.juberblog.core.SubCommand;
import de.weltraumschaf.juberblog.core.ExitCodeImpl;
import de.weltraumschaf.juberblog.core.Options;
import de.weltraumschaf.juberblog.core.Constants;
import de.weltraumschaf.juberblog.publish.PublishSubCommand;
import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.application.IOStreams;
import de.weltraumschaf.commons.application.InvokableAdapter;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.jcommander.JCommanderImproved;
import de.weltraumschaf.commons.system.Environments;
import de.weltraumschaf.commons.system.ExitCode;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.core.SubCommand.Name;
import static de.weltraumschaf.juberblog.core.SubCommand.Name.CREATE;
import static de.weltraumschaf.juberblog.core.SubCommand.Name.INSTALL;
import static de.weltraumschaf.juberblog.core.SubCommand.Name.PUBLISH;
import static de.weltraumschaf.juberblog.core.SubCommand.Name.UNKNOWN;
import de.weltraumschaf.juberblog.create.CreateSubCommand;
import de.weltraumschaf.juberblog.install.InstallSubCommand;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Main class invoked by JVM.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class App extends InvokableAdapter {

    private final JCommanderImproved<Options> cliArgs
            = new JCommanderImproved<>(Constants.COMMAND_NAME.toString(), Options.class);
    /**
     * To obtain environment variables.
     */
    private final Environments.Env env;
    /**
     * Version information.
     */
    private final Version version;
    private Options options = new Options();
    private Arguments arguments = new Arguments();

    App(final String[] args) {
        this(args, Environments.defaultEnv());
    }

    /**
     * Dedicated constructor.
     *
     * @param args must not be {@code null}
     */
    App(final String[] args, final Environments.Env env) {
        super(args);
        this.version = new Version(Constants.PACKAGE_BASE.toString() + "/version.properties");
        this.env = Validate.notNull(env, "env");
    }

    /**
     * Handles all not yet catched exceptions in main function.
     *
     * @param invokable must not be {@code null}
     * @param cause must not be {@code null}
     * @param code must not be {@code null}
     * @param prefix must not be {@code null}
     */
    private static void handleFatals(
            final App invokable,
            final Throwable cause,
            final ExitCode code,
            final String prefix) {
        // CHECKSTYLE:OFF
        // At this point we do not have IO streams.
        System.err.print(Validate.notNull(prefix, "prefix"));
        System.err.println(Validate.notNull(cause, "cause").getMessage());

        if (Validate.notNull(invokable, "invokable").isEnvDebug()) {
            cause.printStackTrace(System.err);
        }

        // CHECKSTYLE:ON
        invokable.exit(Validate.notNull(code, "code").getCode());
    }

    /**
     * Main entry point of VM.
     *
     * @param args CLI arguments from VM
     */
    public static void main(final String[] args) {
        final App invokable = new App(args);

        try {
            InvokableAdapter.main(
                    invokable,
                    IOStreams.newDefault(),
                    invokable.isEnvDebug());
        } catch (final UnsupportedEncodingException ex) {
            handleFatals(invokable, ex, ExitCodeImpl.CANT_READ_IO_STREAMS, "Can't create IO streams!\n");
        }
    }

    Options getOptions() {
        return options;
    }

    /**
     * Determine if debug is enabled by environment variable {@link Constants#ENVIRONMENT_VARIABLE_DEBUG}.
     *
     * @return by default {@code false} if environment is not present or false
     */
    boolean isEnvDebug() {
        final String debug = env.get(Constants.ENVIRONMENT_VARIABLE_DEBUG.toString());
        return "true".equalsIgnoreCase(debug.trim());
    }

    @Override
    public void execute() throws Exception {
        /**
         * 1. If args[] empty, then usage.
         * 2. If args[] size 1, then check if subcommand, else basic option
         */

        setup();


        if (options.isVersion()) {
            getIoStreams().println(version.getVersion());
            return;
        }

        if (options.isHelp()) {
            getIoStreams().println(cliArgs.helpMessage(
                    "create|install|publish [-h] [-v] [-d]",
                    "Commandline tool to manage your blog.",
                    "TODO"));
            return;
        }

        executeSubCommand();
    }

    private void setup() throws IOException {
        version.load();
        arguments = new Arguments(getArgs());
        options = cliArgs.gatherOptions(arguments.getAll());
    }

    private void executeSubCommand() throws ApplicationException {
        final SubCommand command = createSubcommand(arguments.getFirstArgument(), options, getIoStreams());
        try {
            command.execute();
        } catch (Exception ex) {
            throw new ApplicationException(ExitCodeImpl.FATAL, ex.getMessage(), ex);
        }
    }

    static SubCommand createSubcommand(final String commandName, final Options options, final IO io) throws ApplicationException {
        switch (Name.betterValueOf(commandName)) {
            case CREATE:
                return new CreateSubCommand(options, io);
            case INSTALL:
                return new InstallSubCommand(options, io);
            case PUBLISH:
                return new PublishSubCommand(options, io);
            case UNKNOWN:
                throw new ApplicationException(
                        ExitCodeImpl.FATAL,
                        String.format("Unknown sub command: '%s'!", commandName));
            default:
                throw new IllegalStateException(
                        "Unhandled sub command type given!This must never happen.Please filea bug.");
        }
    }

}
