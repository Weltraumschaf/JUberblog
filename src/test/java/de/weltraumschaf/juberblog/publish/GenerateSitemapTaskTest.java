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
package de.weltraumschaf.juberblog.publish;

import de.weltraumschaf.juberblog.BaseTestCase;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import java.util.Collection;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link GenerateSitemapTask}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class GenerateSitemapTaskTest extends BaseTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test(expected = NullPointerException.class)
    public void constructWithNullThrowsException() {
        new GenerateSitemapTask(null);
    }

    @Test
    public void execute_noPages() throws Exception {
        final GenerateSitemapTask sut = new GenerateSitemapTask(new GenerateSitemapTask.Config(
                createPath(SCAFOLD_PACKAGE_PREFIX + "site_map.ftl"),
                tmp.getRoot().toPath(),
                ENCODING));

        sut.execute();

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.XML)
                .find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(1));
        final DataFile expectedFile = new DataFile(tmp.getRoot().toString() + "/site_map.xml");
        assertThat(foundFiles, containsInAnyOrder(expectedFile));
        assertThat(expectedFile.readContent(ENCODING), is(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
                + "</urlset>"
        ));
    }

    @Test
    public void execute_twoPages() throws Exception {
        final GenerateSitemapTask sut = new GenerateSitemapTask(new GenerateSitemapTask.Config(
                createPath(SCAFOLD_PACKAGE_PREFIX + "site_map.ftl"),
                tmp.getRoot().toPath(),
                ENCODING));
        final Page.Pages pages = new Page.Pages();
        pages.add(new Page("title1", "link1", "desc1", new DateTime("2014-11-29"), Page.Type.POST));
        pages.add(new Page("title2", "link2", "desc2", new DateTime("2014-11-30"), Page.Type.POST));

        sut.execute(pages);

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.XML)
                .find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(1));
        final DataFile expectedFile = new DataFile(tmp.getRoot().toString() + "/site_map.xml");
        assertThat(foundFiles, containsInAnyOrder(expectedFile));
        assertThat(expectedFile.readContent(ENCODING), is(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
                + "    <url>\n"
                + "        <loc>link1</loc>\n"
                + "        <lastmod>2014-11-29T00:00:00+01:00</lastmod>\n"
                + "        <changefreq>daily</changefreq>\n"
                + "        <priority>0.8</priority>\n"
                + "    </url>\n"
                + "    <url>\n"
                + "        <loc>link2</loc>\n"
                + "        <lastmod>2014-11-30T00:00:00+01:00</lastmod>\n"
                + "        <changefreq>daily</changefreq>\n"
                + "        <priority>0.8</priority>\n"
                + "    </url>\n"
                + "</urlset>"
        ));
    }

}
