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

import de.weltraumschaf.commons.ApplicationException;
import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.Directories;
import de.weltraumschaf.juberblog.ExitCodeImpl;
import de.weltraumschaf.juberblog.files.FilenameFilters;
import de.weltraumschaf.juberblog.formatter.Formatters;
import de.weltraumschaf.juberblog.model.DataFile;
import de.weltraumschaf.juberblog.model.Page;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class Publisher implements Command {

    /**
     * Log facility.
     */
    private static final Logger LOG = Logger.getLogger(Publisher.class);
    private final Directories dirs;
    private final Configuration templateConfig;
    private final String baseUri;
    private boolean sites;
    private boolean purge;
    private boolean dataRead;
    private Collection<DataFile> sitesData;
    private Collection<DataFile> postsData;

    public Publisher(final Directories dirs, final Configuration templateConfig, final String baseUri) {
        super();
        this.dirs = dirs;
        this.templateConfig = templateConfig;
        this.baseUri = baseUri;
    }

    void readData() {
        List<DataFile> files = Lists.newArrayList();

        for (final File f : dirs.dataSites().toFile().listFiles(FilenameFilters.findMarkdownFiles())) {
            files.add(new DataFile(f));
        }

        sitesData = files;
        files = Lists.newArrayList();

        for (final File f : dirs.dataPosts().toFile().listFiles(FilenameFilters.findMarkdownFiles())) {
            files.add(new DataFile(f));
        }

        postsData = files;
        dataRead = true;
    }

    Collection<DataFile> getSitesData() {
        if (!dataRead) {
            throw new IllegalStateException("Data never read! Invoke #readData() at least once.");
        }

        return sitesData;
    }

    Collection<DataFile> getPostsData() {
        if (!dataRead) {
            throw new IllegalStateException("Data never read! Invoke #readData() at least once.");
        }

        return postsData;
    }

    @Override
    public void execute() throws ApplicationException {
        readData();
        final List<Page> publishedSites = isSites() ? publishSites() : Lists.<Page>newArrayList();

        final List<Page> publishedPosts = publisPosts();
        updateIndexes(publishedSites, publishedPosts);
    }

    public void setSites(boolean sites) {
        this.sites = sites;
    }

    private boolean isSites() {
        return sites;
    }

    public void setPurga(boolean purga) {
        this.purge = purga;
    }

    private boolean isPurge() {
        return purge;
    }

    private List<Page> publishSites() throws ApplicationException {
        LOG.info("Publish sites...");
        return publishFiles(
                Formatters.Type.SITE,
                getSitesData(),
                dirs.htdocsSites());
    }

    private List<Page> publisPosts() throws ApplicationException {
        LOG.info("Publish posts...");
        return publishFiles(
                Formatters.Type.POST,
                getPostsData(),
                dirs.htdocsPosts());
    }

    private void updateIndexes(final List<Page> sites, final List<Page> posts) {
        updateHomeSite();
        updateSiteMap();
        updateFeed();
    }

    /**
     * Publish all files in given directory with given layout.
     *
     * @param type must not be {@literal null}
     * @param data must not be {@literal null}
     * @param outputDir must not be {@literal null}
     * @throws ApplicationException if can't render template
     */
    private List<Page> publishFiles(final Formatters.Type type, final Collection<DataFile> data, final Path outputDir)
            throws ApplicationException {
        Validate.notNull(type, "Layout must not be null!");
        Validate.notNull(data, "Dirname must not be null!");
        Validate.notNull(outputDir, "Output dir must not be null!");
        LOG.debug(String.format("Pubish files from '%s'...", data));
        final List<Page> published = Lists.newArrayList();

        for (final DataFile file : data) {
            published.add(publishFile(type, file, outputDir));
        }

        return published;
    }

    /**
     * Publish a given file with a given layout.
     *
     * @param type must not be {@literal null}
     * @param file must not be {@literal null} or empty
     * @param outputDir must not be {@literal null}
     * @throws ApplicationException if can't render template
     */
    private Page publishFile(final Formatters.Type type, final DataFile data, final Path outputDir)
            throws ApplicationException {
        Validate.notNull(type, "Layout must not be null!");
        Validate.notNull(data, "File name must not be null or empty!");
        Validate.notNull(outputDir, "Output dir must not be null!");
        final String targetFileName = data.getSlug() + ".html";
        LOG.info(String.format("Publishing file '%s'...", targetFileName));
        final Path target = outputDir.resolve(targetFileName);

        if (publishedFileExists(target.toFile())) {
            LOG.info(String.format("File %s already exists.", target));

            if (isPurge()) {
                LOG.info("Purge option is true. File will be republished.");
            } else {
                LOG.info("Skip file.");
                try {
                    return Page.newExistingPage("title", new URI(""), "description", new DateTime());
                } catch (URISyntaxException ex) {
                    throw new ApplicationException(ExitCodeImpl.FATAL, ex.getMessage(), ex);
                }
            }
        }

        FileInputStream input = null;

        try {
            if (type == Formatters.Type.POST || type == Formatters.Type.SITE) {
                input = new FileInputStream(new File(data.getFilename()));
                final DataProcessor processor = new DataProcessor(
                        input,
                        type,
                        baseUri,
                        templateConfig);

                LOG.info(String.format("Write published file to '%s'.", target));
                Files.createFile(target);
                Files.write(
                        target,
                        processor.getHtml().getBytes(Constants.DEFAULT_ENCODING.toString()),
                        StandardOpenOption.WRITE);
            }
        } catch (final IOException | TemplateException ex) {
            throw new ApplicationException(
                    ExitCodeImpl.FATAL,
                    String.format("Error occured during publishing: %s!", ex.getMessage()),
                    ex);
        } finally {
            IOUtils.closeQuietly(input);
        }

        try {
            return Page.newPublishedPage("title", new URI(""), "description", new DateTime());
        } catch (URISyntaxException ex) {
            throw new ApplicationException(ExitCodeImpl.FATAL, ex.getMessage(), ex);
        }
    }

    /**
     * Checks if a file to be published already exists.
     *
     * @param file must not be {@literal null}
     * @return {@literal true} if file already exists, else {@literal false}
     */
    boolean publishedFileExists(final File file) {
        return file.exists();
    }

    private void updateHomeSite() {
        LOG.info("Update home site...");
        new HomeSiteGenerator().execute();
    }

    private void updateSiteMap() {
        LOG.info("Update site map...");
        new SiteMapGenerator(templateConfig).execute();
    }

    private void updateFeed() {
        LOG.info("Update feed...");
//        new FeedGenerator(templateConfig).execute();
    }

}
