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
package de.weltraumschaf.juberblog;

import de.weltraumschaf.commons.validate.Validate;
import static de.weltraumschaf.juberblog.JUberblogTestCase.ENCODING;
import de.weltraumschaf.juberblog.tasks.Task;
import de.weltraumschaf.juberblog.tasks.TaskExecutor;
import java.nio.file.Path;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InOrder;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link App}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AppTest extends JUberblogTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void publishing() throws Exception {
        final TaskExecutor executor = new TaskExecutor();
        executor.append(new PublishTask(new PublishTask.Config(
                        ENCODING,
                        createPath("posts"),
                        tmp.getRoot().toPath(),
                        createPath("layout.ftl"),
                        createPath("post.ftl")
                )))
                .append(new GenerateFeedTask())
                .append(new PublishTask(new PublishTask.Config(
                        ENCODING,
                        createPath("sites"),
                        tmp.getRoot().toPath(),
                        createPath("layout.ftl"),
                        createPath("site.ftl")
                )))
                .append(new GenerateIndexTask())
                .append(new GenerateSitemapTask())
                .execute();
    }

    public static final class PublishTask implements Task<Void> {

        private final Config config;

        public PublishTask(final Config config) {
            super();
            this.config = config;
        }

        @Override
        public Void execute() throws Exception {
            final Publisher publisher = new Publisher(
                    config.inputDir,
                    config.outputDir,
                    config.layoutTemplate,
                    config.contentTemplate,
                    config.encoding
            );
            publisher.publish();
            return null;
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

    class GenerateIndexTask implements Task<Void> {

        @Override
        public Void execute() throws Exception {
            return null;
        }
    }

    class GenerateSitemapTask implements Task<Void> {

        @Override
        public Void execute() throws Exception {
            return null;
        }
    }

    class GenerateFeedTask implements Task<Void> {

        @Override
        public Void execute() throws Exception {
            return null;
        }
    }
}
