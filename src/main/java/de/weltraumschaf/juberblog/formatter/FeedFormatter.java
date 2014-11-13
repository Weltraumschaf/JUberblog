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
import de.weltraumschaf.freemarkerdown.Fragment;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.juberblog.model.Feed;
import de.weltraumschaf.juberblog.template.VarName;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Formats an RSS feed.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class FeedFormatter implements Formatter {

    /**
     * Template for XML.
     */
    private static final String TEMPLATE = "feed.ftl";
    /**
     * used to render XML.
     */
    private final Fragment content;
    /**
     * Data model.
     */
    private final Feed feed;
    private final FreeMarkerDown fmd;

    /**
     * Dedicated constructor.
     *
     * @param templateConfiguration must not be {@literal nul}
     * @param feed must not be {@literal nul}
     * @throws IOException on any template IO error
     */
    public FeedFormatter(final Configuration templateConfiguration, final Feed feed, final Path templateDir) throws IOException {
        super();
        Validate.notNull(templateConfiguration, "Template configuration must not be null!");
        fmd = FreeMarkerDown.create(templateConfiguration);
        Validate.notNull(feed, "Feedmust not be null!");
        content = fmd.createFragemnt(templateDir.resolve(TEMPLATE));
        content.assignVariable(VarName.ENCODING.toString(), templateConfiguration.getDefaultEncoding());
        this.feed = feed;
    }

    @Override
    public String format() throws IOException, TemplateException {
        content.assignVariable("feed", feed);
        return content.render();
    }

    @Override
    public void setEncoding(final String encoding) {
        content.assignVariable(VarName.ENCODING.toString(), encoding);
    }

}
