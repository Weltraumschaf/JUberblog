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
package de.weltraumschaf.juberblog.publish;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.core.Page.Pages;
import de.weltraumschaf.juberblog.core.Page.Type;
import de.weltraumschaf.juberblog.core.BaseTask;
import de.weltraumschaf.juberblog.core.Configuration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Task;
import de.weltraumschaf.juberblog.core.Templates;
import java.net.URI;
import java.nio.file.Path;

/**
 * Task to publish pages.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class PublishTask extends BaseTask<Pages, Pages> implements Task<Pages, Pages> {

    /**
     * Task configuration.
     */
    private final Config config;

    /**
     * Dedicated constructor.
     *
     * @param config must not be {@code null}
     */
    public PublishTask(final Config config) {
        super(Pages.class);
        this.config = Validate.notNull(config, "config");
    }

    @Override
    public Pages execute() throws Exception {
        return execute(new Pages());
    }

    @Override
    public Pages execute(final Pages previusResult) throws Exception {
        final Publisher publisher = new Publisher(
                config.inputDir,
                config.outputDir,
                config.layoutTemplate,
                config.contentTemplate,
                config.encoding,
                config.baseUrlForPages,
                config.type
        );

        previusResult.addAll(publisher.publish());
        return previusResult;
    }

    /**
     * Task configuration.
     */
    public static final class Config {

        /**
         * Encoding to read/write files and for XML.
         */
        private final String encoding;
        /**
         * Where to read the Markdown data files.
         */
        private final Path inputDir;
        /**
         * Where to store the HTML pages.
         */
        private final Path outputDir;
        /**
         * Outer template.
         */
        private final Path layoutTemplate;
        /**
         * Inner template.
         */
        private final Path contentTemplate;
        /**
         * Type of published data.
         */
        private final Type type;
        /**
         * Base URI for published page.
         */
        private final URI baseUrlForPages;

        /**
         * Dedicated constructor.
         *
         * @param templates must not be {@code null}
         * @param directories must not be {@code null}
         * @param configuration must not be {@code null}
         * @param type must not be {@code null}
         */
        public Config(
                final Templates templates,
                final Directories directories,
                final Configuration configuration,
                final Type type) {
            super();
            Validate.notNull(templates, "templates");
            Validate.notNull(directories, "directories");
            Validate.notNull(configuration, "configuration");
            this.encoding = configuration.getEncoding();
            this.layoutTemplate = templates.getLayoutTemplate();
            this.type = Validate.notNull(type, "type");
            this.baseUrlForPages = configuration.getBaseUri();

            // XXX: Move into publisher.
            if (type == Type.POST) {
                this.inputDir = directories.getPostsData();
                this.outputDir = directories.getPostsOutput();
                this.contentTemplate = templates.getPostTemplate();
            } else if (type == Type.SITE) {
                this.inputDir = directories.getSitesData();
                this.outputDir = directories.getSitesOutput();
                this.contentTemplate = templates.getSiteTemplate();
            } else {
                throw new IllegalArgumentException(String.format("Bad type '%s'!", type));
            }
        }

        /**
         * Dedicated constructor.
         *
         * @param encoding must not be {@code null} or empty
         * @param inputDir must not be {@code null}
         * @param outputDir must not be {@code null}
         * @param layoutTemplate must not be {@code null}
         * @param contentTemplate must not be {@code null}
         * @param type must not be {@code null}
         * @param baseUrlForPages must not be {@code null}
         */
        @Deprecated
        public Config(
                final String encoding,
                final Path inputDir,
                final Path outputDir,
                final Path layoutTemplate,
                final Path contentTemplate,
                final Type type,
                final URI baseUrlForPages) {
            super();
            this.encoding = Validate.notEmpty(encoding, "encoding");
            this.inputDir = Validate.notNull(inputDir, "inputDir");
            this.outputDir = Validate.notNull(outputDir, "outputDir");
            this.layoutTemplate = Validate.notNull(layoutTemplate, "layoutTemplate");
            this.contentTemplate = Validate.notNull(contentTemplate, "contentTemplate");
            this.type = Validate.notNull(type, "type");
            this.baseUrlForPages = Validate.notNull(baseUrlForPages, "baseUrlForPages");
        }

    }
}
