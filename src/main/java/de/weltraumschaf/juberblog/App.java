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
package de.weltraumschaf.juberblog;

import com.beust.jcommander.Parameter;
import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IOStreams;
import de.weltraumschaf.commons.application.InvokableAdapter;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.jcommander.JCommanderImproved;
import de.weltraumschaf.commons.system.Environments;
import de.weltraumschaf.commons.system.ExitCode;
import de.weltraumschaf.commons.validate.Validate;
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
    private final Environments.Env env = Environments.defaultEnv();
    /**
     * Version information.
     */
    private final Version version;
    private Options options = new Options();
    private Arguments arguments = new Arguments();

    /**
     * Dedicated constructor.
     *
     * @param args must not be {@code null}
     */
    public App(final String[] args) {
        super(args);
        version = new Version(Constants.PACKAGE_BASE.toString() + "/version.properties");
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

        if (Validate.notNull(invokable, "invokable").isDebug()) {
            cause.printStackTrace(System.err);
        }

        // CHECKSTYLE:ON
        invokable.exit(Validate.notNull(code, "code").getCode());
    }

    /**
     * Main entry point of VM.
     *
     * @param args cli arguments from VM
     */
    public static void main(final String[] args) {
        final App invokable = new App(args);

        try {
            InvokableAdapter.main(invokable, IOStreams.newDefault(), invokable.isDebug());
        } catch (final UnsupportedEncodingException ex) {
            handleFatals(invokable, ex, ExitCodeImpl.CANT_READ_IO_STREAMS, "Can't create IO streams!\n");
        }
    }

    /**
     * Determine if debug is enabled by environment variable {@link Constants#ENVIRONMENT_VARIABLE_DEBUG}.
     *
     * @return by default {@code false} if environment is not present or false
     */
    private boolean isDebug() {
        final String debug = env.get(Constants.ENVIRONMENT_VARIABLE_DEBUG.toString());
        return options.isDebug() || "true".equalsIgnoreCase(debug.trim());
    }

    @Override
    public void execute() throws Exception {
        setup();

        if (options.isVersion()) {
            getIoStreams().println(version.getVersion());
            return;
        }

        if (options.isHelp()) {
            getIoStreams().println(cliArgs.helpMessage(
                    "publish [-h] [-v] [-d]",
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
        final SubCommand command;

        switch (SubCommandName.betterValueOf(arguments.getFirstArgument())) {
            case PUBLISH:
                command = new PublishSubCommand(options, getIoStreams());
                break;
            case UNKNOWN:
                throw new ApplicationException(
                        ExitCodeImpl.FATAL,
                        String.format("Unknown sub command: '%s'!", arguments.getFirstArgument()));
            default:
                throw new IllegalStateException(
                        "Unhandled sub command type given!This must never happen.Please filea bug.");
        }

        command.execute();
    }

    private enum SubCommandName {

        PUBLISH, UNKNOWN;

        static SubCommandName betterValueOf(final String name) {
            try {
                return valueOf(name.toUpperCase());
            } catch(final IllegalArgumentException ex){
                return UNKNOWN;
            }
        }
    }

    interface SubCommand {
        void execute();
    }

}
