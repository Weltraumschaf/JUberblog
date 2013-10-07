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
import de.weltraumschaf.juberblog.opt.Options;

/**
 * SubCommand pattern.
 *
 * @param <T> type of options
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface SubCommand<T extends Options> {

    /**
     * Executes the command.
     *
     * @throws ApplicationException on any executions error
     */
    void execute() throws ApplicationException;
    /**
     * Set CLI options.
     *
     * @param opt must not be {@code null}
     */
    void setOptions(T opt);
    /**
     * Get the CLI options.
     *
     * @return never {@code nul}
     */
    T getOptions();

}
