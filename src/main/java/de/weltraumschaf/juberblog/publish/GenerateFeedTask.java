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
import de.weltraumschaf.freemarkerdown.RenderOptions;
import de.weltraumschaf.juberblog.core.DateFormatter;
import de.weltraumschaf.juberblog.core.DateFormatter.Format;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.core.Page.Pages;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.core.BaseTask;
import de.weltraumschaf.juberblog.core.Task;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.joda.time.DateTime;

/**
 * Generates the RSS feed.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class GenerateFeedTask extends BaseTask<Pages, Pages> implements Task<Pages, Pages> {

    /**
     * Task configuration.
     */
    private final Config config;

    /**
     * Dedicated constructor.
     *
     * @param config  must not be {@code null}
     */
    public GenerateFeedTask(final Config config) {
        super(Pages.class);
        this.config = Validate.notNull(config, "config");
    }

    @Override
    public Pages execute() throws Exception {
        return execute(new Pages());
    }

    @Override
    public Pages execute(final Pages previusResult) throws Exception {
        final FreeMarkerDown fmd = FreeMarkerDown.create(config.encoding);
        final Fragment template = fmd.createFragemnt(
                config.template,
                config.encoding,
                config.template.toString(),
                RenderOptions.WITHOUT_MARKDOWN);
        template.assignVariable("encoding", config.encoding);
        template.assignVariable("title", config.title);
        template.assignVariable("link", config.link.toString());
        template.assignVariable("description", config.description);
        template.assignVariable("language", config.language);
        template.assignVariable(
                "lastBuildDate",
                DateFormatter.format(config.lastBuildDate, Format.RSS_PUBLISH_DATE_FORMAT));
        template.assignVariable("items", convert(previusResult));

        Files.write(
                config.outputDir.resolve("feed" + FileNameExtension.XML.getExtension()),
                fmd.render(template).getBytes(config.encoding)
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

        for (final Page page : pages) {
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
        final Map<String, String> item = Maps.newHashMap();
        item.put("title", page.getTitle());
        item.put("link", page.getLink().toString());
        item.put("description", page.getDescription());
        item.put("pubDate", DateFormatter.format(page.getPublishingDate(), Format.RSS_PUBLISH_DATE_FORMAT));
        item.put("dcDate", DateFormatter.format(page.getPublishingDate(), Format.W3C_DATE_FORMAT));
        return Collections.unmodifiableMap(item);
    }

    /**
     * Task configuration.
     */
    public static final class Config {

        /**
         * The template of the XML.
         */
        private final Path template;
        /**
         * Where to store the XML.
         */
        private final Path outputDir;
        /**
         * Encoding to read/write files and for XML.
         */
        private final String encoding;
        /**
         * Blog title.
         */
        private final String title;
        /**
         * Blog URL.
         */
        private final URI link;
        /**
         * Blog description.
         */
        private final String description;
        /**
         * Blog language.
         */
        private final String language;
        /**
         * Date of last feed generation.
         */
        private final DateTime lastBuildDate;

        /**
         * Dedicated constructor.
         *
         * XXX: Use builder to reduce constructor parameters.
         *
         * @param template must not be {@code null}
         * @param outputDir must not be {@code null}
         * @param encoding must not be {@code null} or empty
         * @param title must not be {@code null} or empty
         * @param link must not be {@code null}
         * @param description must not be {@code null} or empty
         * @param language must not be {@code null} or empty
         * @param lastBuildDate must not be {@code null}
         */
        public Config(
                final Path template,
                final Path outputDir,
                final String encoding,
                final String title,
                final URI link,
                final String description,
                final String language,
                final DateTime lastBuildDate) {
            super();
            this.template = Validate.notNull(template, "template");
            this.outputDir = Validate.notNull(outputDir, "outputDir");
            this.encoding = Validate.notEmpty(encoding, "encoding");
            this.title = Validate.notEmpty(title, "title");
            this.link = Validate.notNull(link, "link");
            this.description = Validate.notEmpty(description, "description");
            this.language = Validate.notEmpty(language, "language");
            this.lastBuildDate = Validate.notNull(lastBuildDate, "lastBuildDate");
        }
    }
}
