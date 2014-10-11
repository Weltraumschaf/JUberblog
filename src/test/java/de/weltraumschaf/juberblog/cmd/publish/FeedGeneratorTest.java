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

import com.beust.jcommander.internal.Lists;
import de.weltraumschaf.juberblog.model.DataFile;
import de.weltraumschaf.juberblog.model.MetaData;
import de.weltraumschaf.juberblog.model.Page;
import de.weltraumschaf.juberblog.model.PublishedPages;
import de.weltraumschaf.juberblog.model.SiteMapUrl;
import de.weltraumschaf.juberblog.template.Configurations;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Ignore;

/**
 * Tests for {@link FeedGenerator}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class FeedGeneratorTest {

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    private static final String URI = "http://www.foobar.com/";

    private final DataFile dummy1 = new DataFile(
        "file1",
        "file1",
        0L,
        0L,
        "file1",
        "file1",
        "md",
        new MetaData(),
        DataFile.Type.POST);
    private final DataFile dummy2 = new DataFile(
        "file2",
        "file2",
        0L,
        0L,
        "file2",
        "file2",
        "md",
        new MetaData(),
        DataFile.Type.POST);

    private String today(final DateTime ts) {
        return FeedGenerator.formatTimestamp(ts);
    }

    private String todayDc(final DateTime ts) {
        return FeedGenerator.formatDcDate(ts);
    }

    @Test
    public void formatTimestamp() {
        assertThat(
                FeedGenerator.formatTimestamp(new DateTime(1382995754000L)),
                is(equalTo("Mon, 28 Oct 2013 22:29:14 +0100")));
    }

    @Test
    public void formatDcDate() {
        assertThat(
                FeedGenerator.formatDcDate(new DateTime(1382995754000L)),
                is(equalTo("2013-10-28T22:29:14+01:00")));
    }

    @Test
    public void execute_noPages() throws IOException, URISyntaxException {
        final PublishedPages pages = new PublishedPages();
        final FeedGenerator sut = new FeedGenerator(
                Configurations.forTests(Configurations.SCAFFOLD_TEMPLATE_DIR), pages);
        assertThat(sut.getResult(), is(equalTo("")));
        sut.setTitle("This is the title");
        sut.setDescription("This is the description.");
        sut.setLanguage("en");
        sut.setLink(URI + "feed.xml");
        final DateTime now = new DateTime();
        sut.setLastBuildDate(now);

        sut.execute();

        assertThat(sut.getResult(), is(equalTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<rss xmlns:content=\"http://purl.org/rss/1.0/modules/content/\"\n"
                + "     xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\"\n"
                + "     xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n"
                + "     version=\"2.0\"\n"
                + "     xmlns:trackback=\"http://madskills.com/public/xml/rss/module/trackback/\">\n"
                + "    <channel>\n"
                + "        <title>This is the title</title>\n"
                + "        <link>" + URI + "feed.xml</link>\n"
                + "        <description>This is the description.</description>\n"
                + "        <language>en</language>\n"
                + "        <lastBuildDate>" + today(now) + "</lastBuildDate>\n"
                + "    </channel>\n"
                + "</rss>")));
    }

    @Test
    public void execute_twoPage() throws IOException, URISyntaxException {
        final PublishedPages pages = new PublishedPages();
        final FeedGenerator sut = new FeedGenerator(
                Configurations.forTests(Configurations.SCAFFOLD_TEMPLATE_DIR), pages);
        assertThat(sut.getResult(), is(equalTo("")));
        sut.setTitle("This is the title");
        sut.setDescription("This is the description.");
        sut.setLanguage("en");
        sut.setLink(URI + "feed.xml");
        final DateTime now = new DateTime();
        sut.setLastBuildDate(now);
        pages.put(
            new Page(
                "First Post",
                new URI(URI + "posts/First-Post.html"),
                "This is the content.",
                new DateTime(2014, 10, 1, 12, 30),
                dummy1,
                SiteMapUrl.ChangeFrequency.DAILY,
                SiteMapUrl.Priority.POST));
        pages.put(
            new Page(
                "Second Post",
                new URI(URI + "posts/Second-Post.html"),
                "This is the content with <strong>HTML</strong>.",
                new DateTime(2014, 10, 5, 22, 5),
                dummy2,
                SiteMapUrl.ChangeFrequency.DAILY,
                SiteMapUrl.Priority.POST));

        sut.execute();

        assertThat(sut.getResult(), is(equalTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<rss xmlns:content=\"http://purl.org/rss/1.0/modules/content/\"\n"
                + "     xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\"\n"
                + "     xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n"
                + "     version=\"2.0\"\n"
                + "     xmlns:trackback=\"http://madskills.com/public/xml/rss/module/trackback/\">\n"
                + "    <channel>\n"
                + "        <title>This is the title</title>\n"
                + "        <link>" + URI + "feed.xml</link>\n"
                + "        <description>This is the description.</description>\n"
                + "        <language>en</language>\n"
                + "        <lastBuildDate>" + today(now) + "</lastBuildDate>\n"
                + "        <item>\n"
                + "            <title>First Post</title>\n"
                + "            <link>http://www.foobar.com/posts/First-Post.html</link>\n"
                + "            <description>This is the content.</description>\n"
                + "            <pubDate>Wed, 01 Oct 2014 12:30:00 +0200</pubDate>\n"
                + "            <dc:date>2014-10-01T12:30:00+02:00</dc:date>\n"
                + "        </item>\n"
                + "        <item>\n"
                + "            <title>Second Post</title>\n"
                + "            <link>http://www.foobar.com/posts/Second-Post.html</link>\n"
                + "            <description>This is the content with &lt;strong&gt;HTML&lt;/strong&gt;.</description>\n"
                + "            <pubDate>Sun, 05 Oct 2014 22:05:00 +0200</pubDate>\n"
                + "            <dc:date>2014-10-05T22:05:00+02:00</dc:date>\n"
                + "        </item>\n"
                + "    </channel>\n"
                + "</rss>")));
    }

}
