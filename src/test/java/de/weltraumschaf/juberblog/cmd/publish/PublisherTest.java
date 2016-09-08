package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.juberblog.BaseTestCase;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.content.Page;
import de.weltraumschaf.juberblog.content.PageType;
import de.weltraumschaf.juberblog.core.Verbose;
import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

/**
 * Tests for {@link Publisher}.
 *
 * @author Sven Strittmatter
 */
public class PublisherTest extends BaseTestCase {

    @Test
    public void publishPosts() throws URISyntaxException, IOException {
        final Directories directories = createDirectories();
        Files.copy(createPath("posts/2014-05-30T21.29.20_This-is-the-First-Post.md"), directories.getPostsData().resolve("2014-05-30T21.29.20_This-is-the-First-Post.md"));
        Files.copy(createPath("posts/2014-06-30T23.25.44_This-is-the-Second-Post.md"), directories.getPostsData().resolve("2014-06-30T23.25.44_This-is-the-Second-Post.md"));
        Files.copy(createPath("posts/2014-07-28T17.44.13_This-is-the-Third-Post.md"), directories.getPostsData().resolve("2014-07-28T17.44.13_This-is-the-Third-Post.md"));
        final Publisher sut = new Publisher(
            createTemplates(),
            directories,
            createConfig(),
            PageType.POST,
            createVersion(),
            new Verbose(false, mock(PrintStream.class))
        );

        final List<Page> pages = sut.publish().data();
        assertThat(pages.size(), is(3));
        assertThat(pages, containsInAnyOrder(
            new Page(
                "This is the First Post",
                URI.create("http://uberblog.local/posts/This-is-the-First-Post.html"),
                "This is the first post.",
                new DateTime("2014-05-30T21:29:20"), PageType.POST),
            new Page(
                "This is the Second Post",
                URI.create("http://uberblog.local/posts/This-is-the-Second-Post.html"),
                "This is the second post.",
                new DateTime("2014-06-30T23:25:44"), PageType.POST),
            new Page(
                "This is the Third Post",
                URI.create("http://uberblog.local/posts/This-is-the-Third-Post.html"),
                "This is the third post.",
                new DateTime("2014-07-28T17:44:13"), PageType.POST)
        ));

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.HTML).find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(3));
        final DataFile expectedOne = new DataFile(tmp.getRoot().toString() + "/public/posts/This-is-the-First-Post.html");
        final DataFile expectedTwo = new DataFile(tmp.getRoot().toString() + "/public/posts/This-is-the-Second-Post.html");
        final DataFile expectedThree = new DataFile(tmp.getRoot().toString() + "/public/posts/This-is-the-Third-Post.html");
        assertThat(foundFiles, containsInAnyOrder(expectedOne, expectedTwo, expectedThree));
        assertThat(
            expectedOne.readContent(ENCODING),
            isSimilarTo("<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "    <head>\n"
                + "        <meta name=\"robots\" content=\"all\"/>\n"
                + "        <meta name=\"description\" content=\"\"/>\n"
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
                + "        <article>\n"
                + "    <h3>This is the First Post</h3>"
                + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor "
                + "invidunt ut labore et dolore magna aliquyam.</p>"
                + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor "
                + "invidunt ut labore et dolore magna aliquyam.</p>\n"
                + "</article>\n"
                + "        </section>\n"
                + "\n"
                + "        <footer>\n"
                + "            Powered by JUberblog.\n"
                + "        </footer>\n"
                + "\n"
                + "        <script type=\"text/javascript\" src=\"http://uberblog.local//js/main.js\"></script>\n"
                + "    </body>\n"
                + "</html>").ignoreWhitespace());
        assertThat(
            expectedTwo.readContent(ENCODING),
            isSimilarTo("<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "    <head>\n"
                + "        <meta name=\"robots\" content=\"all\"/>\n"
                + "        <meta name=\"description\" content=\"\"/>\n"
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
                + "        <article>\n"
                + "    <h3>This is the Second Post</h3>"
                + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor "
                + "invidunt ut labore et dolore magna aliquyam.</p>"
                + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor "
                + "invidunt ut labore et dolore magna aliquyam.</p>\n"
                + "</article>\n"
                + "        </section>\n"
                + "\n"
                + "        <footer>\n"
                + "            Powered by JUberblog.\n"
                + "        </footer>\n"
                + "\n"
                + "        <script type=\"text/javascript\" src=\"http://uberblog.local//js/main.js\"></script>\n"
                + "    </body>\n"
                + "</html>").ignoreWhitespace());
        assertThat(
            expectedThree.readContent(ENCODING),
            isSimilarTo("<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "    <head>\n"
                + "        <meta name=\"robots\" content=\"all\"/>\n"
                + "        <meta name=\"description\" content=\"\"/>\n"
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
                + "        <article>\n"
                + "    <h3>This is the Third Post</h3>"
                + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor "
                + "invidunt ut labore et dolore magna aliquyam.</p>"
                + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor "
                + "invidunt ut labore et dolore magna aliquyam.</p>\n"
                + "</article>\n"
                + "        </section>\n"
                + "\n"
                + "        <footer>\n"
                + "            Powered by JUberblog.\n"
                + "        </footer>\n"
                + "\n"
                + "        <script type=\"text/javascript\" src=\"http://uberblog.local//js/main.js\"></script>\n"
                + "    </body>\n"
                + "</html>").ignoreWhitespace()
        );
    }

}
