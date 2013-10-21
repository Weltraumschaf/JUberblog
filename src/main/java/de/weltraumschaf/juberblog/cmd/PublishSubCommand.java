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
import de.weltraumschaf.juberblog.Directories;
import de.weltraumschaf.juberblog.ExitCodeImpl;
import de.weltraumschaf.juberblog.MarkdownFilenamefiler;
import de.weltraumschaf.juberblog.formatter.Formatter;
import de.weltraumschaf.juberblog.formatter.PostFormatter;
import de.weltraumschaf.juberblog.formatter.SiteFormatter;
import de.weltraumschaf.juberblog.opt.PublishOptions;
import de.weltraumschaf.juberblog.template.Configurations;
import freemarker.template.Configuration;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.apache.commons.lang3.time.StopWatch;

/**
 * PublishSubCommand all sites from data directory.
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
     * @param io must not be {@code null}
     */
    public PublishSubCommand(final IO io) {
        super(io);
    }

    @Override
    protected void init() throws ApplicationException {
        super.init();
        try {
            templateConfig = Configurations.forProduction(getDirectories().getTemplateDir().toString());
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
            try {
                publishSites();
            } catch (final IOException ex) {
                throw new ApplicationException(ExitCodeImpl.FATAL, "Can't publish sites!", ex);
            }
        }

        try {
            publisPosts();
        } catch (final IOException ex) {
            throw new ApplicationException(ExitCodeImpl.FATAL, "Can't publish posts!", ex);
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

    /**
     * Publish sites.
     */
    private void publishSites() throws IOException {
        LOG.info("Publish sites...");
        final Formatter fmt = new SiteFormatter(templateConfig);
        publishFiles(fmt, getBlogConfiguration().getDataDir() + "/sites");
    }

    /**
     * Publish posts.
     */
    private void publisPosts() throws IOException {
        LOG.info("Publish posts...");
        final Formatter fmt = new PostFormatter(templateConfig);
        publishFiles(fmt, getBlogConfiguration().getDataDir() + "/posts");

    }

    /**
     * Publish all files in given directory with given layout.
     *
     * @param fmt must not be {@code null}
     * @param dirname must not be {@code null}
     */
    private void publishFiles(final Formatter fmt, final String dirname) {
        Validate.notNull(fmt, "Layout must not be null!");
        Validate.notNull(dirname, "Dirname must not be null!");
        LOG.debug(String.format("Pubish files from '%s'...", dirname));
        final List<File> fileList = readFileList(dirname);

        for (final File file : fileList) {
            publishFile(fmt, file);
        }
    }

    /**
     * Read all files from a given directory.
     *
     * @param dirname must not be {@code null}
     * @return never {@code null} or empty
     */
    private List<File> readFileList(final String dirname) {
        Validate.notEmpty(dirname, "Dirname must not be null or empty");
        LOG.debug(String.format("Read file list from '%s'...", dirname));
        final ArrayList<File> files = Lists.newArrayList();

        for (final File f : getDirectories().getDataDir().toFile().listFiles(new MarkdownFilenamefiler())) {
            files.add(f);
        }

        LOG.debug(String.format("Found %d files to publish.", files.size()));
        return files;
    }

    /**
     * Publish a given file with a given layout.
     *
     * @param fmt must not be {@code null}
     * @param file must not be {@code null} or empty
     */
    private void publishFile(final Formatter fmt, final File file) {
        Validate.notNull(fmt, "Layout must not be null!");
        Validate.notNull(file, "File name must not be null or empty!");
        LOG.info(String.format("Publish file '%s'...", file));

        if (publishedFileExists(file)) {
            LOG.info(String.format("File %s already exists.", file));

            if (options.isPurge()) {
                LOG.info("Purge option is true. File will be republished.");
            } else {
                LOG.info("Skip file.");
                return;
            }
        }

        // TODO Publish file.
    }

    /**
     * Checks if a file to be published already exists.
     *
     * @param filename must not be {@code null} or empty
     * @return {@code true} if file already exists, else {@code false}
     */
    private boolean publishedFileExists(final File filename) {
        // TODO Implement if file xist check
        return false;
    }

}
