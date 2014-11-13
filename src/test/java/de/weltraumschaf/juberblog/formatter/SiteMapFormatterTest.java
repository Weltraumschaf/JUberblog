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

import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.model.SiteMap;
import de.weltraumschaf.juberblog.model.SiteMapUrl;
import de.weltraumschaf.juberblog.template.Configurations;
import de.weltraumschaf.juberblog.template.TemplateDirectories;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link SiteMapFormatter}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SiteMapFormatterTest {

    private static final String FIXTURE_PACKAGE = Constants.PACKAGE_BASE.toString() + "/formatter";
    private final SiteMap siteMap = new SiteMap();
    private final SiteMapFormatter sut;

    public SiteMapFormatterTest() throws IOException, URISyntaxException {
        super();
        sut = new SiteMapFormatter(
                Configurations.forTests(),
                siteMap,
                TemplateDirectories.scaffold());
        sut.setEncoding(Constants.DEFAULT_ENCODING.toString());
    }

    @Test
    public void format_empty() throws IOException, URISyntaxException, TemplateException {
        final InputStream htmlFile = getClass().getResourceAsStream(FIXTURE_PACKAGE + "/sitemap_empty.xml");
        assertThat(sut.format(), is(equalTo(IOUtils.toString(htmlFile))));
        IOUtils.closeQuietly(htmlFile);
    }

    @Test
    public void format_oneSite() throws IOException, URISyntaxException, TemplateException {
        siteMap.add(new SiteMapUrl(
                "loc1", "lastmod1", SiteMapUrl.ChangeFrequency.DAILY, SiteMapUrl.Priority.POST));
        final InputStream htmlFile = getClass().getResourceAsStream(FIXTURE_PACKAGE + "/sitemap_one.xml");
        assertThat(sut.format(), is(equalTo(IOUtils.toString(htmlFile))));
        IOUtils.closeQuietly(htmlFile);
    }

    @Test
    public void format_threeSites() throws IOException, URISyntaxException, TemplateException {
        siteMap.add(new SiteMapUrl(
                "loc1", "lastmod1", SiteMapUrl.ChangeFrequency.DAILY, SiteMapUrl.Priority.POST));
        siteMap.add(new SiteMapUrl(
                "loc2", "lastmod2", SiteMapUrl.ChangeFrequency.WEEKLY, SiteMapUrl.Priority.POST));
        siteMap.add(new SiteMapUrl(
                "loc3", "lastmod3", SiteMapUrl.ChangeFrequency.MONTHLY, SiteMapUrl.Priority.SITE));
        final InputStream htmlFile = getClass().getResourceAsStream(FIXTURE_PACKAGE + "/sitemap_three.xml");
        assertThat(sut.format(), is(equalTo(IOUtils.toString(htmlFile))));
        IOUtils.closeQuietly(htmlFile);
    }

}
