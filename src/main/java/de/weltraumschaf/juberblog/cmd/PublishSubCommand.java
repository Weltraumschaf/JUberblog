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

import de.weltraumschaf.commons.IO;
import de.weltraumschaf.juberblog.opt.CreateAndPublishOptions;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

/**
 * PublishSubCommand all sites from data directory.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class PublishSubCommand extends CreateAndPublishCommand<CreateAndPublishOptions> {

    /**
     * Log facility.
     */
    private static final Logger LOG = Logger.getLogger(CreateSubCommand.class);
    private CreateAndPublishOptions options;

    public PublishSubCommand(final IO io) {
        super(io);
    }

    @Override
    public void run() {
        LOG.debug("pulish");
    }

    @Override
    public void setOptions(final CreateAndPublishOptions opt) {
        Validate.notNull(opt, "Options must not be null!");
        options = opt;
    }

    @Override
    public CreateAndPublishOptions getOptions() {
        return options;
    }

}
