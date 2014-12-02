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
package de.weltraumschaf.juberblog.tasks;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.Page;
import de.weltraumschaf.juberblog.Publisher;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class PublishTask extends BaseTask<Collection<Page>, Void> implements Task<Collection<Page>, Void> {

    private final Config config;

    public PublishTask(final Config config) {
        super(Void.class);
        this.config = Validate.notNull(config, "config");
    }

    @Override
    public Collection<Page> execute() throws Exception {
        final Publisher publisher = new Publisher(
                config.inputDir,
                config.outputDir,
                config.layoutTemplate,
                config.contentTemplate,
                config.encoding
        );

        return publisher.publish();
    }

    @Override
    public Collection<Page> execute(final Void previusResult) throws Exception {
        return execute();
    }

    public static final class Config {

        private final String encoding;
        private final Path inputDir;
        private final Path outputDir;
        private final Path layoutTemplate;
        private final Path contentTemplate;

        public Config(final String encoding, final Path inputDir, final Path outputDir, final Path layoutTemplate, final Path contentTemplate) {
            super();
            this.encoding = Validate.notEmpty(encoding, "encoding");
            this.inputDir = Validate.notNull(inputDir, "inputDir");
            this.outputDir = Validate.notNull(outputDir, "outputDir");
            this.layoutTemplate = Validate.notNull(layoutTemplate, "layoutTemplate");
            this.contentTemplate = Validate.notNull(contentTemplate, "contentTemplate");
        }

    }
}
