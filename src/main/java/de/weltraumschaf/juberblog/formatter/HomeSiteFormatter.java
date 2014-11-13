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

package de.weltraumschaf.juberblog.formatter;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.model.Post;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class HomeSiteFormatter extends BaseFormatter {

    /**
     * Post template file name.
     */
    private static final String TEMPLATE = "index.ftl";
    private final List<Post> posts;
    private final String headline;
    private final String description;
    private final String version;

    /**
     * Dedicated constructor.
     *
     * @param templateConfiguration must not be {@code null} or empty
     * @param posts must not be {@code null}
     * @param headline must not be {@code null}
     * @param description must not be {@code null}
     * @param version must not be {@code null}
     * @throws IOException if template file can't be read
     */
    public HomeSiteFormatter(final Configuration templateConfiguration, final List<Post> posts, final String headline, final String description, final String version, final Path templateDir) throws IOException {
        super(templateConfiguration, TEMPLATE, "BLA", templateDir);
        this.posts = Validate.notNull(posts, "posts");
        this.headline = Validate.notNull(headline, "title");
        this.description = Validate.notNull(description, "description");
        this.version = Validate.notNull(version, "version");
    }

    @Override
    public String format() throws IOException, TemplateException {
        content.assignVariable("posts", posts);
        setHeadline(headline);
        setDescription(description);
        setVersion(version);
        return super.format();
    }


}
