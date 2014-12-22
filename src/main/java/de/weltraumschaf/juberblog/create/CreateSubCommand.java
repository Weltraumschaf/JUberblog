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

package de.weltraumschaf.juberblog.create;

import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.juberblog.cmd.SubCommandBase;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class CreateSubCommand  extends SubCommandBase {

    public CreateSubCommand(final JUberblog registry) {
        super(registry);
    }

    @Override
    public void execute() {
        io().println("create");
    }
}
