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
import de.weltraumschaf.juberblog.opt.PublishOptions;
import de.weltraumschaf.juberblog.template.Layout;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
     * Freemarker templates resource directory.
     */
    private static final String TEMPLATE_DIRECTORRY = "de/weltraumschaf/juberblog/layout";
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
            templateConfig = configureTemplates();
        } catch (final IOException | URISyntaxException ex) {
            throw new ApplicationException(ExitCodeImpl.FATAL, "Can't configure templates!", ex);
        }
    }

    /**
     * Configure templates and returns configuration.
     *
     * @return never {@code null}
     * @throws IOException if template directory can't be read
     * @throws URISyntaxException if template directory URI can't be created from class loader
     */
    private Configuration configureTemplates() throws IOException, URISyntaxException {
        final Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(new File(getClass().getResource(TEMPLATE_DIRECTORRY).toURI()));
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setDefaultEncoding(Constants.DEFAULT_ENCODING.toString());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setIncompatibleImprovements(Constants.FREEMARKER_VERSION);
        return cfg;
    }

    @Override
    public void run() {
        watch.reset();
        watch.start();
        LOG.debug("Start pulishing...");

        if (options.isSites()) {
            publishSites();
        }

        publisPosts();
        watch.stop();
        LOG.debug(String.format("Publishing finished! Elapsed time: %s", watch.toString()));
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
    private void publishSites() {
        LOG.debug("Publish sites.");
        final Layout layout = new Layout(templateConfig, "/site.ftl");
        publishFiles(layout, getBlogConfiguration().getDataDir() + "/sites");
    }

    /**
     * Publish posts.
     */
    private void publisPosts() {
        LOG.debug("Publish posts.");
        final Layout layout = new Layout(templateConfig, "/post.ftl");
        publishFiles(layout, getBlogConfiguration().getDataDir() + "/posts");

    }

    /**
     * Publish all files in given directory with given layout.
     *
     * @param layout must not be {@code null}
     * @param dirname must not be {@code null}
     */
    private void publishFiles(final Layout layout, final String dirname) {
        Validate.notNull(layout, "Layout must not be null!");
        Validate.notNull(dirname, "Dirname must not be null!");
        LOG.debug(String.format("Pubish files from '%s'...", dirname));
        final List<String> fileList = readFileList(dirname);

        for (final String filename : fileList) {
            publishFile(layout, filename);
        }
    }

    /**
     * Read all files from a given directory.
     *
     * @param dirname must not be {@code null}
     * @return never {@code null} or empty
     */
    private List<String> readFileList(final String dirname) {
        Validate.notEmpty(dirname, "Dirname must not be null or empty");
        LOG.debug(String.format("Read file list from '%s'...", dirname));
        // TODO Read ile list.
        return Lists.newArrayList();
    }

    /**
     * Publish a given file with a given layout.
     *
     * @param layout must not be {@code null}
     * @param filename must not be {@code null} or empty
     */
    private void publishFile(final Layout layout, final String filename) {
        Validate.notNull(layout, "Layout must not be null!");
        Validate.notEmpty(filename, "File name must not be null or empty!");
        LOG.debug(String.format("Publish file '%s'...", filename));

        if (publishedFileExists(filename)) {
            LOG.debug("Published already exists.");

            if (options.isPurge()) {
                LOG.debug("Purge option is true. File will be republished.");
            } else {
                LOG.debug("Skip file.");
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
    private boolean publishedFileExists(final String filename) {
        // TODO Implement if file xist check
        return false;
    }

}
