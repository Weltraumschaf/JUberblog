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
package de.weltraumschaf.juberblog.cmd;

import de.weltraumschaf.commons.ApplicationException;
import de.weltraumschaf.commons.IOStreams;
import de.weltraumschaf.juberblog.CliOptions;
import de.weltraumschaf.juberblog.Configuration;
import de.weltraumschaf.juberblog.ExitCodeImpl;
import java.io.IOException;
import org.apache.commons.lang3.Validate;

/**
 * Common base functionality for commands.
 *
 * Implements template method pattern.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
abstract class BaseCommand implements Command {

    /**
     * Command line options.
     */
    protected final CliOptions options;
    /**
     * Used for IO.
     */
    protected final IOStreams io;
    /**
     * Loaded from file.
     */
    private Configuration configuration;
    private boolean requireConfiguration;

    /**
     * Dedicated constructor.
     *
     * @param options must not be {@code null}
     * @param io must not be {@code null}
     */
    public BaseCommand(final CliOptions options, final IOStreams io, final boolean requireConfiguration) {
        super();
        Validate.notNull(options, "Options must not be null!");
        this.options = options;
        Validate.notNull(io, "IO must not be null!");
        this.io = io;
        this.requireConfiguration = requireConfiguration;
    }

    @Override
    public void execute() throws ApplicationException {
        init();
        run();
        deinit();
    }

    /**
     * Command initialization invoked before {@link #run()}.
     */
    private void init() throws ApplicationException {
        loadConfiguration();
    }

    /**
     * Command deinitialization invoked after {@link #run()}.
     */
    private void deinit() {
        // Nothing to do here yet.
    }

    /**
     * Main command method.
     */
    abstract protected void run();

    private void loadConfiguration() throws ApplicationException {
        try {
            configuration = new Configuration(options.getConfigurationFile());
            configuration.load();
        } catch (IOException | IllegalArgumentException | NullPointerException ex) {
            if (requireConfiguration) {
                throw new ApplicationException(
                        ExitCodeImpl.CANT_LOAD_CONFIG,
                        String.format("Can't load configuration file '%s'!", options.getConfigurationFile()),
                        ex);
            }
        }
    }

}
