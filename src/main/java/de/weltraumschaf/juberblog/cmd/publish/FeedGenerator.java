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
     * Publishing date format.
     *
     * See http://www.joda.org/joda-time/key_format.html
     */
    private static final DateTimeFormatter PUBLISH_DATE_FORMAT =
        DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss Z");
    /**
     * DC publishing date format.
     *
     * See http://www.joda.org/joda-time/key_format.html
     */
    private static final DateTimeFormatter DC_DATE_FORMAT =
        DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
    /**
     * Template configuration.
     */
    private final Configuration templateConfiguration;
    /**
     * Pages for the feed.
     */
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
    /**
     * Generated XML string.
     */
    private String xml = "";

    /**
     * Dedicated constructor.
     *
     * @param templateConfiguration must not be {@code null}
     * @param pages must not be {@code null}
     */
    public FeedGenerator(final Configuration templateConfiguration, final List<Page> pages) {
        super();
        Validate.notNull(templateConfiguration, "TemplateConfiguration must not be null!");
        Validate.notNull(pages, "Pages must not be null!");
        this.templateConfiguration = templateConfiguration;
        this.pages = pages;
    }

    /**
     * Set the title.
     *
     * @param title must not be {@code null}
     */
    public void setTitle(final String title) {
        Validate.notNull(pages, "Page must not be null!");
        this.title = title;
    }

    /**
     * Set the link to to the feed.
     *
     * @param link must not be {@code null}
     */
    public void setLink(final String link) {
        Validate.notNull(link, "Link must not be null!");
        this.link = link;
    }

    /**
     * Set feed description.
     *
     * @param description must not be {@code null}
     */
    public void setDescription(final String description) {
        Validate.notNull(description, "Description must not be null!");
        this.description = description;
    }

    /**
     * Set feed language.
     *
     * @param language must not be {@code null}
     */
    public void setLanguage(final String language) {
        Validate.notNull(language, "Language must not be null!");
        this.language = language;
    }

    /**
     * Set last build date of feed.
     *
     * @param lastBuildDate must not be {@code null}
     */
    public void setLastBuildDate(final DateTime lastBuildDate) {
        Validate.notNull(lastBuildDate, "LastBuildDate must not be null!");
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
