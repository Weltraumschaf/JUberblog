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

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.time.StopWatch;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.ExitCodeImpl;
import de.weltraumschaf.juberblog.cmd.CommonCreateAndPublishSubCommand;
import de.weltraumschaf.juberblog.opt.PublishOptions;
import de.weltraumschaf.juberblog.template.Configurations;
import freemarker.template.Configuration;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.log4j.Logger;

/**
 * Publish all sites/posts from data directory.
 *
 * TODO create index
 * TODO create sitemap.xml
 * TODO create feed xml
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final  class PublishSubCommand extends CommonCreateAndPublishSubCommand<PublishOptions> {

    /**
     * Log facility.
     */
    private static final Logger LOG = Logger.getLogger(PublishSubCommand.class);
    /**
     * USed to measure publish time.
     */
    private final StopWatch watch = new StopWatch();
    /**
     * Command line options.
     */
    private PublishOptions options;
    /**
     * Template configuration.
     */
    private Configuration templateConfig;

    /**
     * Dedicated constructor.
     *
     * @param io must not be {@literal null}
     */
    public PublishSubCommand(final IO io) {
        super(io);
    }

    @Override
    protected void init() throws ApplicationException {
        super.init();
        try {
            templateConfig = Configurations.forProduction(getDirectories().templates().toString());
        } catch (final IOException | URISyntaxException ex) {
            throw new ApplicationException(ExitCodeImpl.FATAL, "Can't configure templates!", ex);
        }
    }

    @Override
    public void run() throws ApplicationException {
        watch.reset();
        final Publisher pub = new Publisher(getDirectories(), templateConfig, getBlogConfiguration().getBaseUri());
        pub.setPurge(getOptions().isPurge());
        pub.setSites(getOptions().isSites());
        LOG.info("Start pulishing...");
        watch.start();

        try {
            pub.execute();
        } catch (PublishingSubCommandExcpetion ex) {
            throw new ApplicationException(ExitCodeImpl.FATAL, "Error while publishing files: " + ex.getMessage(), ex);
        }

        watch.stop();
        LOG.info(String.format("Publishing finished! Elapsed time: %s", watch.toString()));
    }

    @Override
    public void setOptions(final PublishOptions opt) {
        Validate.notNull(opt, "Options must not be null!");
        options = opt;
    }

    @Override
    public PublishOptions getOptions() {
        return options;
    }

}
