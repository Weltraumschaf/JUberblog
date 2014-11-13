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

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.juberblog.formatter.Formatter;
import de.weltraumschaf.juberblog.formatter.Formatters;
import de.weltraumschaf.juberblog.model.Page;
import de.weltraumschaf.juberblog.model.PublishedPages;
import de.weltraumschaf.juberblog.model.SiteMap;
import de.weltraumschaf.juberblog.model.SiteMapUrl;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.nio.file.Path;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Generates the site map XML.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class SiteMapGenerator implements Command {

    /**
     * Format string for %gt;lastmod&lt; tag.
     *
     * See http://www.joda.org/joda-time/key_format.html
     */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");
    /**
     * Directories with published files.
     */
    private final PublishedPages pages;
    /**
     * used to render XML.
     */
    private final Configuration templateConfiguration;
    private final Path templateDir;
    /**
     * Computed by {@link #execute()}.
     */
    private String xml = "";

    /**
     * Dedicated constructor.
     *
     * @param templateConfiguration must not be {@literal null}
     * @param pages must not be {@literal null}
     */
    public SiteMapGenerator(final Configuration templateConfiguration, final PublishedPages pages, final Path templateDir) {
        super();
        this.templateConfiguration = Validate.notNull(templateConfiguration, "templateConfiguration");
        this.pages = Validate.notNull(pages, "pages");
        this.templateDir = Validate.notNull(templateDir, "templateDir");
    }

    @Override
    public void execute() {
        final SiteMap map = findPublishedFiles();
        try {
            xml = generaeXml(map);
        } catch (final IOException | TemplateException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Find published files for all {@link #pages firecotries}.
     *
     * @return never {@literal null}
     */
    SiteMap findPublishedFiles() {
        final SiteMap map = new SiteMap();

        for (final Page page : pages.values()) {
            map.add(new SiteMapUrl(
                    page.getUri().toString(),
                    formatTimestamp(page.getPublishingDate()),
                    page.getFrequencey(),
                    page.getPriority()));
        }

        return map;
    }

    /**
     *
     * @param map must not be {@literal null}
     * @return never {@literal null}
     * @throws IOException on any I/O error
     * @throws TemplateException on any template error
     */
    String generaeXml(final SiteMap map) throws IOException, TemplateException {
        Validate.notNull(map, "Map must not be null!");
        final Formatter fmt = Formatters.createSiteMapFormatter(templateConfiguration, map, templateDir);
        fmt.setEncoding(templateConfiguration.getDefaultEncoding());
        return fmt.format();
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
     * Formates a date time according to {@link #DATE_FORMAT}.
     *
     * @param ts must not be {@literal null}
     * @return never {@literal null}
     */
    static String formatTimestamp(final DateTime ts) {
        return ts.toString(DATE_FORMAT);
    }

    /**
     * Formates a unix timestamp according to {@link #DATE_FORMAT}.
     *
     * @param ts any valid unix time stamp
     * @return never {@literal null}
     */
    static String formatTimestamp(final long ts) {
        return formatTimestamp(new DateTime(ts));
    }

}
