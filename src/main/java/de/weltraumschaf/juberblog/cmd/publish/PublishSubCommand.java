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
import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.ExitCodeImpl;
import de.weltraumschaf.juberblog.cmd.CommonCreateAndPublishSubCommand;
import de.weltraumschaf.juberblog.model.PublishedPages;
import de.weltraumschaf.juberblog.opt.PublishOptions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

/**
 * Publish all sites/posts from data directory.
 *
 * TODO create index sites (home, sites, posts)
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
                blogConfiguration().getBaseUri(),
                pages,
                version(),
                blogConfiguration()
        );

        pub.setPurge(getOptions().isPurge());
        pub.setSites(getOptions().isSites());
        LOG.info("Start pulishing...");
        watch.start();

        try {
            // TODO Write tests for this stuff here.
            pub.execute();
            updateIndexSites(pages);
            updateSiteMap(pages);
            updateFeed(pages);
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
     *
     * FIXME Fix index sites update.
     *
     * @param pages must not be {@code null}
     */
    private void updateIndexSites(final PublishedPages pages) {
        LOG.info("Update home site...");
        new HomeSiteGenerator(pages).execute();
    }

    /**
     * Update the site map.
     *
     * @param pages must not be {@code null}
     * @throws PublishingSubCommandExcpetion if site map can't be updated
     */
    private void updateSiteMap(final PublishedPages pages) throws PublishingSubCommandExcpetion {
        LOG.info("Update site map...");

        final String filename = "sitemap.xml";
        final SiteMapGenerator generator = new SiteMapGenerator(getTemplateConfig(), pages);
        generator.execute();

        final String xml = generator.getResult();
        final Path target = getDirectories().htdocs().resolve(filename);

        if (!Files.exists(target)) {
            try {
                Files.createFile(target);
                LOG.info(String.format("File '%s' created.", target));
            } catch (final IOException ex) {
                throw new PublishingSubCommandExcpetion(String.format("Can't create feed file '%s'!", target), ex);
            }
        } else {
            // TODO delete file if purge.
        }

        try {
            Files.write(
                    target,
                    xml.getBytes(Constants.DEFAULT_ENCODING.toString()),
                    StandardOpenOption.WRITE);
            LOG.info(String.format("Fle '%s' written.", target));
        } catch (IOException ex) {
            throw new PublishingSubCommandExcpetion(String.format("Can't write to feed file '%s'!", target), ex);
        }
    }

    /**
     * Update the RSS feed.
     *
     * @param pages must not be {@code null}
     * @throws PublishingSubCommandExcpetion if feed can't be updated
     */
    private void updateFeed(final PublishedPages pages) throws PublishingSubCommandExcpetion {
        LOG.info("Update feed...");

        final String filename = "feed.xml";
        final FeedGenerator generator = new FeedGenerator(getTemplateConfig(), pages);

        generator.setDescription(blogConfiguration().getDescription());
        generator.setLanguage(blogConfiguration().getLanguage());
        generator.setLastBuildDate(new DateTime());
        generator.setLink(blogConfiguration().getBaseUri() + filename);
        generator.setTitle(blogConfiguration().getHeadline());
        generator.execute();

        final String xml = generator.getResult();
        final Path target = getDirectories().htdocs().resolve(filename);

        if (!Files.exists(target)) {
            try {
                Files.createFile(target);
                LOG.info(String.format("File '%s' created.", target));
            } catch (final IOException ex) {
                throw new PublishingSubCommandExcpetion(String.format("Can't create feed file '%s'!", target), ex);
            }
        } else {
            // TODO delete file if purge.
        }

        try {
            Files.write(
                    target,
                    xml.getBytes(Constants.DEFAULT_ENCODING.toString()),
                    StandardOpenOption.WRITE);
            LOG.info(String.format("Fle '%s' written.", target));
        } catch (IOException ex) {
            throw new PublishingSubCommandExcpetion(String.format("Can't write to feed file '%s'!", target), ex);
        }
    }
}
