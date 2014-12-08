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

import com.beust.jcommander.internal.Maps;
import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.Fragment;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.freemarkerdown.Layout;
import de.weltraumschaf.freemarkerdown.RenderOptions;
import de.weltraumschaf.juberblog.DateFormatter;
import de.weltraumschaf.juberblog.Page;
import de.weltraumschaf.juberblog.Page.Pages;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class GenerateIndexTask extends BaseTask<Pages, Pages> implements Task<Pages, Pages> {

    private final Config config;

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
        layout.assignVariable("name", "TODO");
        layout.assignVariable("description", "TODO");
        template.assignVariable("posts", convert(previusResult));
        Files.write(
                config.outputDir.resolve("index" + FileNameExtension.HTML.getExtension()),
                fmd.render(layout).getBytes(config.encoding)
        );

        return previusResult;
    }

    private Collection<Map<String, String>> convert(final List<Page> pages) {
        final Collection<Map<String, String>> items = Lists.newArrayList();

        for (final Page page : pages) {
            items.add(convert(page));
        }

        return Collections.unmodifiableCollection(items);
    }

    private Map<String, String> convert(final Page page) {
        final Map<String, String> item = Maps.newHashMap();
        item.put("title", page.getTitle());
        item.put("link", page.getLink());
        item.put("description", page.getDescription());
        item.put("pubDate", DateFormatter.format(page.getPublishingDate(), DateFormatter.Format.RSS_PUBLISH_DATE_FORMAT));
        item.put("dcDate", DateFormatter.format(page.getPublishingDate(), DateFormatter.Format.W3C_DATE_FORMAT));
        return Collections.unmodifiableMap(item);
    }

    public static final class Config {

        private final String encoding;
        private final Path outputDir;
        private final Path layoutTemplate;
        private final Path indexTemplate;

        public Config(final String encoding, final Path outputDir, final Path layoutTemplate, final Path indexTemplate) {
            super();
            this.encoding = Validate.notEmpty(encoding, "encoding");
            this.outputDir = Validate.notNull(outputDir, "outputDir");
            this.layoutTemplate = Validate.notNull(layoutTemplate, "layoutTemplate");
            this.indexTemplate = Validate.notNull(indexTemplate, "indexTemplate");
        }

    }
}
