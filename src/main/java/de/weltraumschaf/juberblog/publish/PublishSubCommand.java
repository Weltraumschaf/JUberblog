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

import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.cmd.SubCommandBase;
import de.weltraumschaf.juberblog.core.TaskExecutor;
import org.joda.time.DateTime;

/**
 * Published the blog (pages, sites, index, site map, feed).
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class PublishSubCommand extends SubCommandBase {

    /**
     * Used to execute some tasks in sequence.
     */
    private final TaskExecutor executor = new TaskExecutor();

    /**
     * Dedicated constructor.
     *
     * @param registry must not be {@code null}
     */
    public PublishSubCommand(final JUberblog registry) {
        super(registry);
    }

    @Override
    public void execute() throws Exception {
        executor.append(new PublishTask(new PublishTask.Config(
                                        configuration().getEncoding(),
                                        directories().getPostsData(),
                                        directories().getPostsOutput(),
                                        templates().getLayoutTemplate(),
                                        templates().getPostTemplate(),
                                        Page.Type.POST,
                                        configuration().getBaseUri().resolve("posts")
                                )))
                .append(new GenerateFeedTask(new GenerateFeedTask.Config(
                                        templates().getFeedTemplate(),
                                        directories().getOutput(),
                                        configuration().getEncoding(),
                                        configuration().getTitle(),
                                        configuration().getBaseUri(),
                                        configuration().getDescription(),
                                        configuration().getLanguage(),
                                        new DateTime("2014-12-08T20:17:00")
                                )))
                .append(new GenerateIndexTask(new GenerateIndexTask.Config(
                                        configuration().getEncoding(),
                                        directories().getOutput(),
                                        templates().getLayoutTemplate(),
                                        templates().getIndexTemplate(),
                                        configuration().getTitle(),
                                        configuration().getDescription()
                                )))
                .append(new PublishTask(new PublishTask.Config(
                                        configuration().getEncoding(),
                                        directories().getSitesData(),
                                        directories().getSiteOutput(),
                                        templates().getLayoutTemplate(),
                                        templates().getSiteTemplate(),
                                        Page.Type.SITE,
                                        configuration().getBaseUri().resolve("sites")
                                )))
                .append(new GenerateSitemapTask(new GenerateSitemapTask.Config(
                                        templates().getSiteMapTemplate(),
                                        directories().getOutput(),
                                        configuration().getEncoding())))
                .execute();
    }

}
