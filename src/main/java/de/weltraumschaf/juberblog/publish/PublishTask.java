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
import de.weltraumschaf.juberblog.core.Task;
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
                "http://localhost/posts", // FIXME Use URI from config.
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
         * Dedicated constructor.
         *
         * XXX: Use builder to reduce constructor parameters.
         *
         * @param encoding must not be {@code null} or empty
         * @param inputDir must not be {@code null}
         * @param outputDir must not be {@code null}
         * @param layoutTemplate must not be {@code null}
         * @param contentTemplate must not be {@code null}
         * @param type must not be {@code null}
         */
        public Config(
                final String encoding,
                final Path inputDir,
                final Path outputDir,
                final Path layoutTemplate,
                final Path contentTemplate,
                final Type type) {
            super();
            this.encoding = Validate.notEmpty(encoding, "encoding");
            this.inputDir = Validate.notNull(inputDir, "inputDir");
            this.outputDir = Validate.notNull(outputDir, "outputDir");
            this.layoutTemplate = Validate.notNull(layoutTemplate, "layoutTemplate");
            this.contentTemplate = Validate.notNull(contentTemplate, "contentTemplate");
            this.type = Validate.notNull(type, "type");
        }

    }
}
