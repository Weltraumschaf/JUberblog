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
import de.weltraumschaf.juberblog.BlogConfiguration;
import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.ExitCodeImpl;
import de.weltraumschaf.juberblog.layout.PageLayout;
import de.weltraumschaf.juberblog.opt.PublishOptions;
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
    private static final Logger LOG = Logger.getLogger(CreateSubCommand.class);
    private PublishOptions options;
    private Configuration templateConfig;
    private BlogConfiguration blogConfig;
    private final StopWatch watch = new StopWatch();

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

    public BlogConfiguration getBlogConfig() {
        if (null == blogConfig) {
            blogConfig = new BlogConfiguration(options.getConfigurationFile());
        }

        return blogConfig;
    }

    private void publishSites() {
        LOG.debug("Publish sites.");
        final PageLayout layout = new PageLayout(templateConfig, "/site.ftl");
        publishFiles(layout, getBlogConfig().getDataDir() + "/sites");
    }

    private void publisPosts() {
        LOG.debug("Publish posts.");
        final PageLayout layout = new PageLayout(templateConfig, "/post.ftl");
        publishFiles(layout, getBlogConfig().getDataDir() + "/posts");

    }

    private Configuration configureTemplates() throws IOException, URISyntaxException {
        final Configuration cfg = new Configuration();
        final File templateDir = new File(getClass().getResource(TEMPLATE_DIRECTORRY).toURI());
        cfg.setDirectoryForTemplateLoading(templateDir);
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setDefaultEncoding(Constants.DEFAULT_ENCODING.toString());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setIncompatibleImprovements(Constants.FREEMARKER_VERSION);
        return cfg;
    }

    private void publishFiles(final PageLayout layout, final String dirname) {
        LOG.debug(String.format("Pubish files from '%s'...", dirname));
        final List<String> fileList = readFileList(dirname);

        for (final String filename : fileList) {
            publishFile(layout, filename);
        }
    }

    private List<String> readFileList(final String dirname) {
        Validate.notEmpty(dirname);
        LOG.debug(String.format("Read file list from '%s'...", dirname));
        return Lists.newArrayList();
    }

    private void publishFile(final PageLayout layout, final String filename) {
        Validate.notEmpty(filename);
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

    private boolean publishedFileExists(final String filename) {
        // TODO Implement if file xist check
        return false;
    }

}
