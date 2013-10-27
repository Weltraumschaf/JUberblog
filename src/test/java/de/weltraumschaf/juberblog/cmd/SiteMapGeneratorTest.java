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
package de.weltraumschaf.juberblog.cmd;

import de.weltraumschaf.juberblog.cmd.SiteMapGenerator.Dir;
import de.weltraumschaf.juberblog.model.SiteMap;
import de.weltraumschaf.juberblog.model.SiteMapUrl;
import de.weltraumschaf.juberblog.template.Configurations;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
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
        sut.addDirecotry(null);
    }

    @Test
    public void postsDir_throwsExceptionIfNotDirectory() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        Dir.posts(tmp.newFile(), "foo");
    }

    public void sitesDir_throwsExceptionIfNotDirectory() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        Dir.sites(tmp.newFile(), "foo");
    }

    private void addfilesToSut() throws IOException {
        final File sites = tmp.newFolder("sites");
        assertThat(new File(sites, "foo.html").createNewFile(), is(true));
        assertThat(new File(sites, "bar.html").createNewFile(), is(true));
        assertThat(new File(sites, "baz.html").createNewFile(), is(true));
        assertThat(new File(sites, "snafu").createNewFile(), is(true));
        assertThat(new File(sites, "README.md").createNewFile(), is(true));
        final File posts = tmp.newFolder("posts");
        assertThat(new File(posts, "foo.html").createNewFile(), is(true));
        assertThat(new File(posts, "bar.html").createNewFile(), is(true));
        assertThat(new File(posts, "snafu.txt").createNewFile(), is(true));
        sut.addDirecotry(Dir.sites(sites, URI + "sites/"));
        sut.addDirecotry(Dir.posts(posts, URI + "posts/"));
//        return new Dirs(sites, posts);
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
    public void findPublishedFiles() throws IOException {
        SiteMap map = sut.findPublishedFiles();
        assertThat(map, is(not(nullValue())));
        assertThat(map.getUrls(), hasSize(0));
        addfilesToSut();
        map = sut.findPublishedFiles();
        assertThat(map, is(not(nullValue())));
        assertThat(map.getUrls(), hasSize(5));
        assertThat(map.getUrls(), containsInAnyOrder(
            new SiteMapUrl(URI + "sites/foo.html", today(), SiteMapUrl.ChangeFrequency.WEEKLY, Dir.SITE_PRIORITY),
            new SiteMapUrl(URI + "sites/bar.html", today(), SiteMapUrl.ChangeFrequency.WEEKLY, Dir.SITE_PRIORITY),
            new SiteMapUrl(URI + "sites/baz.html", today(), SiteMapUrl.ChangeFrequency.WEEKLY, Dir.SITE_PRIORITY),
            new SiteMapUrl(URI + "posts/foo.html", today(), SiteMapUrl.ChangeFrequency.DAILY, Dir.POST_PRIORITY),
            new SiteMapUrl(URI + "posts/bar.html", today(), SiteMapUrl.ChangeFrequency.DAILY, Dir.POST_PRIORITY)
        ));
    }

    @Test
    public void generaeXml() throws IOException, TemplateException {
        final SiteMap map = new SiteMap();
        map.add(new SiteMapUrl(
            URI + "sites/foo.html", "2013-10-27", SiteMapUrl.ChangeFrequency.WEEKLY, Dir.SITE_PRIORITY));
        map.add(new SiteMapUrl(
            URI + "sites/bar.html", "2013-10-27", SiteMapUrl.ChangeFrequency.WEEKLY, Dir.SITE_PRIORITY));
        map.add(new SiteMapUrl(
            URI + "posts/foo.html", "2013-10-27", SiteMapUrl.ChangeFrequency.DAILY, Dir.POST_PRIORITY));
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
    public void execute() throws IOException {
        assertThat(sut.getResult(), is(equalTo("")));
        sut.execute();
        assertThat(sut.getResult(), is(equalTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
            + "</urlset>")));

        addfilesToSut();

        sut.execute();
        assertThat(sut.getResult(), is(equalTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
            + "    <url>\n"
            + "        <loc>http://www.foobar.com/sites/bar.html</loc>\n"
            + "        <lastmod>" + today() + "</lastmod>\n"
            + "        <changefreq>weekly</changefreq>\n"
            + "        <priority>0.5</priority>\n"
            + "    </url>\n"
            + "    <url>\n"
            + "        <loc>http://www.foobar.com/sites/baz.html</loc>\n"
            + "        <lastmod>" + today() + "</lastmod>\n"
            + "        <changefreq>weekly</changefreq>\n"
            + "        <priority>0.5</priority>\n"
            + "    </url>\n"
            + "    <url>\n"
            + "        <loc>http://www.foobar.com/sites/foo.html</loc>\n"
            + "        <lastmod>" + today() + "</lastmod>\n"
            + "        <changefreq>weekly</changefreq>\n"
            + "        <priority>0.5</priority>\n"
            + "    </url>\n"
            + "    <url>\n"
            + "        <loc>http://www.foobar.com/posts/bar.html</loc>\n"
            + "        <lastmod>" + today() + "</lastmod>\n"
            + "        <changefreq>daily</changefreq>\n"
            + "        <priority>1.0</priority>\n"
            + "    </url>\n"
            + "    <url>\n"
            + "        <loc>http://www.foobar.com/posts/foo.html</loc>\n"
            + "        <lastmod>" + today() + "</lastmod>\n"
            + "        <changefreq>daily</changefreq>\n"
            + "        <priority>1.0</priority>\n"
            + "    </url>\n"
            + "</urlset>")));
    }

}
