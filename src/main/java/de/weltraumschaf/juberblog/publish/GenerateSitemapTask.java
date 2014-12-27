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
import de.weltraumschaf.juberblog.core.BaseTask;
import de.weltraumschaf.juberblog.core.DateFormatter;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.core.Page.Pages;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.core.Task;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Task to generate a site map XML.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class GenerateSitemapTask extends BaseTask<Void, Pages> implements Task<Void, Pages> {

    /**
     * Task configuration.
     */
    private final Config config;

    /**
     * Dedicated constructor.
     *
     * @param config must not be {@code null}
     */
    public GenerateSitemapTask(final Config config) {
        super(Pages.class);
        this.config = Validate.notNull(config, "config");
    }

    @Override
    public Void execute() throws Exception {
        return execute(new Page.Pages());
    }

    @Override
    public Void execute(final Pages previusResult) throws Exception {
        final FreeMarkerDown fmd = FreeMarkerDown.create(config.encoding);
        final Fragment template = fmd.createFragemnt(
                config.template,
                config.encoding,
                config.template.toString(),
                RenderOptions.WITHOUT_MARKDOWN);
        template.assignVariable("encoding", config.encoding);
        template.assignVariable("urls", convert(previusResult));

        Files.write(
                config.outputDir.resolve("site_map" + FileNameExtension.XML.getExtension()),
                fmd.render(template).getBytes(config.encoding)
        );

        return null;
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
        // XXX: Code duplication de.weltraumschaf.juberblog.publish.GenerateIndexTask.convert(de.weltraumschaf.juberblog.core.Page):java.util.Map<java.lang.String,java.lang.String>
        final Map<String, String> item = Maps.newHashMap();
        item.put("loc", page.getLink().toString());
        // XXX Introduce last mod date.
        item.put("lastmod", DateFormatter.format(page.getPublishingDate(), DateFormatter.Format.W3C_DATE_FORMAT));

        if (page.getType() == Page.Type.POST) {
            item.put("changefreq", "daily");
            item.put("priority", "0.8");
        } else {
            item.put("changefreq", "weekly");
            item.put("priority", "0.5");
        }

        return Collections.unmodifiableMap(item);
    }

    /**
     * Task configuration.
     */
    public static final class Config {

        /**
         * Template for site map XML.
         */
        private final Path template;
        /**
         * Where to store site map.
         */
        private final Path outputDir;
        /**
         * Used to read/write files and as the encoding in the HTML.
         */
        private final String encoding;

        /**
         * Dedicated constructor.
         *
         * @param template must not be {@code null}
         * @param outputDir must not be {@code null}
         * @param encoding must not be {@code null} or empty
         */
        public Config(final Path template, final Path outputDir, final String encoding) {
            super();
            this.template = Validate.notNull(template, "template");
            this.outputDir = Validate.notNull(outputDir, "outputDir");
            this.encoding = Validate.notEmpty(encoding, "title");
        }
    }
}
