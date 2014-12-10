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
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.core.Task;
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
public class GenerateSitemapTask implements Task<Void, Page.Pages> {

    private final Config config;

    public GenerateSitemapTask(final Config config) {
        super();
        this.config = Validate.notNull(config, "config");
    }

    @Override
    public Void execute() throws Exception {
        return execute(new Page.Pages());
    }

    @Override
    public Void execute(final Page.Pages previusResult) throws Exception {
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

    private Collection<Map<String, String>> convert(final List<Page> pages) {
        final Collection<Map<String, String>> items = Lists.newArrayList();

        for (final Page page : pages) {
            items.add(convert(page));
        }

        return Collections.unmodifiableCollection(items);
    }

    private Map<String, String> convert(final Page page) {
        final Map<String, String> item = Maps.newHashMap();
        item.put("loc", page.getLink());
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

    @Override
    public Class<Page.Pages> getDesiredTypeForPreviusResult() {
        return Page.Pages.class;
    }

    public static final class Config {

        private final Path template;
        private final Path outputDir;
        private final String encoding;

        public Config(final Path template, final Path outputDir, final String encoding) {
            super();
            this.template = Validate.notNull(template, "template");
            this.outputDir = Validate.notNull(outputDir, "outputDir");
            this.encoding = Validate.notEmpty(encoding, "title");
        }
    }
}
