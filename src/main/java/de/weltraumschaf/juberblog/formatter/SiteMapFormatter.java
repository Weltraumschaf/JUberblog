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

import com.google.common.collect.Lists;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SiteMapFormatter implements Formatter {

    private final SiteMap map;

    public SiteMapFormatter(SiteMap map) {
        this.map = map;
    }

    @Override
    public String format(InputStream template) throws IOException, TemplateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String format(String template) throws IOException, TemplateException {
        for (final SiteMapEntry entry : map) {

        }

        return "";
    }

    @Override
    public void setEncoding(String encoding) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * http://www.sitemaps.org/schemas/sitemap/sitemap.xsd
     */
    public static final class SiteMap implements Iterable<SiteMapEntry> {

        private final List<SiteMapEntry> entries = Lists.newArrayList();

        public boolean add(SiteMapEntry e) {
            return entries.add(e);
        }

        @Override
        public Iterator<SiteMapEntry> iterator() {
            return entries.iterator();
        }

    }

    public static final class SiteMapEntry {

        private final String loc;
        private final String lastmod;
        private final String changefreq;
        private final String priority;

        public SiteMapEntry(String loc, String lastmod, ChangeFrequency changefreq, String priority) {
            super();
            this.loc = loc;
            this.lastmod = lastmod;
            this.changefreq = changefreq.toString();
            this.priority = priority;
        }

        public String getLoc() {
            return loc;
        }

        public String getLastmod() {
            return lastmod;
        }

        public String getChangefreq() {
            return changefreq;
        }

        public String getPriority() {
            return priority;
        }

        public enum ChangeFrequency {

            ALWAYS,
            HOURLY,
            DAILY,
            WEEKLY,
            MONTHLY,
            YEARLY,
            NEVER;

            @Override
            public String toString() {
                return name().toLowerCase();
            }

        }
    }
}
