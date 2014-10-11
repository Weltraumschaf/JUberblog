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

import de.weltraumschaf.juberblog.model.DataFile;
import de.weltraumschaf.juberblog.model.MetaData;
import de.weltraumschaf.juberblog.model.Page;
import de.weltraumschaf.juberblog.model.PublishedPages;
import de.weltraumschaf.juberblog.model.SiteMap;
import de.weltraumschaf.juberblog.model.SiteMapUrl;
import de.weltraumschaf.juberblog.template.Configurations;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.joda.time.DateTime;

/**
 * Tests for {@link SiteMapGenerator}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SiteMapGeneratorTest {

    private static final String URI = "http://www.foobar.com/";

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    private final PublishedPages pages = new PublishedPages();
    private final SiteMapGenerator sut = new SiteMapGenerator(
            Configurations.forTests(Configurations.SCAFFOLD_TEMPLATE_DIR),
            pages);

    private final DataFile dummy1 = new DataFile(
        "file1", "file1", 1L, 1L, "file1", "file1", "md", new MetaData(), DataFile.Type.SITE);
    private final DataFile dummy2 = new DataFile(
        "file2", "file2", 2L, 2L, "file2", "file2", "md", new MetaData(), DataFile.Type.SITE);
    private final DataFile dummy3 = new DataFile(
        "file3", "file3", 3L, 3L, "file2", "file3", "md", new MetaData(), DataFile.Type.SITE);

    public SiteMapGeneratorTest() throws IOException, URISyntaxException {
        super();
    }

    @Test
    public void formatTimestamp() {
        assertThat(SiteMapGenerator.formatTimestamp(123456L), is(equalTo("1970-01-01")));
        assertThat(SiteMapGenerator.formatTimestamp(1382901066000L), is(equalTo("2013-10-27")));
    }

    @Test
    public void generaeXml() throws IOException, TemplateException {
        final SiteMap map = new SiteMap();
        map.add(new SiteMapUrl(
                URI + "sites/foo.html", "2013-10-27", SiteMapUrl.ChangeFrequency.WEEKLY, SiteMapUrl.Priority.SITE));
        map.add(new SiteMapUrl(
                URI + "sites/bar.html", "2013-10-27", SiteMapUrl.ChangeFrequency.WEEKLY, SiteMapUrl.Priority.SITE));
        map.add(new SiteMapUrl(
                URI + "posts/foo.html", "2013-10-27", SiteMapUrl.ChangeFrequency.DAILY, SiteMapUrl.Priority.POST));
        assertThat(sut.generaeXml(map), is(equalTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
                + "    <url>\n"
                + "        <loc>http://www.foobar.com/sites/foo.html</loc>\n"
                + "        <lastmod>2013-10-27</lastmod>\n"
                + "        <changefreq>weekly</changefreq>\n"
                + "        <priority>0.5</priority>\n"
                + "    </url>\n"
                + "    <url>\n"
                + "        <loc>http://www.foobar.com/sites/bar.html</loc>\n"
                + "        <lastmod>2013-10-27</lastmod>\n"
                + "        <changefreq>weekly</changefreq>\n"
                + "        <priority>0.5</priority>\n"
                + "    </url>\n"
                + "    <url>\n"
                + "        <loc>http://www.foobar.com/posts/foo.html</loc>\n"
                + "        <lastmod>2013-10-27</lastmod>\n"
                + "        <changefreq>daily</changefreq>\n"
                + "        <priority>1.0</priority>\n"
                + "    </url>\n"
                + "</urlset>")));
    }

    @Test
    public void execute() throws IOException, URISyntaxException {
        assertThat(sut.getResult(), is(equalTo("")));
        sut.execute();
        assertThat(sut.getResult(), is(equalTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
                + "</urlset>")));

        pages.put(
                new Page("title1", new URI(URI + "sites/foo.html"), "description1", new DateTime(2013, 10, 27, 0, 0),
                        dummy1, SiteMapUrl.ChangeFrequency.WEEKLY, SiteMapUrl.Priority.SITE));
        pages.put(
                new Page("title2", new URI(URI + "sites/bar.html"), "description2", new DateTime(2013, 10, 28, 0, 0),
                        dummy2, SiteMapUrl.ChangeFrequency.WEEKLY, SiteMapUrl.Priority.SITE));
        pages.put(
                new Page("title3", new URI(URI + "posts/foo.html"), "description3", new DateTime(2013, 10, 29, 0, 0),
                        dummy3, SiteMapUrl.ChangeFrequency.DAILY, SiteMapUrl.Priority.POST));

        sut.execute();
        assertThat(sut.getResult(), is(equalTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
                + "    <url>\n"
                + "        <loc>http://www.foobar.com/sites/bar.html</loc>\n"
                + "        <lastmod>2013-10-28</lastmod>\n"
                + "        <changefreq>weekly</changefreq>\n"
                + "        <priority>0.5</priority>\n"
                + "    </url>\n"
                + "    <url>\n"
                + "        <loc>http://www.foobar.com/posts/foo.html</loc>\n"
                + "        <lastmod>2013-10-29</lastmod>\n"
                + "        <changefreq>daily</changefreq>\n"
                + "        <priority>1.0</priority>\n"
                + "    </url>\n"
                + "    <url>\n"
                + "        <loc>http://www.foobar.com/sites/foo.html</loc>\n"
                + "        <lastmod>2013-10-27</lastmod>\n"
                + "        <changefreq>weekly</changefreq>\n"
                + "        <priority>0.5</priority>\n"
                + "    </url>\n"
                + "</urlset>")));
    }

}
