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

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import de.weltraumschaf.juberblog.template.Template;
import de.weltraumschaf.juberblog.template.VarName;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.Validate;

/**
 * Formats a site map to a string.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
class SiteMapFormatter implements Formatter {

    /**
     * Template for XML.
     */
    private static final String TEMPLATE = "site_map.ftl";
    /**
     * used to render XML.
     */
    private final Template content;
    /**
     * The formatted site map.
     */
    private final SiteMap siteMap;

    /**
     * Dedicated constructor.
     *
     * @param templateConfiguration must not be {@code null}
     * @param siteMap must not be {@code null}
     * @throws IOException on any template I/O error
     */
    public SiteMapFormatter(final Configuration templateConfiguration, final SiteMap siteMap) throws IOException {
        super();
        Validate.notNull(templateConfiguration, "Template configuration must not be null!");
        Validate.notNull(siteMap, "SiteMap not be null!");
        content = new Template(templateConfiguration, TEMPLATE);
        this.siteMap = siteMap;
    }

    @Override
    public void setEncoding(String encoding) {
        content.assignVariable(VarName.ENCODING, encoding);
    }

    @Override
    public String format() throws IOException, TemplateException {
        content.assignVariable("urls", siteMap.getUrls());
        return content.render();
    }

    /**
     * Models the site map data.
     *
     * See http://www.sitemaps.org/schemas/sitemap/sitemap.xsd
     */
    public static final class SiteMap {

        /**
         * Site map URLs.
         */
        private final List<SiteMapUrl> urls = Lists.newArrayList();

        /**
         * Add an URL to the site map.
         *
         * @param url must not be {@code null}
         */
        public void add(final SiteMapUrl url) {
            urls.add(url);
        }

        /**
         * Get site map URLs.
         *
         * @return never {@code null}
         */
        public List<SiteMapUrl> getUrls() {
            return urls;
        }

    }

    /**
     * Models a site map URL.
     */
    public static final class SiteMapUrl {

        /**
         * The location as URI.
         */
        private final String loc;
        /**
         * The last modification date.
         */
        private final String lastmod;
        /**
         * The change frequency of the content.
         */
        private final String changefreq;
        /**
         * Site priority.
         */
        private final String priority;

        /**
         * Dedicated constructor.
         *
         * @param loc must not be {@code null} or empty
         * @param lastmod must not be {@code null} or empty
         * @param changefreq must not be {@code null}
         * @param priority must not be less than 0.0
         */
        public SiteMapUrl(String loc, String lastmod, ChangeFrequency changefreq, float priority) {
            super();
            Validate.notEmpty(loc, "Loc must not be null or empty!");
            Validate.notEmpty(lastmod, "Lastmod must not be null or empty!");
            Validate.notNull(changefreq, "Changefreq must not be null!");
            Validate.isTrue(priority >= 0.0f, "Priority must not be less than 0.0!");
            this.loc = loc;
            this.lastmod = lastmod;
            this.changefreq = changefreq.toString();
            this.priority = String.format(Locale.ENGLISH, "%.1f", priority);
        }

        /**
         * Get the location URI of the site.
         *
         * @return never {@code null} or empty
         */
        public String getLoc() {
            return loc;
        }

        /**
         * Get the last modification date of the site.
         *
         * @return never {@code null} or empty
         */
        public String getLastmod() {
            return lastmod;
        }

        /**
         * Get the change frequency of the site.
         *
         * @return never {@code null}
         */
        public String getChangefreq() {
            return changefreq;
        }

        /**
         * Get the priority of the site.
         *
         * Formatted to EN decimal number with 1 digit after period.
         *
         * @return never less than 0.0
         */
        public String getPriority() {
            return priority;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(loc, lastmod, changefreq, priority);
        }

        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof SiteMapUrl)) {
                return false;
            }

            final SiteMapUrl other = (SiteMapUrl) obj;
            return Objects.equal(loc, other.loc)
                    && Objects.equal(lastmod, other.lastmod)
                    && Objects.equal(changefreq, other.changefreq)
                    && Objects.equal(priority, other.priority);
        }

    }

    /**
     * Available change frequencies.
     */
    public enum ChangeFrequency {

        /**
         * Content changes always.
         */
        ALWAYS,
        /**
         * Content changes hourly.
         */
        HOURLY,
        /**
         * Content changes daily.
         */
        DAILY,
        /**
         * Content changes weekly.
         */
        WEEKLY,
        /**
         * Content changes monthly.
         */
        MONTHLY,
        /**
         * Content changes yearly.
         */
        YEARLY,
        /**
         * Content changes never.
         */
        NEVER;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

    }
}
