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

import com.google.common.collect.Lists;
import java.util.List;

/**
 * Models the site map data.
 *
 * See http://www.sitemaps.org/schemas/sitemap/sitemap.xsd
 */
public final class SiteMap {

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
