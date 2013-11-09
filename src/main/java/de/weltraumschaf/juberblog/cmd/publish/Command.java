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
package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.commons.ApplicationException;

/**
 * Implementors have uniform API to be executed.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
interface Command {

    /**
     * Executes the command.
     *
     * @throws PublishingSubCommandExcpetion if anything goes wrong.
     */
    void execute() throws PublishingSubCommandExcpetion;

}
