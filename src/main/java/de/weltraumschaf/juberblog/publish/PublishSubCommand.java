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

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.core.Options;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.core.SubCommandBase;
import de.weltraumschaf.juberblog.core.TaskExecutor;
import org.joda.time.DateTime;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class PublishSubCommand extends SubCommandBase {

    private final TaskExecutor executor = new TaskExecutor();
    private final String encoding = "utf-8";
    private Templates templates;
    private Directories dirs;

    public PublishSubCommand(final Options options, final IO io) {
        super(options, io);
    }

    public void setTemplates(final Templates templates) {
        this.templates = Validate.notNull(templates, "templates");
    }

    public void setDirs(final Directories dirs) {
        this.dirs = Validate.notNull(dirs, "dirs");
    }

    @Override
    public void execute() throws Exception {
        if (null == templates) {
            throw new IllegalStateException("Member 'templates' must not be null! Set it bevore.");
        }

        if (null == dirs) {
            throw new IllegalStateException("Member 'dirs' must not be null! Set it bevore.");
        }

        executor.append(new PublishTask(new PublishTask.Config(
                                        encoding,
                                        dirs.getPostsData(),
                                        dirs.getPostsOutput(),
                                        templates.getLayoutTemplate(),
                                        templates.getPostTemplate(),
                                        Page.Type.POST
                                )))
                .append(new GenerateFeedTask(new GenerateFeedTask.Config(
                                        templates.getFeedTemplate(),
                                        dirs.getOutput(),
                                        encoding,
                                        "title",
                                        "link",
                                        "description",
                                        "language",
                                        new DateTime("2014-12-08T20:17:00")
                                )))
                .append(new GenerateIndexTask(new GenerateIndexTask.Config(
                                        encoding,
                                        dirs.getOutput(),
                                        templates.getLayoutTemplate(),
                                        templates.getIndexTemplate()
                                )))
                .append(new PublishTask(new PublishTask.Config(
                                        encoding,
                                        dirs.getSitesData(),
                                        dirs.getSiteOutput(),
                                        templates.getLayoutTemplate(),
                                        templates.getSiteTemplate(),
                                        Page.Type.SITE
                                )))
                .append(new GenerateSitemapTask(new GenerateSitemapTask.Config(
                                        templates.getSiteMapTemplate(),
                                        dirs.getOutput(),
                                        encoding)))
                .execute();
    }

}
