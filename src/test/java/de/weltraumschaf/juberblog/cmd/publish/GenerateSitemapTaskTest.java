package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.juberblog.BaseTestCase;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.core.PageType;
import de.weltraumschaf.juberblog.core.Pages;
import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import java.net.URI;
import java.util.Collection;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

/**
 * Tests for {@link GenerateSitemapTask}.
 *
 * @author Sven Strittmatter
 */
public class GenerateSitemapTaskTest extends BaseTestCase {

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructWithNullThrowsException() {
        new GenerateSitemapTask(null);
    }

    @Test
    public void execute_noPages() throws Exception {
        final GenerateSitemapTask sut = new GenerateSitemapTask(createRegistry());

        sut.execute();

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.XML)
                .find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(1));
        final DataFile expectedFile = new DataFile(tmp.getRoot().toString() + "/public/site_map.xml");
        assertThat(foundFiles, containsInAnyOrder(expectedFile));
        assertThat(expectedFile.readContent(ENCODING), isSimilarTo(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
                + "</urlset>"
        ).ignoreWhitespace());
    }

    @Test
    public void execute_twoPages() throws Exception {
        final GenerateSitemapTask sut = new GenerateSitemapTask(createRegistry());
        final Pages pages = new Pages();
        pages.add(new Page("title1", URI.create("http://www.myblog.com/link1"), "desc1", new DateTime("2014-11-29"), PageType.POST));
        pages.add(new Page("title2", URI.create("http://www.myblog.com/link2"), "desc2", new DateTime("2014-11-30"), PageType.POST));

        sut.execute(pages);

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.XML)
                .find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(1));
        final DataFile expectedFile = new DataFile(tmp.getRoot().toString() + "/public/site_map.xml");
        assertThat(foundFiles, containsInAnyOrder(expectedFile));
        assertThat(expectedFile.readContent(ENCODING), isSimilarTo(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
                + "    <url>\n"
                + "        <loc>http://www.myblog.com/link1</loc>\n"
                + "        <lastmod>2014-11-29T00:00:00+01:00</lastmod>\n"
                + "        <changefreq>daily</changefreq>\n"
                + "        <priority>0.8</priority>\n"
                + "    </url>\n"
                + "    <url>\n"
                + "        <loc>http://www.myblog.com/link2</loc>\n"
                + "        <lastmod>2014-11-30T00:00:00+01:00</lastmod>\n"
                + "        <changefreq>daily</changefreq>\n"
                + "        <priority>0.8</priority>\n"
                + "    </url>\n"
                + "</urlset>"
        ).ignoreWhitespace());
    }

}
