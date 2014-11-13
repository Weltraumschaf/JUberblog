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
import java.nio.file.Path;
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
    private final Configuration templateConfig;
    private final Path templateDir;
    private String headline = "";
    private String description = "";
    private String version = "";
    /**
     * Contains data to generate the home site.
     */
    private PublishedPages pages = new PublishedPages();
    private String html = "";

    /**
     * Dedicated constructor.
     *
     * @param templateConfig must not be {@literal null}
     */
    public HomeSiteGenerator(final Configuration templateConfig, final Path templateDir) {
        super();
        this.templateConfig = Validate.notNull(templateConfig, "templateConfig");
        this.templateDir = Validate.notNull(templateDir, "templateDir");
    }

    public void setPages(final PublishedPages pages) {
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

            final Formatter fmt = Formatters.createHomeSiteFormatter(templateConfig, posts, headline, description, version, templateDir);
            html = fmt.format();
        } catch (final IOException | TemplateException ex) {
            throw new RuntimeException(ex);
        }
    }

    String getResult() {
        return html;
    }

    void setHeadline(final String headline) {
        this.headline = Validate.notNull(headline, "headline");
    }

    void setVersion(final String version) {
        this.version = Validate.notNull(version, "version");
    }

    void setDescription(final String description) {
        this.description = Validate.notNull(description, "description");
    }

}
