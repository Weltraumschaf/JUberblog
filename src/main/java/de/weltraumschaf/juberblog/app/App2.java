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
import de.weltraumschaf.commons.jcommander.JCommanderImproved;
import de.weltraumschaf.commons.system.ExitCode;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.core.Constants;
import de.weltraumschaf.juberblog.core.ExitCodeImpl;
import de.weltraumschaf.juberblog.core.Options;
import de.weltraumschaf.juberblog.core.SubCommand.SubCommandName;
import java.io.UnsupportedEncodingException;

/**
 * Main class invoked by JVM.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class App2 extends InvokableAdapter {

    private final JCommanderImproved<Options> cliArgs
            = new JCommanderImproved<>(Constants.COMMAND_NAME.toString(), Options.class);
    private final Arguments arguments;

    App2(final String[] args) {
        super(args);
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
        if (arguments.isEmpty()) {
            getIoStreams().errorln("USAGE");
            return;
        }

        if (SubCommandName.isSubCommand(arguments.getFirstArgument())) {
            executeSubCommand();
        } else {
            executeBaseCommand();
        }
    }

    private void executeSubCommand() {
        final SubCommandName cmd = SubCommandName.betterValueOf(arguments.getFirstArgument());
        final Options opt = cliArgs.gatherOptions(arguments.getTailArguments());
    }

    private void executeBaseCommand() {
        final Options opt = cliArgs.gatherOptions(arguments.getAll());

        if (opt.isVersion()) {
            getIoStreams().println("VERSION");
            return;
        }

        if (opt.isHelp()) {
            getIoStreams().println("HELP");
            return;
        }
    }

}