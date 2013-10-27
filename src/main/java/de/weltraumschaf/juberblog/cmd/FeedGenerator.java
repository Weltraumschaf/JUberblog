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

package de.weltraumschaf.juberblog.cmd;

import de.weltraumschaf.juberblog.formatter.Formatter;
import de.weltraumschaf.juberblog.formatter.Formatters;
import de.weltraumschaf.juberblog.model.Feed;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;

/**
 * Generates the feed XML.
 *
 * 1. read all files in posts/ with mod date
 * 2. generate feed object from files
 * 3. write feed.xml
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class FeedGenerator implements Command {

    private final Configuration templateConfiguration;
    private String xml = "";

    public FeedGenerator(Configuration templateConfiguration) {
        this.templateConfiguration = templateConfiguration;
    }

    @Override
    public void execute() {
        final Feed feed = findPublishedFiles();
        
        try {
            final Formatter fmt = Formatters.createFeedFormatter(templateConfiguration, feed);
            xml = fmt.format();
        } catch (final IOException | TemplateException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Get the rendered XML string.
     *
     * @return never {@code null}, maybe empty until {@link #execute()} is called
     */
    public String getResult() {
        return xml;
    }

    Feed findPublishedFiles() {
        return null;
    }
}
