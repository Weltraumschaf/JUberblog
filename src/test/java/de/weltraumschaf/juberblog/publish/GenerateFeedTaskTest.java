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
import de.weltraumschaf.juberblog.core.Page.Pages;
import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import java.util.Collection;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link GenerateFeedTask}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class GenerateFeedTaskTest extends BaseTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test(expected = NullPointerException.class)
    public void constructWithNullThrowsException() {
        new GenerateFeedTask(null);
    }

    @Test
    public void execute_noPages() throws Exception {
        final GenerateFeedTask sut = new GenerateFeedTask(new GenerateFeedTask.Config(
                createPath(SCAFOLD_PACKAGE_PREFIX + "feed.ftl"),
                tmp.getRoot().toPath(),
                ENCODING,
                "title",
                "link",
                "description",
                "language",
                new DateTime("2014-12-01")));

        sut.execute();

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.XML)
                .find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(1));
        final DataFile expectedFile = new DataFile(tmp.getRoot().toString() + "/feed.xml");
        assertThat(foundFiles, containsInAnyOrder(expectedFile));
        assertThat(expectedFile.readContent(ENCODING), is(
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
                + "        <lastBuildDate>Mon, 01 Dec 2014 00:00:00 +0100</lastBuildDate>\n"
                + "    </channel>\n"
                + "</rss>"));
    }

    @Test
    public void execute_twoPages() throws Exception {
        final GenerateFeedTask sut = new GenerateFeedTask(new GenerateFeedTask.Config(
                createPath(SCAFOLD_PACKAGE_PREFIX + "feed.ftl"),
                tmp.getRoot().toPath(),
                ENCODING,
                "title",
                "link",
                "description",
                "language",
                new DateTime("2014-12-01")));
        final Pages pages = new Pages();
        pages.add(new Page("title1", "link1", "desc1", new DateTime("2014-11-29"), Page.Type.POST));
        pages.add(new Page("title2", "link2", "desc2", new DateTime("2014-11-30"), Page.Type.POST));

        sut.execute(pages);

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.XML)
                .find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(1));
        final DataFile expectedFile = new DataFile(tmp.getRoot().toString() + "/feed.xml");
        assertThat(foundFiles, containsInAnyOrder(expectedFile));
        assertThat(expectedFile.readContent(ENCODING), is(
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
                + "        <lastBuildDate>Mon, 01 Dec 2014 00:00:00 +0100</lastBuildDate>\n"
                + "        <item>\n"
                + "            <title>title1</title>\n"
                + "            <link>link1</link>\n"
                + "            <description>desc1</description>\n"
                + "            <pubDate>Sat, 29 Nov 2014 00:00:00 +0100</pubDate>\n"
                + "            <dc:date>2014-11-29T00:00:00+01:00</dc:date>\n"
                + "        </item>\n"
                + "        <item>\n"
                + "            <title>title2</title>\n"
                + "            <link>link2</link>\n"
                + "            <description>desc2</description>\n"
                + "            <pubDate>Sun, 30 Nov 2014 00:00:00 +0100</pubDate>\n"
                + "            <dc:date>2014-11-30T00:00:00+01:00</dc:date>\n"
                + "        </item>\n "
                + "   </channel>\n"
                + "</rss>"));
    }

}
