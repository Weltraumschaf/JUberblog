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

import de.weltraumschaf.commons.application.IOStreams;
import de.weltraumschaf.commons.application.InvokableAdapter;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.jcommander.JCommanderImproved;
import de.weltraumschaf.commons.system.ExitCode;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.core.Constants;
import de.weltraumschaf.juberblog.core.ExitCodeImpl;
import de.weltraumschaf.juberblog.core.Options;
import de.weltraumschaf.juberblog.core.SubCommand;
import de.weltraumschaf.juberblog.core.SubCommand.Name;
import java.io.UnsupportedEncodingException;

/**
 * Main class invoked by JVM.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class App2 extends InvokableAdapter {

    private static final String USAGE = "create|install|publish [-h] [-v]";
    private static final String DESCRIPTION = "Commandline tool to manage your blog.";
    private static final String EXAMPLE = "TODO";

    /**
     * Command line options parser.
     */
    private final JCommanderImproved<Options> optionsProvider
            = new JCommanderImproved<>(Constants.COMMAND_NAME.toString(), Options.class);
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
    private SubCommand.Factory subCommands = new SubCommand.Factory();

    /**
     * Dedicated constructor.
     *
     * @param args must not be {@code null}
     */
    App2(final String[] args) {
        super(args);
        this.version = new Version(Constants.PACKAGE_BASE.toString() + "/version.properties");
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
            getIoStreams().errorln(String.format("Usage: %s %s", Constants.COMMAND_NAME.toString(), USAGE));
            return;
        }

        if (Name.isSubCommand(arguments.getFirstArgument())) {
            executeSubCommand();
        } else {
            executeBaseCommand();
        }
    }

    private void executeSubCommand() {
        final Options opt = optionsProvider.gatherOptions(arguments.getTailArguments());

        if (opt.isVersion()) {
            showVersion();
            return;
        }

        if (opt.isHelp()) {
            showHelp();
            return;
        }

        final Name cmd = Name.betterValueOf(arguments.getFirstArgument());

    }

    private void executeBaseCommand() {
        final Options opt = optionsProvider.gatherOptions(arguments.getAll());

        if (opt.isVersion()) {
            showVersion();
            return;
        }

        if (opt.isHelp()) {
            showHelp();
        }
    }

    private void showHelp() {
        getIoStreams().println(optionsProvider.helpMessage(USAGE, DESCRIPTION, EXAMPLE));
    }

    private void showVersion() {
        getIoStreams().println(version.getVersion());
    }


}
