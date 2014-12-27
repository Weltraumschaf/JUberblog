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
package integrationtests;

import de.weltraumschaf.juberblog.IntegrationTestCase;
import de.weltraumschaf.juberblog.app.App;
import de.weltraumschaf.juberblog.file.DataFile;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests the publishing of a whole blog.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PublishTest extends IntegrationTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void publishWholeBlog() throws URISyntaxException, IOException {
        final Dirs dirs = createDirs();
        copyData(dirs);
        copyTemplates(dirs);

        App.main(createApp(new String[]{
            "publish",
            "-c", createPath("/integrationtests/", "config.properties").toString(),
            "-l", tmp.getRoot().getAbsolutePath()}));

        final DataFile expectedPostOne = new DataFile(tmp.getRoot().toString() + "/public/posts/This-is-the-First-Post.html");
        assertThat(expectedPostOne.readContent(ENCODING), is("<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <body>\n"
                + "        <h1>NAME</h1>\n"
                + "        <h2>DESCRIPTION</h2>\n"
                + "\n"
                + "        <article>\n"
                + "    <h3>This is the First Post</h3><p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam.</p><p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam.</p>\n"
                + "</article>\n"
                + "    </body>\n"
                + "</html>"));
        final DataFile expectedPostTwo = new DataFile(tmp.getRoot().toString() + "/public/posts/This-is-the-Second-Post.html");
        assertThat(expectedPostTwo.readContent(ENCODING), is("<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <body>\n"
                + "        <h1>NAME</h1>\n"
                + "        <h2>DESCRIPTION</h2>\n"
                + "\n"
                + "        <article>\n"
                + "    <h3>This is the Second Post</h3><p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam.</p><p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam.</p>\n"
                + "</article>\n"
                + "    </body>\n"
                + "</html>"));
        final DataFile expectedPostThree = new DataFile(tmp.getRoot().toString() + "/public/posts/This-is-the-Third-Post.html");
        assertThat(expectedPostThree.readContent(ENCODING), is("<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <body>\n"
                + "        <h1>NAME</h1>\n"
                + "        <h2>DESCRIPTION</h2>\n"
                + "\n"
                + "        <article>\n"
                + "    <h3>This is the Third Post</h3><p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam.</p><p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam.</p>\n"
                + "</article>\n"
                + "    </body>\n"
                + "</html>"));
        final DataFile expectedSiteOne = new DataFile(tmp.getRoot().toString() + "/public/sites/Site-One.html");
        assertThat(expectedSiteOne.readContent(ENCODING), is("<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <body>\n"
                + "        <h1>NAME</h1>\n"
                + "        <h2>DESCRIPTION</h2>\n"
                + "\n"
                + "        <h3>SITE</h3>\n"
                + "<article>\n"
                + "    <h3>This is Site One</h3><p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam.</p>\n"
                + "</article>\n"
                + "    </body>\n"
                + "</html>"));
        final DataFile expectedSiteTwo = new DataFile(tmp.getRoot().toString() + "/public/sites/Site-Two.html");
        assertThat(expectedSiteTwo.readContent(ENCODING), is("<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <body>\n"
                + "        <h1>NAME</h1>\n"
                + "        <h2>DESCRIPTION</h2>\n"
                + "\n"
                + "        <h3>SITE</h3>\n"
                + "<article>\n"
                + "    <h3>This is Site Two</h3><p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam.</p>\n"
                + "</article>\n"
                + "    </body>\n"
                + "</html>"));
        final DataFile expectedIndex = new DataFile(tmp.getRoot().toString() + "/public/index.html");
        assertThat(expectedIndex.readContent(ENCODING), is("<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <body>\n"
                + "        <h1>Blog Title</h1>\n"
                + "        <h2>Blog Description</h2>\n"
                + "\n"
                + "        <h3>All Blog Posts</h3>\n"
                + "<ul>\n"
                + "        <li>\n"
                + "        <a href=\"http://uberblog.local/posts/This-is-the-First-Post.html\">This is the First Post</a>\n"
                + "        <span>(Fri, 30 May 2014 21:29:20 +0200)</span>\n"
                + "    </li>\n"
                + "    <li>\n"
                + "        <a href=\"http://uberblog.local/posts/This-is-the-Second-Post.html\">This is the Second Post</a>\n"
                + "        <span>(Mon, 30 Jun 2014 23:25:44 +0200)</span>\n"
                + "    </li>\n"
                + "    <li>\n"
                + "        <a href=\"http://uberblog.local/posts/This-is-the-Third-Post.html\">This is the Third Post</a>\n"
                + "        <span>(Mon, 28 Jul 2014 17:44:13 +0200)</span>\n"
                + "    </li>\n"
                + "</ul>\n"
                + "\n"
                + "    </body>\n"
                + "</html>"));
        final DataFile expectedSiteMap = new DataFile(tmp.getRoot().toString() + "/public/site_map.xml");
        assertThat(expectedSiteMap.readContent(ENCODING), is("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
                + "    <url>\n"
                + "        <loc>http://uberblog.local/posts/This-is-the-First-Post.html</loc>\n"
                + "        <lastmod>2014-05-30T21:29:20+02:00</lastmod>\n"
                + "        <changefreq>daily</changefreq>\n"
                + "        <priority>0.8</priority>\n"
                + "    </url>\n"
                + "    <url>\n"
                + "        <loc>http://uberblog.local/posts/This-is-the-Second-Post.html</loc>\n"
                + "        <lastmod>2014-06-30T23:25:44+02:00</lastmod>\n"
                + "        <changefreq>daily</changefreq>\n"
                + "        <priority>0.8</priority>\n"
                + "    </url>\n"
                + "    <url>\n"
                + "        <loc>http://uberblog.local/posts/This-is-the-Third-Post.html</loc>\n"
                + "        <lastmod>2014-07-28T17:44:13+02:00</lastmod>\n"
                + "        <changefreq>daily</changefreq>\n"
                + "        <priority>0.8</priority>\n"
                + "    </url>\n"
                + "    <url>\n"
                + "        <loc>http://uberblog.local/sites/Site-One.html</loc>\n"
                + "        <lastmod>2014-08-30T15:29:20+02:00</lastmod>\n"
                + "        <changefreq>weekly</changefreq>\n"
                + "        <priority>0.5</priority>\n"
                + "    </url>\n"
                + "    <url>\n"
                + "        <loc>http://uberblog.local/sites/Site-Two.html</loc>\n"
                + "        <lastmod>2014-09-30T15:29:20+02:00</lastmod>\n"
                + "        <changefreq>weekly</changefreq>\n"
                + "        <priority>0.5</priority>\n"
                + "    </url>\n"
                + "</urlset>"));
        final DataFile expectedFeed = new DataFile(tmp.getRoot().toString() + "/public/feed.xml");
        assertThat(expectedFeed.readContent(ENCODING), is("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<rss xmlns:content=\"http://purl.org/rss/1.0/modules/content/\"\n"
                + "     xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\"\n"
                + "     xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n"
                + "     version=\"2.0\"\n"
                + "     xmlns:trackback=\"http://madskills.com/public/xml/rss/module/trackback/\">\n"
                + "    <channel>\n"
                + "        <title>Blog Title</title>\n"
                + "        <link>http://uberblog.local/</link>\n"
                + "        <description>Blog Description</description>\n"
                + "        <language>en</language>\n"
                + "        <lastBuildDate>Mon, 08 Dec 2014 20:17:00 +0100</lastBuildDate>\n"
                + "        <item>\n"
                + "            <title>This is the First Post</title>\n"
                + "            <link>http://uberblog.local/posts/This-is-the-First-Post.html</link>\n"
                + "            <description>This is the first post.</description>\n"
                + "            <pubDate>Fri, 30 May 2014 21:29:20 +0200</pubDate>\n"
                + "            <dc:date>2014-05-30T21:29:20+02:00</dc:date>\n"
                + "        </item>\n"
                + "        <item>\n"
                + "            <title>This is the Second Post</title>\n"
                + "            <link>http://uberblog.local/posts/This-is-the-Second-Post.html</link>\n"
                + "            <description>This is the second post.</description>\n"
                + "            <pubDate>Mon, 30 Jun 2014 23:25:44 +0200</pubDate>\n"
                + "            <dc:date>2014-06-30T23:25:44+02:00</dc:date>\n"
                + "        </item>\n"
                + "        <item>\n"
                + "            <title>This is the Third Post</title>\n"
                + "            <link>http://uberblog.local/posts/This-is-the-Third-Post.html</link>\n"
                + "            <description>This is the third post.</description>\n"
                + "            <pubDate>Mon, 28 Jul 2014 17:44:13 +0200</pubDate>\n"
                + "            <dc:date>2014-07-28T17:44:13+02:00</dc:date>\n"
                + "        </item>\n"
                + "    </channel>\n"
                + "</rss>"));
    }

    private Dirs createDirs() throws IOException {
        final Dirs dirs = new Dirs();
        dirs.dataDir = tmp.newFolder("data").toPath();
        dirs.postsData = dirs.dataDir.resolve("posts");
        dirs.sitesData = dirs.dataDir.resolve("sites");
        Files.createDirectories(dirs.postsData);
        Files.createDirectories(dirs.sitesData);
        dirs.publicDir = tmp.newFolder("public").toPath();
        Files.createDirectories(dirs.publicDir.resolve("posts"));
        Files.createDirectories(dirs.publicDir.resolve("sites"));
        dirs.templatesDir = tmp.newFolder("templates").toPath();
        return dirs;
    }

    private void copyData(final Dirs dirs) throws IOException, URISyntaxException {
        for (final String name : Arrays.asList(
                "2014-05-30T21.29.20_This-is-the-First-Post.md",
                "2014-06-30T23.25.44_This-is-the-Second-Post.md",
                "2014-07-28T17.44.13_This-is-the-Third-Post.md")) {
            Files.copy(
                    createPath("posts/" + name),
                    dirs.postsData.resolve(name));
        }

        for (final String name : Arrays.asList(
                "2014-08-30T15.29.20_Site-One.md",
                "2014-09-30T15.29.20_Site-Two.md")) {
            Files.copy(
                    createPath("sites/" + name),
                    dirs.sitesData.resolve(name));
        }
    }

    private void copyTemplates(final Dirs dirs) throws IOException, URISyntaxException {
        for (final String name : Arrays.asList(
                "feed.ftl",
                "index.ftl",
                "layout.ftl",
                "post.ftl",
                "site.ftl",
                "site_map.ftl")) {
            Files.copy(
                    createPath(SCAFOLD_PACKAGE_PREFIX + name),
                    dirs.templatesDir.resolve(name));
        }
    }

    private static final class Dirs {

        private Path dataDir;
        private Path postsData;
        private Path sitesData;
        private Path publicDir;
        private Path templatesDir;
    }

}
