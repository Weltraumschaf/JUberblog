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

import com.beust.jcommander.ParameterException;
import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IOStreams;
import de.weltraumschaf.commons.application.InvokableAdapter;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.system.Environments;
import de.weltraumschaf.commons.system.ExitCode;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.core.Constants;
import de.weltraumschaf.juberblog.core.ExitCodeImpl;
import de.weltraumschaf.juberblog.app.SubCommand.Name;
import java.io.UnsupportedEncodingException;

/**
 * Main class invoked by JVM.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class App extends InvokableAdapter {

    /**
     * To obtain environment variables.
     */
    private final Environments.Env env;
    /**
     * Command line arguments.
     */
    private final Arguments arguments;
    /**
     * Version information.
     */
    private final Version version;
    /**
     * Provides sub commands.
     */
    private SubCommand.Factory subCommands = new SubCommand.FactoryImpl();

    /**
     * Convenience constructor with default environment.
     *
     * @param args must not be {@code null}
     */
    public App(final String[] args) {
        this(args, Environments.defaultEnv());
    }

    /**
     * Dedicated constructor.
     *
     * @param args must not be {@code null}
     * @param env must not be {@code null}
     */
    App(final String[] args, final Environments.Env env) {
        super(args);
        this.version = new Version(Constants.PACKAGE_BASE.toString() + "/version.properties");
        this.env = Validate.notNull(env, "env");
        arguments = new Arguments(args);
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

    @Override
    public void execute() throws Exception {
        version.load();

        if (arguments.isEmpty()) {
            showUsage();
            return;
        }

        if (Name.isSubCommand(arguments.getFirstArgument())) {
            executeSubCommand();
        } else {
            executeMainCommand();
        }
    }

    /**
     * Executes the main application code here.
     */
    private void executeMainCommand() throws ApplicationException {
        try {
            if (!executeBaseCommand(Options.gatherOptions(arguments.getAll()))) {
                showUsage();
            }
        } catch (final ParameterException ex) {
            throw new ApplicationException(ExitCodeImpl.BAD_ARGUMENT, errorMessage("Bad arguments!"), ex);
        }
    }

    /**
     * Executes a sub command named by fist CLI argument.
     *
     * @throws Exception if anything went wrong
     */
    private void executeSubCommand() throws Exception {
        final Options cliOptions;
        try {
            cliOptions = Options.gatherOptions(arguments.getTailArguments());
        } catch (final ParameterException ex) {
            throw new ApplicationException(ExitCodeImpl.BAD_ARGUMENT, errorMessage(ex.getMessage()), ex);
        }

        if (executeBaseCommand(cliOptions)) {
            return;
        }

        final SubCommand cmd
                = subCommands.forName(
                        Name.betterValueOf(arguments.getFirstArgument()),
                        JUberblog.generate(cliOptions, getIoStreams()));
        cmd.execute();
    }

    /**
     * Appends usage to given message.
     *
     * @param msg must not be {@code null} or empty
     * @return never {@code null} or empty
     */
    private String errorMessage(final String msg) {
        return new StringBuilder()
                .append(Validate.notEmpty(msg, "msg"))
                .append(Constants.DEFAULT_NEW_LINE.toString())
                .append(Options.usage())
                .toString();
    }

    /**
     * Executes stuff common for all commands (e.g. show help or version).
     *
     * @param cliOptions must not {@code null}
     * @return whether to exit the application
     */
    private boolean executeBaseCommand(final Options cliOptions) {
        Validate.notNull(cliOptions, "opt");

        if (cliOptions.isVersion()) {
            showVersion();
            return true;
        }

        if (cliOptions.isHelp()) {
            showHelp();
            return true;
        }

        return false;
    }

    /**
     * Show help message.
     */
    private void showHelp() {
        getIoStreams().println(Options.helpMessage());
    }

    /**
     * Show version message.
     */
    private void showVersion() {
        getIoStreams().println(version.getVersion());
    }

    /**
     * Show usage message.
     */
    private void showUsage() {
        getIoStreams().errorln(Options.usage());
    }

    /**
     * Whether debug output is enabled by environment variable.
     *
     * @return {@code true} for enabled, else {@code false}
     */
    boolean isEnvDebug() {
        final String debug = env.get(Constants.ENVIRONMENT_VARIABLE_DEBUG.toString());
        return "true".equalsIgnoreCase(debug.trim());
    }

    /**
     * Injection point for testing.
     *
     * @param subCommands must not be {@code null}
     */
    void injectFactory(final SubCommand.Factory subCommands) {
        this.subCommands = Validate.notNull(subCommands, "subCommands");
    }

}
