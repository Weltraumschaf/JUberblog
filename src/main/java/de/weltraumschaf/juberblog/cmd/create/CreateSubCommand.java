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

package de.weltraumschaf.juberblog.cmd.create;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.cmd.CommonCreateAndPublishSubCommand;
import de.weltraumschaf.juberblog.opt.CreateOptions;
import org.apache.log4j.Logger;

/**
 * Creates a post or site optionally as draft.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class CreateSubCommand extends CommonCreateAndPublishSubCommand<CreateOptions>  {

    /**
     * Log facility.
     */
    private static final Logger LOG = Logger.getLogger(CreateSubCommand.class);

    /**
     * Dedicated constructor.
     *
     * @param io must not be {@code null}
     */
    public CreateSubCommand(final IO io) {
        super(io);
    }

    @Override
    public void run() {
        if (getOptions().isSite()) {
            createSite();
        } else {
            createPost();
        }
    }

    private void createSite() {
        final String title = getOptions().getTitle();

        if (getOptions().isDraft()) {
            io.println(String.format("Create site draft '%s'...", title));
        } else {
            io.println(String.format("Create site '%s'...", title));
        }
    }

    private void createPost() {
        final String title = getOptions().getTitle();

        if (getOptions().isDraft()) {
            io.println(String.format("Create post draft '%s'...", title));
        } else {
            io.println(String.format("Create post '%s'...", title));
        }
    }

}
