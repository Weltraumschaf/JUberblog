package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.juberblog.BaseTestCase;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.core.Pages;
import de.weltraumschaf.juberblog.core.PageType;
import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import java.net.URI;
import java.util.Collection;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

/**
 * Tests for {@link GenerateFeedTask}.
 *
 * @author Sven Strittmatter
 */
public class GenerateFeedTaskTest extends BaseTestCase {

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructWithNullThrowsException() {
        new GenerateFeedTask(null);
    }

    @Test
    public void execute_noPages() throws Exception {
        final GenerateFeedTask sut = new GenerateFeedTask(createRegistry());

        sut.execute();

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.XML)
                .find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(1));
        final DataFile expectedFile = new DataFile(tmp.getRoot().toString() + "/public/feed.xml");
        assertThat(foundFiles, containsInAnyOrder(expectedFile));
        assertThat(expectedFile.readContent(ENCODING), isSimilarTo(
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
                + "    </channel>\n"
                + "</rss>").ignoreWhitespace());
    }

    @Test
    public void execute_twoPages() throws Exception {
        final GenerateFeedTask sut = new GenerateFeedTask(createRegistry());

        final Pages pages = new Pages();
        pages.add(new Page("title1", URI.create("http://uberblog.local/link1"), "desc1", new DateTime("2014-11-29"), PageType.POST));
        pages.add(new Page("title2", URI.create("http://uberblog.local/link2"), "desc2", new DateTime("2014-11-30"), PageType.POST));

        sut.execute(pages);

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.XML)
                .find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(1));
        final DataFile expectedFile = new DataFile(tmp.getRoot().toString() + "/public/feed.xml");
        assertThat(foundFiles, containsInAnyOrder(expectedFile));
        assertThat(expectedFile.readContent(ENCODING), isSimilarTo(
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
                + "            <title>title1</title>\n"
                + "            <link>http://uberblog.local/link1</link>\n"
                + "            <description>desc1</description>\n"
                + "            <pubDate>Sat, 29 Nov 2014 00:00:00 +0100</pubDate>\n"
                + "            <dc:date>2014-11-29T00:00:00+01:00</dc:date>\n"
                + "        </item>\n"
                + "        <item>\n"
                + "            <title>title2</title>\n"
                + "            <link>http://uberblog.local/link2</link>\n"
                + "            <description>desc2</description>\n"
                + "            <pubDate>Sun, 30 Nov 2014 00:00:00 +0100</pubDate>\n"
                + "            <dc:date>2014-11-30T00:00:00+01:00</dc:date>\n"
                + "        </item>\n "
                + "   </channel>\n"
                + "</rss>").ignoreWhitespace());
    }

}
