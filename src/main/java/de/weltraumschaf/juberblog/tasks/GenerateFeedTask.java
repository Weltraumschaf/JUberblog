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

import com.beust.jcommander.internal.Lists;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.Fragment;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.freemarkerdown.RenderOptions;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class GenerateFeedTask implements Task<Void> {

    private final Config config;

    public GenerateFeedTask(final Config config) {
        super();
        this.config = Validate.notNull(config, "config");
    }

    @Override
    public Void execute() throws Exception {
        final FreeMarkerDown fmd = FreeMarkerDown.create();
        final Fragment template = fmd.createFragemnt(config.template, RenderOptions.WITHOUT_MARKDOWN);
        template.assignVariable("encoding", config.encoding);
        template.assignVariable("title", config.title);
        template.assignVariable("link", config.link);
        template.assignVariable("description", config.description);
        template.assignVariable("language", config.language);
        template.assignVariable("lastBuildDate", config.lastBuildDate);
        template.assignVariable("items", Lists.newArrayList());

        Files.write(
                config.outputDir.resolve("feed" + FileNameExtension.XML.getExtension()),
                fmd.render(template).getBytes(config.encoding)
        );

        return null;
    }

    public static final class Config {

        private final Path template;
        private final Path outputDir;
        private final String encoding;
        private final String title;
        private final String link;
        private final String description;
        private final String language;
        private final String lastBuildDate;

        public Config(final Path template, final Path outputDir, final String encoding, final String title, final String link, final String description, final String language, final String lastBuildDate) {
            super();
            this.template = Validate.notNull(template, "template");
            this.outputDir = Validate.notNull(outputDir, "outputDir");
            this.encoding = Validate.notEmpty(encoding, "title");
            this.title = Validate.notEmpty(title, "title");
            this.link = Validate.notEmpty(link, "link");
            this.description = Validate.notEmpty(description, "description");
            this.language = Validate.notEmpty(language, "language");
            this.lastBuildDate = Validate.notEmpty(lastBuildDate, "lastBuildDate");
        }
    }
}
