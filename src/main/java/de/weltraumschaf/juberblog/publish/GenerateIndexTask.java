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

import com.beust.jcommander.internal.Maps;
import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.Fragment;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.freemarkerdown.Layout;
import de.weltraumschaf.freemarkerdown.RenderOptions;
import de.weltraumschaf.juberblog.core.DateFormatter;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.core.Page.Pages;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.core.BaseTask;
import de.weltraumschaf.juberblog.core.Configuration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Task;
import de.weltraumschaf.juberblog.core.Templates;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.joda.time.DateTime;

/**
 * Task to generate the index site.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class GenerateIndexTask extends BaseTask<Pages, Pages> implements Task<Pages, Pages> {

    /**
     * Task configuration.
     */
    private final Config config;

    /**
     * Dedicated constructor.
     *
     * @param config must not be {@code null}
     */
    public GenerateIndexTask(final Config config) {
        super(Pages.class);
        this.config = Validate.notNull(config, "config");
    }

    @Override
    public Pages execute() throws Exception {
        return execute(new Page.Pages());
    }

    @Override
    public Pages execute(Pages previusResult) throws Exception {
        final FreeMarkerDown fmd = FreeMarkerDown.create(config.encoding);
        final Fragment template = fmd.createFragemnt(
                config.indexTemplate,
                config.encoding,
                config.indexTemplate.toString(),
                RenderOptions.WITHOUT_MARKDOWN);
        final Layout layout = fmd.createLayout(
                config.layoutTemplate,
                config.encoding,
                config.layoutTemplate.toString(),
                RenderOptions.WITHOUT_MARKDOWN);
        layout.assignTemplateModel("content", template);
        layout.assignVariable("name", config.name);
        layout.assignVariable("description", config.description);
        template.assignVariable("posts", convert(previusResult));
        Files.write(
                config.outputDir.resolve("index" + FileNameExtension.HTML.getExtension()),
                fmd.render(layout).getBytes(config.encoding)
        );

        return previusResult;
    }

    /**
     * Convert collection of pages into plain java collections for assigning them to the templates.
     *
     * @param pages must not be {@code null}
     * @return never {@code null}, unmodifiable
     */
    private Collection<Map<String, String>> convert(final Pages pages) {
        final Collection<Map<String, String>> items = Lists.newArrayList();

        for (final Page page : Validate.notNull(pages, "pages")) {
            items.add(convert(page));
        }

        return Collections.unmodifiableCollection(items);
    }

    /**
     * Converts a single page into plain java collections for assigning them to the templates.
     *
     * @param page must not be {@code null}
     * @return never {@code null}, unmodifiable
     */
    private Map<String, String> convert(final Page page) {
        // XXX: Code duplication de.weltraumschaf.juberblog.publish.GenerateSitemapTask.convert(de.weltraumschaf.juberblog.core.Page):java.util.Map<java.lang.String,java.lang.String>
        Validate.notNull(page, "page");
        final Map<String, String> item = Maps.newHashMap();
        item.put("title", page.getTitle());
        item.put("link", page.getLink().toString());
        item.put("description", page.getDescription());
        item.put(
                "pubDate",
                DateFormatter.format(
                        page.getPublishingDate(),
                        DateFormatter.Format.RSS_PUBLISH_DATE_FORMAT));
        item.put(
                "dcDate",
                DateFormatter.format(page.getPublishingDate(),
                        DateFormatter.Format.W3C_DATE_FORMAT));
        return Collections.unmodifiableMap(item);
    }

    /**
     * Task configuration.
     */
    public static final class Config {

        /**
         * Used to read/write files and as the encoding in the HTML.
         */
        private final String encoding;
        /**
         * Where to store {@literal index.html}.
         */
        private final Path outputDir;
        /**
         * The outer template of the {@literal index.html).
         */
        private final Path layoutTemplate;
        /**
         * The inner template of the {@literal index.html).
         */
        private final Path indexTemplate;
        /**
         * The name of the blog.
         */
        private final String name;
        /**
         * The description of the blog.
         */
        private final String description;

        /**
         * Dedicated constructor.
         *
         * @param templates must not be {@code null}
         * @param directories must not be {@code null}
         * @param configuration must not be {@code null}
         */
        public Config(
                final Templates templates,
                final Directories directories,
                final Configuration configuration) {
            super();
            Validate.notNull(templates, "templates");
            Validate.notNull(directories, "directories");
            Validate.notNull(configuration, "configuration");
            this.encoding = configuration.getEncoding();
            this.outputDir =directories.getOutput();
            this.layoutTemplate = templates.getLayoutTemplate();
            this.indexTemplate = templates.getIndexTemplate();
            this.name = configuration.getTitle();
            this.description = configuration.getDescription();
        }

        /**
         * Dedicated constructor.
         *
         * @param encoding must not be {@code null} or empty
         * @param outputDir must not be {@code null}
         * @param layoutTemplate must not be {@code null}
         * @param indexTemplate must not be {@code null}
         * @param name must not be {@code null} or empty
         * @param description must not be {@code null} or empty
         */
        @Deprecated
        public Config(
                final String encoding,
                final Path outputDir,
                final Path layoutTemplate,
                final Path indexTemplate,
                final String name,
                final String description) {
            super();
            this.encoding = Validate.notEmpty(encoding, "encoding");
            this.outputDir = Validate.notNull(outputDir, "outputDir");
            this.layoutTemplate = Validate.notNull(layoutTemplate, "layoutTemplate");
            this.indexTemplate = Validate.notNull(indexTemplate, "indexTemplate");
            this.name = Validate.notEmpty(name, "name");
            this.description = Validate.notEmpty(description, "description");
        }

    }
}
