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

import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.files.FilenameFilters;
import de.weltraumschaf.juberblog.formatter.Formatters;
import de.weltraumschaf.juberblog.model.DataFile;
import de.weltraumschaf.juberblog.model.Page;
import de.weltraumschaf.juberblog.model.PublishedPages;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import org.apache.log4j.Logger;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class Publisher2 implements Command {

    /**
     * Log facility.
     */
    private static final Logger LOG = Logger.getLogger(Publisher2.class);
    /**
     * Holds information of all published pages.
     */
    private final PublishedPages published = new PublishedPages();
    private final PublisherOptions options;

    Publisher2(final PublisherOptions options) {
        super();
        this.options = Validate.notNull(options, "options");
    }

    @Override
    public void execute() throws PublishingSubCommandExcpetion {
        if (options.isPublishSites()) {
            published.put(publishSites());
        }

        if (options.isPublishPages()) {
            published.put(publisPosts());
        }
    }

    private Collection<Page> publishSites() throws PublishingSubCommandExcpetion {
        LOG.info("Publish sites...");

        try {
            return publishFiles(
                    Formatters.Type.SITE,
                    readSitesData(),
                    options.getDirs().htdocsSites());
        } catch (IOException ex) {
            throw new PublishingSubCommandExcpetion("Can't read sites data files: " + ex.getMessage(), ex);
        }
    }

    /**
     * Get the data for sites to publish.
     *
     * @return never {@code null}
     * @throws IOException if data files can't be read
     */
    Collection<Path> readSitesData() throws IOException {
        final Collection<Path> sitesData = Lists.newArrayList();

        for (final File f : options.getDirs().dataSites().toFile().listFiles(FilenameFilters.findMarkdownFiles())) {
            sitesData.add(f.toPath());
        }

        return sitesData;
    }

    private Collection<Page> publisPosts() throws PublishingSubCommandExcpetion {
        LOG.info("Publish posts...");

        try {
            return publishFiles(
                    Formatters.Type.POST,
                    getPostsData(),
                    options.getDirs().htdocsPosts());
        } catch (IOException ex) {
            throw new PublishingSubCommandExcpetion("Can't read posts data files: " + ex.getMessage(), ex);
        }
    }

    /**
     * Get the data for posts to publish.
     *
     * @return never {@code null}
     * @throws IOException if data files can't be read
     */
    Collection<Path> getPostsData() throws IOException {
        final Collection<Path> postsData = Lists.newArrayList();

        for (final File f : options.getDirs().dataPosts().toFile().listFiles(FilenameFilters.findMarkdownFiles())) {
            postsData.add(f.toPath());
        }

        return postsData;
    }

    /**
     * Publish all files in given directory with given layout.
     *
     * @param type must not be {@literal null}
     * @param data must not be {@literal null}
     * @param outputDir must not be {@literal null}
     * @return never {@code null}
     * @throws PublishingSubCommandExcpetion if can't render template
     */
    private Collection<Page> publishFiles(final Formatters.Type type,
            final Collection<Path> data,
            final Path outputDir)
            throws PublishingSubCommandExcpetion {
        return null;
    }
}
