package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.juberblog.BaseTestCase;
import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.juberblog.core.BlogConfiguration;
import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import java.util.Collection;
import java.util.Properties;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

/**
 * Tests for {@link PublishSubCommand}.
 *
 * @author Sven Strittmatter
 */
public class PublishSubCommandTest extends BaseTestCase {

    @Test
    public void execute() throws Exception {
        final Properties config = new Properties();
        config.setProperty(BlogConfiguration.TITLE, "Blog Title");
        config.setProperty(BlogConfiguration.DESCRIPTION, "Blog Description");
        config.setProperty(BlogConfiguration.LANGUAGE, "en");
        config.setProperty(BlogConfiguration.SITE_URI, "http://uberblog.local/");
        config.setProperty(BlogConfiguration.ENCODING, "utf-8");
        config.setProperty(BlogConfiguration.DATA_DIR, "/");
        config.setProperty(BlogConfiguration.PUBLIC_DIR, "/");
        config.setProperty(BlogConfiguration.TEMPLATE_DIR, "/");
        final JUberblog registry = JUberblog.Builder.create()
            .directories(createDirs(tmp))
            .templates(createTemplates())
            .configuration(new BlogConfiguration(config))
            .options(createOptions())
            .io(createIo())
            .version(createVersion())
            .verbose(createVervose())
            .fmd(createFmd())
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
        assertThat(feedFile.readContent(ENCODING), isSimilarTo(
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
            + "</rss>").ignoreWhitespace());
        assertThat(indexFile.readContent(ENCODING), isSimilarTo(
            "<!DOCTYPE html>\n"
            + "<html lang=\"en\">\n"
            + "    <head>\n"
            + "        <meta name=\"robots\" content=\"all\"/>\n"
            + "        <meta name=\"description\" content=\"Blog Description\"/>\n"
            + "        <meta name=\"keywords\" content=\"\"/>\n"
            + "        <meta charset=\"utf-8\"/>\n"
            + "        <link href=\"http://uberblog.local//css/main.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\"/>\n"
            + "        <link href=\"http://uberblog.local//img/favicon.ico\" rel=\"shortcut icon\" type=\"image/x-icon\"/>\n"
            + "    </head>\n"
            + "\n"
            + "    <body>\n"
            + "        <header>\n"
            + "            <h1>Blog Title</h1>\n"
            + "            <h2>Blog Description</h2>\n"
            + "        </header>\n"
            + "\n"
            + "        <section>\n"
            + "        <h3>All Blog Posts</h3>\n"
            + "<ul>\n"
            + "    <li>\n"
            + "        <a href=\"http://uberblog.local/posts/This-is-the-Third-Post.html\">This is the Third Post</a>\n"
            + "        <span>(28.07.2014, 17:44)</span>\n"
            + "    </li>\n"
            + "    <li>\n"
            + "        <a href=\"http://uberblog.local/posts/This-is-the-Second-Post.html\">This is the Second Post</a>\n"
            + "        <span>(30.06.2014, 23:25)</span>\n"
            + "    </li>\n"
            + "    <li>\n"
            + "        <a href=\"http://uberblog.local/posts/This-is-the-First-Post.html\">This is the First Post</a>\n"
            + "        <span>(30.05.2014, 21:29)</span>\n"
            + "    </li>\n"
            + "</ul>\n"
            + "\n"
            + "        </section>\n"
            + "\n"
            + "        <footer>\n"
            + "            Powered by JUberblog.\n"
            + "        </footer>\n"
            + "\n"
            + "        <script type=\"text/javascript\" src=\"http://uberblog.local//js/main.js\"></script>\n"
            + "    </body>\n"
            + "</html>").ignoreWhitespace().ignoreComments());
        assertThat(siteMapFile.readContent(ENCODING), isSimilarTo(
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
            + "</urlset>").ignoreWhitespace());
    }

}
