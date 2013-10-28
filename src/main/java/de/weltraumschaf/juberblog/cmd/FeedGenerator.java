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
import de.weltraumschaf.juberblog.model.FeedItem;
import de.weltraumschaf.juberblog.model.Page;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.List;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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

    /**
     * See http://www.joda.org/joda-time/key_format.html
     */
    private static final DateTimeFormatter PUBLISH_DATE_FORMAT =
        DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss Z");
    private static final DateTimeFormatter DC_DATE_FORMAT =
        DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
    private final Configuration templateConfiguration;

    private final List<Page> pages;
    /**
     * Feed title.
     */
    private String title = "";
    /**
     * Feed URI.
     */
    private String link = "";
    /**
     * Feed description.
     */
    private String description = "";
    /**
     * Feed language.
     */
    private String language = "";
    /**
     * Feed generation.
     */
    private DateTime lastBuildDate = new DateTime();
    private String xml = "";

    public FeedGenerator(final Configuration templateConfiguration, final List<Page> pages) {
        super();
        Validate.notNull(templateConfiguration, "TemplateConfiguration must not be null!");
        Validate.notNull(pages, "Pages must not be null!");
        this.templateConfiguration = templateConfiguration;
        this.pages = pages;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public void setLastBuildDate(final DateTime lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    @Override
    public void execute() {
        final Feed feed = new Feed(title, link, description, language, lastBuildDate.toString(PUBLISH_DATE_FORMAT));

        for (final Page page : pages) {
            feed.add(
                new FeedItem(
                    page.getTitle(),
                    page.getLink().toString(),
                    StringEscapeUtils.escapeXml(page.getDescription()),
                    formatTimestamp(page.getPublishingDate()),
                    formatDcDate(page.getPublishingDate())));
        }

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

    /**
     * Formates a date time according to {@link #PUBLISH_DATE_FORMAT}.
     *
     * @param ts must not be {@code null}
     * @return never {@code null}
     */
    static String formatTimestamp(final DateTime ts) {
        Validate.notNull(ts, "Ts must not be null!");
        return ts.toString(PUBLISH_DATE_FORMAT);
    }

    /**
     * Formates a date time according to {@link #DC_DATE_FORMAT}.
     *
     * @param ts must not be {@code null}
     * @return never {@code null}
     */
    static String formatDcDate(final DateTime ts) {
        Validate.notNull(ts, "Ts must not be null!");
        return ts.toString(DC_DATE_FORMAT);
    }

}
