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

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.core.Options;
import de.weltraumschaf.juberblog.core.SubCommandBase;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class CreateSubCommand  extends SubCommandBase {

    public CreateSubCommand(final Options options, final IO io) {
        super(options, io);
    }

    @Override
    public void execute() {
        io().println("create");
    }
}
