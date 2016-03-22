package de.weltraumschaf.juberblog.publish;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.BaseTestCase;
import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.juberblog.app.Options;
import de.weltraumschaf.juberblog.core.Configuration;
import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import java.util.Collection;
import java.util.Properties;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link PublishSubCommand}.
 *
 * @author Sven Strittmatter
 */
public class PublishSubCommandTest extends BaseTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void execute() throws Exception {
        final Properties config = new Properties();
        config.setProperty(Configuration.TITLE, "Blog Title");
        config.setProperty(Configuration.DESCRIPTION, "Blog Description");
        config.setProperty(Configuration.LANGUAGE, "en");
        config.setProperty(Configuration.SITE_URI, "http://uberblog.local/");
        config.setProperty(Configuration.ENCODING, "utf-8");
        config.setProperty(Configuration.DATA_DIR, "/");
        config.setProperty(Configuration.PUBLIC_DIR, "/");
        config.setProperty(Configuration.TEMPLATE_DIR, "/");
        final JUberblog registry = JUberblog.Builder.create()
                .directories(createDirs(tmp))
                .templates(createTemplates())
                .configuration(new Configuration(config))
                .options(new Options())
                .io(mock(IO.class))
                .product();
        final PublishSubCommand sut = new PublishSubCommand(registry);

        sut.execute();

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.HTML, FileNameExtension.XML)
                .find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(8));
        final DataFile feedFile = new DataFile(tmp.getRoot().toString() + "/feed.xml");
        final DataFile indexFile = new DataFile(tmp.getRoot().toString() + "/index.html");
        final DataFile siteMapFile = new DataFile(tmp.getRoot().toString() + "/site_map.xml");
        assertThat(foundFiles,
                containsInAnyOrder(new DataFile(tmp.getRoot().toString() + "/posts/This-is-the-First-Post.html"),
                        new DataFile(tmp.getRoot().toString() + "/posts/This-is-the-Second-Post.html"),
                        new DataFile(tmp.getRoot().toString() + "/posts/This-is-the-Third-Post.html"),
                        new DataFile(tmp.getRoot().toString() + "/sites/Site-One.html"),
                        new DataFile(tmp.getRoot().toString() + "/sites/Site-Two.html"),
                        feedFile, indexFile, siteMapFile));
        assertThat(feedFile.readContent(ENCODING), is(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
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
        assertThat(indexFile.readContent(ENCODING), is(
                "<!DOCTYPE html>\n"
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
        assertThat(siteMapFile.readContent(ENCODING), is(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
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
    }

}
