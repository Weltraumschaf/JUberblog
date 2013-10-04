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
import de.weltraumschaf.commons.InvokableAdapter;
import de.weltraumschaf.commons.Version;
import de.weltraumschaf.juberblog.cmd.Command;
import de.weltraumschaf.juberblog.cmd.Commands;
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
        InvokableAdapter.main(new App(args));
    }

    @Override
    public void execute() throws Exception {
        version.load();
        registerShutdownHook(new Runnable() {
            @Override
            public void run() {
            }
        });

        final String commandName = getArgs().length > 0
                ? getArgs()[0]
                : "";
        final CliOptions options = new CliOptions();
        final String[] args = getArgs().length > 1
                ? Arrays.copyOfRange(getArgs(), 1, getArgs().length)
                : new String[]{};
        final JCommander optionsParser = new JCommander(options, args);
        optionsParser.setProgramName("juberblog");

        if (getArgs().length < 1) {
            final StringBuilder errorMessage = new StringBuilder("Too few arguments! ");
            optionsParser.usage(errorMessage);
            throw new ApplicationException(
                    ExitCodeImpl.TOO_FEW_ARGUMENTS,
                    errorMessage.toString(),
                    null);
        }

        final Command cmd;
        try {
            cmd = Commands.create(commandName, options, getIoStreams());

        } catch (final IllegalArgumentException ex) {
            throw new ApplicationException(
                    ExitCodeImpl.UNKNOWN_COMMAND,
                    ex.getMessage(),
                    ex);
        }

        cmd.execute();
    }
}
