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
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.commons.time.StopWatch;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.ExitCodeImpl;
import de.weltraumschaf.juberblog.cmd.CommonCreateAndPublishSubCommand;
import de.weltraumschaf.juberblog.model.PublishedPages;
import de.weltraumschaf.juberblog.opt.PublishOptions;
import org.apache.log4j.Logger;

/**
 * Publish all sites/posts from data directory.
 *
 * TODO create index TODO create sitemap.xml TODO create feed xml
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class PublishSubCommand extends CommonCreateAndPublishSubCommand<PublishOptions> {

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
     * Dedicated constructor.
     *
     * @param io must not be {@literal null}
     * @param version must not be {@literal null}
     */
    public PublishSubCommand(final IO io, final Version version) {
        super(io, version);
    }

    @Override
    public void run() throws ApplicationException {
        watch.reset();
        final PublishedPages pages = new PublishedPages();
        final Publisher pub = new Publisher(
            getDirectories(),
            getTemplateConfig(),
            getBlogConfiguration().getBaseUri(),
            pages,
            version());

        pub.setPurge(getOptions().isPurge());
        pub.setSites(getOptions().isSites());
        LOG.info("Start pulishing...");
        watch.start();

        try {
            // TODO Write tests for this stuff here.
            pub.execute();
            updateHomeSite();
            updateSiteMap();
            updateFeed();
            // TODO Save published data in home dir.
        } catch (PublishingSubCommandExcpetion ex) {
            throw new ApplicationException(ExitCodeImpl.FATAL, "Error while publishing files: " + ex.getMessage(), ex);
        }

        watch.stop();
        LOG.info(String.format("Publishing finished! Elapsed time: %s", watch.toString()));
    }

    @Override
    public void setOptions(final PublishOptions opt) {
        options = Validate.notNull(opt, "opt");
    }

    @Override
    public PublishOptions getOptions() {
        return options;
    }

    /**
     * Update the home site.
     */
    private void updateHomeSite() {
        LOG.info("Update home site...");
        new HomeSiteGenerator().execute();
    }

    /**
     * Update the site map.
     */
    private void updateSiteMap() {
        LOG.info("Update site map...");
        new SiteMapGenerator(getTemplateConfig()).execute();
    }

    /**
     * Update the RSS feed.
     */
    private void updateFeed() {
        LOG.info("Update feed...");
//        new FeedGenerator(templateConfig).execute();
    }
}
