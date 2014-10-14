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
package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.formatter.Formatter;
import de.weltraumschaf.juberblog.formatter.Formatters;
import de.weltraumschaf.juberblog.model.DataFile;
import de.weltraumschaf.juberblog.model.Page;
import de.weltraumschaf.juberblog.model.Post;
import de.weltraumschaf.juberblog.model.PublishedPages;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.List;

/**
 * Generates the home site.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class HomeSiteGenerator implements Command {

    /**
     * Template configuration.
     */
    private final Configuration templateConfiguration;
    /**
     * Contains data to generate the home site.
     */
    private final PublishedPages pages;
    private String html = "";

    /**
     * Dedicated constructor.
     *
     * @param templateConfiguration must not be {@literal null}
     * @param pages must not be {@literal null}
     */
    public HomeSiteGenerator(final Configuration templateConfiguration, final PublishedPages pages) {
        super();
        this.templateConfiguration = Validate.notNull(templateConfiguration, "templateConfiguration");
        this.pages = Validate.notNull(pages, "pages");
    }

    @Override
    public void execute() {
        try {
            final List<Post> posts = Lists.newArrayList();

            for (final Page post : Page.filter(pages.values(), DataFile.Type.SITE)) {
                posts.add(new Post(
                    post.getUri().toString(),
                    post.getTitle(),
                    post.getPublishingDate().toString()));
            }

            final Formatter fmt = Formatters.createHomeSiteFormatter(templateConfiguration, posts);
            html = fmt.format();
        } catch (final IOException | TemplateException ex) {
            throw new RuntimeException(ex);
        }
    }

    String getResult() {
        return html;
    }

}
