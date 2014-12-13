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

import de.weltraumschaf.commons.validate.Validate;
import java.nio.file.Path;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class Templates {

    private final Path layoutTemplate;
    private final Path postTemplate;
    private final Path siteTemplate;
    private final Path feedTemplate;
    private final Path indexTemplate;
    private final Path siteMapTemplate;

    Templates(
            final Path layoutTemplate,
            final Path postTemplate,
            final Path siteTemplate,
            final Path feedTemplate,
            final Path indexTemplate,
            final Path siteMapTemplate)
    {
        super();
        this.layoutTemplate = Validate.notNull(layoutTemplate, "layoutTemplate");
        this.postTemplate = Validate.notNull(postTemplate, "postTemplate");
        this.siteTemplate = Validate.notNull(siteTemplate, "siteTemplate");
        this.feedTemplate = Validate.notNull(feedTemplate, "feedTemplate");
        this.indexTemplate = Validate.notNull(indexTemplate, "indexTemplate");
        this.siteMapTemplate = Validate.notNull(siteMapTemplate, "siteMapTemplate");
    }

    Path getLayoutTemplate() {
        return layoutTemplate;
    }

    Path getPostTemplate() {
        return postTemplate;
    }

    Path getSiteTemplate() {
        return siteTemplate;
    }

    Path getFeedTemplate() {
        return feedTemplate;
    }

    Path getIndexTemplate() {
        return indexTemplate;
    }

    Path getSiteMapTemplate() {
        return siteMapTemplate;
    }

}
