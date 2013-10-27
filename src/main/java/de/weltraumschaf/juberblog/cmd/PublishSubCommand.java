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

import com.google.common.collect.Lists;
import de.weltraumschaf.commons.ApplicationException;
import de.weltraumschaf.commons.IO;
import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.ExitCodeImpl;
import de.weltraumschaf.juberblog.files.FilenameFilters;
import de.weltraumschaf.juberblog.formatter.Formatters;
import de.weltraumschaf.juberblog.opt.PublishOptions;
import de.weltraumschaf.juberblog.template.Configurations;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.apache.commons.lang3.time.StopWatch;

/**
 * PublishSubCommand all sites from data directory.
 *
 * TODO - create index - create sitemap.xml - create feed xml
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class PublishSubCommand extends CommonCreateAndPublishSubCommand<PublishOptions> {

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
        watch.start();
        LOG.info("Start pulishing...");

        if (options.isSites()) {
            publishSites();
        }

        publisPosts();
        updateIndexes();
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

    /**
     * Publish sites.
     *
     * @throws ApplicationException if IO error occurs when configure templates
     */
    private void publishSites() throws ApplicationException {
        LOG.info("Publish sites...");
        publishFiles(
                Formatters.Type.SITE,
                getDirectories().dataSites(),
                getDirectories().htdocsSites());
    }

    /**
     * Publish posts.
     *
     * @throws ApplicationException if IO error occurs when configure templates
     */
    private void publisPosts() throws ApplicationException {
        LOG.info("Publish posts...");
        publishFiles(
                Formatters.Type.POST,
                getDirectories().dataPosts(),
                getDirectories().htdocsPosts());

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

            if (options.isPurge()) {
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
                    getBlogConfiguration().getBaseUri(),
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

    private void updateIndexes() {
        updateHomeSite();
        updateSiteMap();
        updateFeed();
    }

    private void updateHomeSite() {
        new HomeSiteGenerator().execute();
    }

    private void updateSiteMap() {
        new SiteMapGenerator().execute();
    }

    private void updateFeed() {
        new FeedGenerator().execute();
    }

}
