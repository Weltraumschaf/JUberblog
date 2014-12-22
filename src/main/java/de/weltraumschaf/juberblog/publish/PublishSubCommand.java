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

import de.weltraumschaf.juberblog.app.JUberblog;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.app.SubCommandBase;
import de.weltraumschaf.juberblog.core.TaskExecutor;
import org.joda.time.DateTime;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class PublishSubCommand extends SubCommandBase {

    private final TaskExecutor executor = new TaskExecutor();
    private final String encoding = "utf-8";

    public PublishSubCommand(final JUberblog registry) {
        super(registry);
    }

    @Override
    public void execute() throws Exception {
        executor.append(new PublishTask(new PublishTask.Config(
                                        encoding,
                                        registry().directories().getPostsData(),
                                        registry().directories().getPostsOutput(),
                                        registry().templates().getLayoutTemplate(),
                                        registry().templates().getPostTemplate(),
                                        Page.Type.POST
                                )))
                .append(new GenerateFeedTask(new GenerateFeedTask.Config(
                                        registry().templates().getFeedTemplate(),
                                        registry().directories().getOutput(),
                                        encoding,
                                        "title",
                                        "link",
                                        "description",
                                        "language",
                                        new DateTime("2014-12-08T20:17:00")
                                )))
                .append(new GenerateIndexTask(new GenerateIndexTask.Config(
                                        encoding,
                                        registry().directories().getOutput(),
                                        registry().templates().getLayoutTemplate(),
                                        registry().templates().getIndexTemplate()
                                )))
                .append(new PublishTask(new PublishTask.Config(
                                        encoding,
                                        registry().directories().getSitesData(),
                                        registry().directories().getSiteOutput(),
                                        registry().templates().getLayoutTemplate(),
                                        registry().templates().getSiteTemplate(),
                                        Page.Type.SITE
                                )))
                .append(new GenerateSitemapTask(new GenerateSitemapTask.Config(
                                        registry().templates().getSiteMapTemplate(),
                                        registry().directories().getOutput(),
                                        encoding)))
                .execute();
    }

}
