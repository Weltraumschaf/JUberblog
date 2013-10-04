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

import de.weltraumschaf.commons.IOStreams;
import de.weltraumschaf.juberblog.CliOptions;
import org.apache.log4j.Logger;

/**
 * Installation command.
 *
 * Creates a scaffold directory in specified location.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class Install extends BaseCommand {

    private static final boolean REQUIRE_CONFIGURATION = false;
    /**
     * Log facility.
     */
    private static final Logger LOG = Logger.getLogger(Create.class);

    public Install(final CliOptions options, final IOStreams io) {
        super(options, io, REQUIRE_CONFIGURATION);
    }

    @Override
    protected void run() {
        LOG.debug("install");
    }
}
