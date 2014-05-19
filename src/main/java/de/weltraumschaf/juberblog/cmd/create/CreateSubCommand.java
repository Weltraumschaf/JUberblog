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
import de.weltraumschaf.juberblog.cmd.CommonCreateAndPublishSubCommand;
import de.weltraumschaf.juberblog.opt.CreateOptions;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

/**
 * Creates a post or site draft.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CreateSubCommand extends CommonCreateAndPublishSubCommand<CreateOptions>  {

    /**
     * Log facility.
     */
    private static final Logger LOG = Logger.getLogger(CreateSubCommand.class);
    /**
     * Command line options.
     */
    private CreateOptions options;

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
        LOG.debug("create");
    }

    @Override
    public void setOptions(final CreateOptions opt) {
        Validate.notNull(opt, "Options must not be null!");
        options = opt;
    }

    @Override
    public CreateOptions getOptions() {
        return options;
    }

}
