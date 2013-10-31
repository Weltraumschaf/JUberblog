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

import com.google.common.collect.Lists;
import de.weltraumschaf.juberblog.files.FilenameFilters;
import de.weltraumschaf.juberblog.files.PublishedFile;
import de.weltraumschaf.juberblog.formatter.Formatter;
import de.weltraumschaf.juberblog.formatter.Formatters;
import de.weltraumschaf.juberblog.model.SiteMap;
import de.weltraumschaf.juberblog.model.SiteMapUrl;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Generates the site map XML.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class SiteMapGenerator implements Command {

    /**
     * Format string for %gt;lastmod&lt; tag.
     *
     * See http://www.joda.org/joda-time/key_format.html
     */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");
    /**
     * Directories with published files.
     */
    private final List<Dir> directoriesWithPublishedFiles = Lists.newArrayList();
    /**
     * used to render XML.
     */
    private final Configuration templateConfiguration;
    /**
     * Computed by {@link #execute()}.
     */
    private String xml = "";

    /**
     * Dedicated constructor.
     *
     * @param templateConfiguration must not be {@code null}
     */
    public SiteMapGenerator(final Configuration templateConfiguration) {
        super();
        Validate.notNull(templateConfiguration);
        this.templateConfiguration = templateConfiguration;
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
     * Add directory to find published files with their base URI.
     *
     * @param baseUri must not be {@literal nul} or empty
     * @param directory must not be {@literal null}, must be directory
     */
    public void addDirecotry(final Dir dir) {
        Validate.notNull(dir, "Dir must not be null!");
        directoriesWithPublishedFiles.add(dir);
    }

    /**
     * Find published files for all {@link #directoriesWithPublishedFiles firecotries}.
     *
     * @return never {@code null}
     */
    SiteMap findPublishedFiles() {
        final SiteMap map = new SiteMap();

        for (final Dir dir : directoriesWithPublishedFiles) {
            final File[] files = dir.getDir().listFiles(FilenameFilters.findHtmlFiles());
            Arrays.sort(files);

            for (final File publishedFile : files) {
                final PublishedFile published = new PublishedFile(dir.getBaseUri(), publishedFile);
                map.add(new SiteMapUrl(
                        published.getUri(),
                        formatTimestamp(published.getModificationTime()),
                        dir.getFrequencey(),
                        dir.getPriority()));
            }
        }

        return map;
    }

    /**
     *
     * @param map must not be {@code null}
     * @return never {@code null}
     * @throws IOException on any I/O error
     * @throws TemplateException on any template error
     */
    String generaeXml(final SiteMap map) throws IOException, TemplateException {
        Validate.notNull(map, "Map must not be null!");
        final Formatter fmt = Formatters.createSiteMapFormatter(templateConfiguration, map);
        fmt.setEncoding(templateConfiguration.getDefaultEncoding());
        return fmt.format();
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
     * Formates a date time according to {@link #DATE_FORMAT}.
     *
     * @param ts must not be {@code null}
     * @return never {@code null}
     */
    static String formatTimestamp(final DateTime ts) {
        return ts.toString(DATE_FORMAT);
    }

    /**
     * Formates a unix timestamp according to {@link #DATE_FORMAT}.
     *
     * @param ts any valid unix time stamp
     * @return never {@code null}
     */
    static String formatTimestamp(final long ts) {
        return formatTimestamp(new DateTime(ts));
    }

    /**
     * A directory to scan for published files.
     */
    public static abstract class Dir {
        /** Priority for posts. */
        public static final float POST_PRIORITY = 1.0f;
        /** Priority for sites. */
        public static final float SITE_PRIORITY = 0.5f;
        /**
         * Change frequency for all published files in the directory.
         */
        private final SiteMapUrl.ChangeFrequency frequencey;
        /**
         * Priority for all published files in the directory.
         */
        private final float priority;
        /**
         * Directory to scan for published files.
         */
        private final File dir;
        /**
         * Base URI for all published sites in {@link #dir direcotry}.
         */
        private final String baseUri;

        /**
         * Dedicated constructor.
         *
         * @param frequencey mist not be {@code null}
         * @param priority [0.0 - 1.0]
         * @param dir must not be {@code null} and must be a directory
         * @param baseUri must not be {@code null}
         */
        Dir(final SiteMapUrl.ChangeFrequency frequencey, final float priority, final File dir, final String baseUri) {
            super();
            Validate.notNull(frequencey, "Frequencey must not be null!");
            Validate.isTrue(priority >= 0.0f && priority <= 1.0f, "Priority must be in range [0.0 - 1.0]!");
            Validate.notNull(dir, "Dir must not be null!");
            Validate.isTrue(dir.isDirectory(), "Diry must be directory!");
            Validate.notNull(baseUri, "BaseUri must not be null!");
            this.frequencey = frequencey;
            this.priority = priority;
            this.dir = dir;
            this.baseUri = baseUri;
        }

        /**
         * Create a directory containing posts.
         *
         * @param dir must not be {@code null} and must be a directory
         * @param baseUri must not be {@code null}
         * @return never {@code null}, always new instance
         */
        public static Dir posts(final File dir, final String baseUri) {
            return new Posts(dir, baseUri);
        }

        /**
         * Create a directory containing sites.
         *
         * @param dir must not be {@code null} and must be a directory
         * @param baseUri must not be {@code null}
         * @return never {@code null}, always new instance
         */
        public static Dir sites(final File dir, final String baseUri) {
            return new Sites(dir, baseUri);
        }

        /**
         * Get the change frequency.
         *
         * @return never {@code null}
         */
        public SiteMapUrl.ChangeFrequency getFrequencey() {
            return frequencey;
        }

        /**
         * Get the priority.
         *
         * @return [0.0 - 1.0]
         */
        public float getPriority() {
            return priority;
        }

        /**
         * Get the directory.
         *
         * @return never {@code null}
         */
        public File getDir() {
            return dir;
        }

        /**
         * Get the base URI.
         *
         * @return never {@code null}
         */
        public String getBaseUri() {
            return baseUri;
        }

    }

    /**
     * Posts directory.
     */
    static final class Posts extends Dir {

        /**
         * Initializes {@link Dir#frequencey} with {@link SiteMapUrl.ChangeFrequency#DAILY}
         * and {@link Dir#priority} with {@link Dir#POST_PRIORITY}.
         *
         * @param dir must not be {@code null} and must be a directory
         * @param baseUri must not be {@code null}
         */
        Posts(final File dir, final String baseUri) {
            super(SiteMapUrl.ChangeFrequency.DAILY, POST_PRIORITY, dir, baseUri);
        }

    }

    /**
     * Sites directory.
     */
    static final class Sites extends Dir {

        /**
         * Initializes {@link Dir#frequencey} with {@link SiteMapUrl.ChangeFrequency#WEEKLY}
         * and {@link Dir#priority} with {@link Dir#SITE_PRIORITY}.
         *
         * @param dir must not be {@code null} and must be a directory
         * @param baseUri must not be {@code null}
         */
        Sites(final File dir, final String baseUri) {
            super(SiteMapUrl.ChangeFrequency.WEEKLY, SITE_PRIORITY, dir, baseUri);
        }

    }
}
