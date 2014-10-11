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

import de.weltraumschaf.commons.string.StringEscape;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.formatter.Formatter;
import de.weltraumschaf.juberblog.formatter.Formatters;
import de.weltraumschaf.juberblog.model.Feed;
import de.weltraumschaf.juberblog.model.FeedItem;
import de.weltraumschaf.juberblog.model.Page;
import de.weltraumschaf.juberblog.model.PublishedPages;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
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
 * FIXME Renders the description with the whole layout. Only add renderd content of layout.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class FeedGenerator implements Command {

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
    private final PublishedPages pages;
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
     * @param templateConfiguration must not be {@literal null}
     * @param pages must not be {@literal null}
     */
    public FeedGenerator(final Configuration templateConfiguration, final PublishedPages pages) {
        super();
        Validate.notNull(templateConfiguration, "templateConfiguration");
        Validate.notNull(pages, "pages");
        this.templateConfiguration = templateConfiguration;
        this.pages = pages;
    }

    /**
     * Set the title.
     *
     * @param title must not be {@literal null}
     */
    public void setTitle(final String title) {
        Validate.notNull(pages, "pages");
        this.title = title;
    }

    /**
     * Set the link to to the feed.
     *
     * @param link must not be {@literal null}
     */
    public void setLink(final String link) {
        Validate.notNull(link, "link");
        this.link = link;
    }

    /**
     * Set feed description.
     *
     * @param description must not be {@literal null}
     */
    public void setDescription(final String description) {
        this.description = Validate.notNull(description, "description");
    }

    /**
     * Set feed language.
     *
     * @param language must not be {@literal null}
     */
    public void setLanguage(final String language) {
        Validate.notNull(language, "language");
        this.language = language;
    }

    /**
     * Set last build date of feed.
     *
     * @param lastBuildDate must not be {@literal null}
     */
    public void setLastBuildDate(final DateTime lastBuildDate) {
        Validate.notNull(lastBuildDate, "lastBuildDate");
        this.lastBuildDate = lastBuildDate;
    }

    @Override
    public void execute() {
        final Feed feed = new Feed(
            title,
            link,
            description,
            language,
            lastBuildDate.toString(PUBLISH_DATE_FORMAT));

        for (final Page page : pages.values()) {
            feed.add(
                new FeedItem(
                    page.getTitle(),
                    page.getUri().toString(),
                    StringEscape.escapeXml(page.getDescription()),
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
     * @return never {@literal null}, maybe empty until {@link #execute()} is called
     */
    public String getResult() {
        return xml;
    }

    /**
     * Formates a date time according to {@link #PUBLISH_DATE_FORMAT}.
     *
     * @param ts must not be {@literal null}
     * @return never {@literal null}
     */
    static String formatTimestamp(final DateTime ts) {
        Validate.notNull(ts, "Ts must not be null!");
        return ts.toString(PUBLISH_DATE_FORMAT);
    }

    /**
     * Formates a date time according to {@link #DC_DATE_FORMAT}.
     *
     * @param ts must not be {@literal null}
     * @return never {@literal null}
     */
    static String formatDcDate(final DateTime ts) {
        Validate.notNull(ts, "Ts must not be null!");
        return ts.toString(DC_DATE_FORMAT);
    }

}
