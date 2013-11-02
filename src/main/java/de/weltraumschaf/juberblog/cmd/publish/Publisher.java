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

import com.google.common.collect.Lists;
import de.weltraumschaf.commons.ApplicationException;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;

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
        publishFiles(
                Formatters.Type.SITE,
                dirs.dataSites(),
                dirs.htdocsSites());

        return Lists.newArrayList();
    }

    private List<Page> publisPosts() throws ApplicationException {
        LOG.info("Publish posts...");
        publishFiles(
                Formatters.Type.POST,
                dirs.dataPosts(),
                dirs.htdocsPosts());
        return Lists.newArrayList();
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
     * @param dataDir must not be {@literal null}
     * @param outputDir must not be {@literal null}
     * @throws ApplicationException if can't render template
     */
    private void publishFiles(final Formatters.Type type, final Path dataDir, final Path outputDir)
        throws ApplicationException {
        Validate.notNull(type, "Layout must not be null!");
        Validate.notNull(dataDir, "Dirname must not be null!");
        Validate.notNull(outputDir, "Output dir must not be null!");
        LOG.debug(String.format("Pubish files from '%s'...", dataDir));

        for (final File file : readFileList(dataDir)) {
            publishFile(type, file, outputDir);
        }
    }

    /**
     * Read all files from a given directory.
     *
     * @param dir must not be {@literal null}
     * @return never {@literal null} or empty
     */
    private List<File> readFileList(final Path dir) {
        Validate.notNull(dir, "Dir must not be null!");
        LOG.debug(String.format("Read file list from '%s'...", dir));
        final List<File> files = Lists.newArrayList();

        for (final File f : dir.toFile().listFiles(FilenameFilters.findMarkdownFiles())) {
            files.add(f);
        }

        LOG.debug(String.format("Found %d files to publish.", files.size()));
        return files;
    }

    /**
     * Publish a given file with a given layout.
     *
     * @param type must not be {@literal null}
     * @param file must not be {@literal null} or empty
     * @param outputDir must not be {@literal null}
     * @throws ApplicationException if can't render template
     */
    private void publishFile(final Formatters.Type type, final File file, final Path outputDir)
        throws ApplicationException {
        Validate.notNull(type, "Layout must not be null!");
        Validate.notNull(file, "File name must not be null or empty!");
        Validate.notNull(outputDir, "Output dir must not be null!");
        LOG.info(String.format("Publishing file '%s'...", file));

        if (publishedFileExists(file)) {
            LOG.info(String.format("File %s already exists.", file));

            if (isPurge()) {
                LOG.info("Purge option is true. File will be republished.");
            } else {
                LOG.info("Skip file.");
                return;
            }
        }

        FileInputStream input = null;

        try {
            if (type == Formatters.Type.POST || type == Formatters.Type.SITE) {
                input = new FileInputStream(file);
                final DataProcessor processor = new DataProcessor(
                    input,
                    type,
                    baseUri,
                    templateConfig);
                final String targetFileName = processor.getSlug() + ".html";
                final Path target = outputDir.resolve(targetFileName);
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
    }

    /**
     * Checks if a file to be published already exists.
     *
     * @param filename must not be {@literal null} or empty
     * @return {@literal true} if file already exists, else {@literal false}
     */
    private boolean publishedFileExists(final File filename) {
        // TODO Implement if file exist check
        return false;
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
