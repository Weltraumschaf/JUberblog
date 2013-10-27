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
package de.weltraumschaf.juberblog.model;

import com.google.common.base.Objects;
import java.util.Locale;
import org.apache.commons.lang3.Validate;

/**
 * Models a site map URL.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class SiteMapUrl {

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

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("loc", loc)
            .add("lastmod", lastmod)
            .add("changefreq", changefreq)
            .add("priority", priority)
            .toString();
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
