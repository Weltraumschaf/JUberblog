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
import de.weltraumschaf.commons.IO;
import de.weltraumschaf.juberblog.opt.Options;
import org.apache.commons.lang3.Validate;

/**
 * Common base functionality for commands.
 *
 * Implements template method pattern.
 *
 * @param <T> type of options
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
abstract class BaseSubCommand<T extends Options> implements SubCommand<T> {

    /**
     * Used for IO.
     */
    protected final IO io;

    /**
     * Dedicated constructor.
     *
     * @param io must not be {@code null}
     */
    public BaseSubCommand(final IO io) {
        super();
        Validate.notNull(io, "IO must not be null!");
        this.io = io;
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
     */
    protected void deinit() throws ApplicationException {
        // Nothing to do here yet.
    }

    /**
     * Main command method.
     */
    protected abstract void run() throws ApplicationException;

}
