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

import com.beust.jcommander.JCommander;
import de.weltraumschaf.commons.ApplicationException;
import de.weltraumschaf.commons.IOStreams;
import de.weltraumschaf.commons.InvokableAdapter;
import de.weltraumschaf.commons.Version;
import de.weltraumschaf.juberblog.cmd.Command;
import de.weltraumschaf.juberblog.cmd.Commands;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 * Main class.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class App extends InvokableAdapter {

    /**
     * Log facility.
     */
    private static final Logger LOG = Logger.getLogger(App.class);
    /**
     * Version information.
     */
    private final Version version;

    /**
     * Dedicated constructor.
     *
     * @param args must not be {@code null}
     */
    public App(final String[] args) {
        super(args);
        version = new Version("/de/weltraumschaf/juberblog/version.properties");
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
        } catch (UnsupportedEncodingException ex) {
            LOG.fatal("Can't create IO streams!", ex);
            invokable.exit(-1);
        }
    }

    private boolean isDebug() {
        final String debug = System.getenv(Constants.ENVIRONMENT_VARIABLE_DEBUG.toString());

        if (null == debug || debug.isEmpty()) {
            return false;
        }

        if ("true".equalsIgnoreCase(debug.trim())) {
            return true;
        }

        return false;
    }

    @Override
    public void execute() throws Exception {
        version.load();
        registerShutdownHook(new Runnable() {
            @Override
            public void run() {
            }
        });

        final CliOptions options = determineOptions();

        final Command cmd;
        try {
            cmd = Commands.create(options, getIoStreams());

        } catch (final IllegalArgumentException ex) {
            throw new ApplicationException(
                    ExitCodeImpl.UNKNOWN_COMMAND,
                    ex.getMessage(),
                    ex);
        }

        cmd.execute();
    }

    private CliOptions determineOptions() throws ApplicationException {
        String[] args = getArgs();
        String subcommandName = "";
        JCommander optionsParser;

        if (args.length == 0) {
            final StringBuilder errorMessage = new StringBuilder("Too few arguments! ");
            optionsParser = createOptionsParser(new CliOptions(""), args);
            optionsParser.usage(errorMessage);
            throw new ApplicationException(
                    ExitCodeImpl.TOO_FEW_ARGUMENTS,
                    errorMessage.toString(),
                    null);
        }

        subcommandName = args[0];
        args = Arrays.copyOfRange(getArgs(), 1, getArgs().length);

        final CliOptions options = new CliOptions(subcommandName);
        optionsParser = createOptionsParser(options, args);

        if (options.isHelp()) {
            final StringBuilder errorMessage = new StringBuilder();
            optionsParser.usage(errorMessage);
            throw new ApplicationException(
                    ExitCodeImpl.OK,
                    errorMessage.toString(),
                    null);
        }

        return options;
    }

    private JCommander createOptionsParser(final CliOptions options, final String[] args) {
        final JCommander optionsParser = new JCommander(options, args);
        optionsParser.setProgramName("juberblog");
        return optionsParser;
    }
}
