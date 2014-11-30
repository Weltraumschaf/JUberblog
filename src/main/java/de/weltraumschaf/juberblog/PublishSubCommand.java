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

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.App.SubCommand;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class PublishSubCommand implements SubCommand {

    private final Options options;
    private final IO io;

    PublishSubCommand(final Options options, final IO io) {
        super();
        this.options = Validate.notNull(options, "options");
        this.io = Validate.notNull(io, "io");
    }

    @Override
    public void execute() {
        io.println("publish");
    }

}
