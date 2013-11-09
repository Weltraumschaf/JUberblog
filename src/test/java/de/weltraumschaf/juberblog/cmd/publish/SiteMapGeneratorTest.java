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
import de.weltraumschaf.juberblog.model.Page;
import de.weltraumschaf.juberblog.model.SiteMap;
import de.weltraumschaf.juberblog.model.SiteMapUrl;
import de.weltraumschaf.juberblog.template.Configurations;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
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
    public final TemporaryFolder tmp = new TemporaryFolder();
    //CHECKSTYLE:ON
    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON
    private final SiteMapGenerator sut = new SiteMapGenerator(
            Configurations.forTests(Configurations.SCAFFOLD_TEMPLATE_DIR));

    public SiteMapGeneratorTest() throws IOException, URISyntaxException {
        super();
    }

    @Test
    public void addDirecotry_throwsExceptionIfDirIsNull() throws IOException {
        thrown.expect(NullPointerException.class);
        sut.addPage(null);
    }

    private String today() {
        return SiteMapGenerator.formatTimestamp(new DateTime());
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

        sut.addPage(
            new Page("title1", new URI(URI + "sites/foo.html"), "description1", new DateTime(2013, 10, 27, 0, 0),
                    new DataFile(tmp.newFile()), SiteMapUrl.ChangeFrequency.WEEKLY, SiteMapUrl.Priority.SITE));
        sut.addPage(
            new Page("title1", new URI(URI + "sites/bar.html"), "description1", new DateTime(2013, 10, 27, 0, 0),
                    new DataFile(tmp.newFile()), SiteMapUrl.ChangeFrequency.WEEKLY, SiteMapUrl.Priority.SITE));
        sut.addPage(
            new Page("title1", new URI(URI + "posts/foo.html"), "description1", new DateTime(2013, 10, 27, 0, 0),
                    new DataFile(tmp.newFile()), SiteMapUrl.ChangeFrequency.DAILY, SiteMapUrl.Priority.POST));

        sut.execute();
        assertThat(sut.getResult(), is(equalTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
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

}
