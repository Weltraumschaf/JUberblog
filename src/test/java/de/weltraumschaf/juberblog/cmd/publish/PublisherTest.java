package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.juberblog.cmd.publish.Publisher;
import de.weltraumschaf.juberblog.BaseTestCase;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link Publisher}.
 *
 * @author Sven Strittmatter
 */
public class PublisherTest extends BaseTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void publishPosts() throws URISyntaxException, IOException {
        final Publisher sut = new Publisher(
                createTemplates(),
                createDirs(tmp),
                createConfig(),
                Page.Type.POST
        );

        final Collection<Page> pages = sut.publish();
        assertThat(pages.size(), is(3));
        assertThat(pages, containsInAnyOrder(
                new Page(
                        "This is the First Post",
                        URI.create("http://www.myblog.com/posts/This-is-the-First-Post.html"),
                        "This is the first post.",
                        new DateTime("2014-05-30T21:29:20"), Page.Type.POST),
                new Page(
                        "This is the Second Post",
                        URI.create("http://www.myblog.com/posts/This-is-the-Second-Post.html"),
                        "This is the second post.",
                        new DateTime("2014-06-30T23:25:44"), Page.Type.POST),
                new Page(
                        "This is the Third Post",
                        URI.create("http://www.myblog.com/posts/This-is-the-Third-Post.html"),
                        "This is the third post.",
                        new DateTime("2014-07-28T17:44:13"), Page.Type.POST)
        ));

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.HTML).find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(3));
        final DataFile expectedOne = new DataFile(tmp.getRoot().toString() + "/posts/This-is-the-First-Post.html");
        final DataFile expectedTwo = new DataFile(tmp.getRoot().toString() + "/posts/This-is-the-Second-Post.html");
        final DataFile expectedThree = new DataFile(tmp.getRoot().toString() + "/posts/This-is-the-Third-Post.html");
        assertThat(foundFiles, containsInAnyOrder(expectedOne, expectedTwo, expectedThree));
        assertThat(
                expectedOne.readContent(ENCODING),
                is("<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "    <body>\n"
                        + "        <h1>NAME</h1>\n"
                        + "        <h2>DESCRIPTION</h2>\n"
                        + "\n"
                        + "        <article>\n"
                        + "    <h3>This is the First Post</h3>"
                        + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor "
                        + "invidunt ut labore et dolore magna aliquyam.</p>"
                        + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor "
                        + "invidunt ut labore et dolore magna aliquyam.</p>\n"
                        + "</article>\n"
                        + "    </body>\n"
                        + "</html>"));
        assertThat(
                expectedTwo.readContent(ENCODING),
                is("<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "    <body>\n"
                        + "        <h1>NAME</h1>\n"
                        + "        <h2>DESCRIPTION</h2>\n"
                        + "\n"
                        + "        <article>\n"
                        + "    <h3>This is the Second Post</h3>"
                        + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor "
                        + "invidunt ut labore et dolore magna aliquyam.</p>"
                        + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor "
                        + "invidunt ut labore et dolore magna aliquyam.</p>\n"
                        + "</article>\n"
                        + "    </body>\n"
                        + "</html>"));
        assertThat(
                expectedThree.readContent(ENCODING),
                is("<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "    <body>\n"
                        + "        <h1>NAME</h1>\n"
                        + "        <h2>DESCRIPTION</h2>\n"
                        + "\n"
                        + "        <article>\n"
                        + "    <h3>This is the Third Post</h3>"
                        + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor "
                        + "invidunt ut labore et dolore magna aliquyam.</p>"
                        + "<p>Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor "
                        + "invidunt ut labore et dolore magna aliquyam.</p>\n"
                        + "</article>\n"
                        + "    </body>\n"
                        + "</html>")
        );
    }

}
