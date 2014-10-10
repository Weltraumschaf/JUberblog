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

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.opt.Options;

/**
 * Common base functionality for commands.
 *
 * Implements template method pattern.
 *
 * @param <T> type of options
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public abstract class BaseSubCommand<T extends Options> implements SubCommand<T> {

    /**
     * Used for IO.
     */
    protected final IO io;
    private final Version version;

    /**
     * Dedicated constructor.
     *
     * @param io must not be {@code null}
     * @param version must not be {@code null}
     */
    public BaseSubCommand(final IO io, final Version version) {
        super();
        this.io = Validate.notNull(io, "io");
        this.version = Validate.notNull(version, "version");
    }

    @Override
    public final void execute() throws ApplicationException {
        Validate.notNull(getOptions(), "Options must nit be null! Invoke #setOptions() before executing.");
        init();
        run();
        deinit();
    }

    /**
     * SubCommand initialization invoked before {@link #run()}.
     *
     * @throws ApplicationException on any executions error
     */
    protected void init() throws ApplicationException {
        // Nothing to do here yet.
    }

    /**
     * SubCommand deinitialization invoked after {@link #run()}.
     *
     * @throws ApplicationException on any executions error
     */
    protected void deinit() throws ApplicationException {
        // Nothing to do here yet.
    }

    /**
     * Main command method.
     *
     * @throws ApplicationException on any executions error
     */
    protected abstract void run() throws ApplicationException;

    protected Version version() {
        return version;
    }
}
