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
package de.weltraumschaf.juberblog;

import com.beust.jcommander.internal.Maps;
import de.weltraumschaf.commons.guava.Lists;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.Fragment;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.freemarkerdown.RenderOptions;
import de.weltraumschaf.juberblog.Page.Pages;
import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.tasks.GenerateFeedTask;
import de.weltraumschaf.juberblog.tasks.GenerateIndexTask;
import de.weltraumschaf.juberblog.tasks.PublishTask;
import de.weltraumschaf.juberblog.tasks.Task;
import de.weltraumschaf.juberblog.tasks.TaskExecutor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link App}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AppTest extends JUberblogTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void publishing() throws Exception {
        final TaskExecutor executor = new TaskExecutor();
        executor.append(new PublishTask(new PublishTask.Config(
                ENCODING,
                createPath("posts"),
                tmp.getRoot().toPath(),
                createPath("layout.ftl"),
                createPath("post.ftl"),
                Page.Type.POST)))
                .append(new GenerateFeedTask(new GenerateFeedTask.Config(
                                        createPath("feed.ftl"),
                                        tmp.getRoot().toPath(),
                                        ENCODING,
                                        "title",
                                        "link",
                                        "description",
                                        "language",
                                        new DateTime("2014-12-08T20:17:00")
                                )))
                .append(new GenerateIndexTask(new GenerateIndexTask.Config(
                                        ENCODING,
                                        tmp.getRoot().toPath(),
                                        createPath("layout.ftl"),
                                        createPath("index.ftl"))))
                .append(new PublishTask(new PublishTask.Config(
                                        ENCODING,
                                        createPath("sites"),
                                        tmp.getRoot().toPath(),
                                        createPath("layout.ftl"),
                                        createPath("site.ftl"),
                                        Page.Type.SITE
                                )))
                .append(new GenerateSitemapTask(new GenerateSitemapTask.Config(
                                        createPath("site_map.ftl"),
                                        tmp.getRoot().toPath(),
                                        ENCODING)))
                .execute();

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.HTML, FileNameExtension.XML)
                .find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(8));
        final DataFile feedFile = new DataFile(tmp.getRoot().toString() + "/feed.xml");
        final DataFile indexFile = new DataFile(tmp.getRoot().toString() + "/index.html");
        final DataFile siteMapFile = new DataFile(tmp.getRoot().toString() + "/site_map.xml");
        assertThat(foundFiles,
                containsInAnyOrder(new DataFile(tmp.getRoot().toString() + "/This-is-the-First-Post.html"),
                        new DataFile(tmp.getRoot().toString() + "/This-is-the-Second-Post.html"),
                        new DataFile(tmp.getRoot().toString() + "/This-is-the-Third-Post.html"),
                        new DataFile(tmp.getRoot().toString() + "/Site-One.html"),
                        new DataFile(tmp.getRoot().toString() + "/Site-Two.html"),
                        feedFile, indexFile, siteMapFile));
        assertThat(feedFile.readContent(ENCODING), is(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<rss xmlns:content=\"http://purl.org/rss/1.0/modules/content/\"\n"
                + "     xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\"\n"
                + "     xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n"
                + "     version=\"2.0\"\n"
                + "     xmlns:trackback=\"http://madskills.com/public/xml/rss/module/trackback/\">\n"
                + "    <channel>\n"
                + "        <title>title</title>\n"
                + "        <link>link</link>\n"
                + "        <description>description</description>\n"
                + "        <language>language</language>\n"
                + "        <lastBuildDate>Mon, 08 Dec 2014 20:17:00 +0100</lastBuildDate>\n"
                + "        <item>\n"
                + "            <title>This is the First Post</title>\n"
                + "            <link>http://localhost/posts/This-is-the-First-Post.html</link>\n"
                + "            <description>This is the first post.</description>\n"
                + "            <pubDate>Fri, 30 May 2014 21:29:20 +0200</pubDate>\n"
                + "            <dc:date>2014-05-30T21:29:20+02:00</dc:date>\n"
                + "        </item>\n"
                + "        <item>\n"
                + "            <title>This is the Second Post</title>\n"
                + "            <link>http://localhost/posts/This-is-the-Second-Post.html</link>\n"
                + "            <description>This is the second post.</description>\n"
                + "            <pubDate>Mon, 30 Jun 2014 23:25:44 +0200</pubDate>\n"
                + "            <dc:date>2014-06-30T23:25:44+02:00</dc:date>\n"
                + "        </item>\n"
                + "        <item>\n"
                + "            <title>This is the Third Post</title>\n"
                + "            <link>http://localhost/posts/This-is-the-Third-Post.html</link>\n"
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
                + "        <h1>TODO</h1>\n"
                + "        <h2>TODO</h2>\n"
                + "\n"
                + "        <h3>All Blog Posts</h3>\n"
                + "<ul>\n"
                + "        <li>\n"
                + "        <a href=\"http://localhost/posts/This-is-the-First-Post.html\">This is the First Post</a>\n"
                + "        <span>(Fri, 30 May 2014 21:29:20 +0200)</span>\n"
                + "    </li>\n"
                + "    <li>\n"
                + "        <a href=\"http://localhost/posts/This-is-the-Second-Post.html\">This is the Second Post</a>\n"
                + "        <span>(Mon, 30 Jun 2014 23:25:44 +0200)</span>\n"
                + "    </li>\n"
                + "    <li>\n"
                + "        <a href=\"http://localhost/posts/This-is-the-Third-Post.html\">This is the Third Post</a>\n"
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
                        + "        <loc>http://localhost/posts/This-is-the-First-Post.html</loc>\n"
                        + "        <lastmod>2014-05-30T21:29:20+02:00</lastmod>\n"
                        + "        <changefreq>daily</changefreq>\n"
                        + "        <priority>0.8</priority>\n"
                        + "    </url>\n"
                        + "    <url>\n"
                        + "        <loc>http://localhost/posts/This-is-the-Second-Post.html</loc>\n"
                        + "        <lastmod>2014-06-30T23:25:44+02:00</lastmod>\n"
                        + "        <changefreq>daily</changefreq>\n"
                        + "        <priority>0.8</priority>\n"
                        + "    </url>\n"
                        + "    <url>\n"
                        + "        <loc>http://localhost/posts/This-is-the-Third-Post.html</loc>\n"
                        + "        <lastmod>2014-07-28T17:44:13+02:00</lastmod>\n"
                        + "        <changefreq>daily</changefreq>\n"
                        + "        <priority>0.8</priority>\n"
                        + "    </url>\n"
                        + "    <url>\n"
                        + "        <loc>http://localhost/posts/Site-One.html</loc>\n"
                        + "        <lastmod>2014-08-30T15:29:20+02:00</lastmod>\n"
                        + "        <changefreq>weekly</changefreq>\n"
                        + "        <priority>0.5</priority>\n"
                        + "    </url>\n"
                        + "    <url>\n"
                        + "        <loc>http://localhost/posts/Site-Two.html</loc>\n"
                        + "        <lastmod>2014-09-30T15:29:20+02:00</lastmod>\n"
                        + "        <changefreq>weekly</changefreq>\n"
                        + "        <priority>0.5</priority>\n"
                        + "    </url>\n"
                        + "</urlset>"));
    }

    static class GenerateSitemapTask implements Task<Void, Pages> {

        private final Config config;

        public GenerateSitemapTask(final Config config) {
            super();
            this.config = Validate.notNull(config, "config");
        }

        @Override
        public Void execute() throws Exception {
            return execute(new Pages());
        }

        @Override
        public Void execute(final Pages previusResult) throws Exception {
            final FreeMarkerDown fmd = FreeMarkerDown.create(config.encoding);
            final Fragment template = fmd.createFragemnt(
                    config.template,
                    config.encoding,
                    config.template.toString(),
                    RenderOptions.WITHOUT_MARKDOWN);
            template.assignVariable("encoding", config.encoding);
            template.assignVariable("urls", convert(previusResult));

            Files.write(
                    config.outputDir.resolve("site_map" + FileNameExtension.XML.getExtension()),
                    fmd.render(template).getBytes(config.encoding)
            );

            return null;
        }

        private Collection<Map<String, String>> convert(final List<Page> pages) {
            final Collection<Map<String, String>> items = Lists.newArrayList();

            for (final Page page : pages) {
                items.add(convert(page));
            }

            return Collections.unmodifiableCollection(items);
        }

        private Map<String, String> convert(final Page page) {
            final Map<String, String> item = Maps.newHashMap();
            item.put("loc", page.getLink());
            // XXX Introduce last mod date.
            item.put("lastmod", DateFormatter.format(page.getPublishingDate(), DateFormatter.Format.W3C_DATE_FORMAT));

            if (page.getType() == Page.Type.POST) {
                item.put("changefreq", "daily");
                item.put("priority", "0.8");
            } else {
                item.put("changefreq", "weekly");
                item.put("priority", "0.5");
            }

            return Collections.unmodifiableMap(item);
        }

        @Override
        public Class<Pages> getDesiredTypeForPreviusResult() {
            return Pages.class;
        }

        public static final class Config {

            private final Path template;
            private final Path outputDir;
            private final String encoding;

            public Config(final Path template, final Path outputDir, final String encoding) {
                super();
                this.template = Validate.notNull(template, "template");
                this.outputDir = Validate.notNull(outputDir, "outputDir");
                this.encoding = Validate.notEmpty(encoding, "title");
            }
        }
    }

}
