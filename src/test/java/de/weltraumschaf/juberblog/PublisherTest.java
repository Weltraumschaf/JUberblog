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

import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import java.io.IOException;
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
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PublisherTest extends JUberblogTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void publishPosts() throws URISyntaxException, IOException {
        final Publisher sut = new Publisher(
                createPath("posts"),
                tmp.getRoot().toPath(),
                createPath("layout.ftl"),
                createPath("post.ftl"),
                ENCODING,
                "http://localhost/posts"
        );

        final Collection<Page> pages = sut.publish();
        assertThat(pages.size(), is(3));
        assertThat(pages, containsInAnyOrder(
                new Page(
                        "This is the First Post",
                        "http://localhost/posts/This-is-the-First-Post.html",
                        "This is the first post.",
                        new DateTime("2014-05-30T21:29:20")),
                new Page(
                        "This is the Second Post",
                        "http://localhost/posts/This-is-the-Second-Post.html",
                        "This is the second post.",
                        new DateTime("2014-06-30T23:25:44")),
                new Page(
                        "This is the Third Post",
                        "http://localhost/posts/This-is-the-Third-Post.html",
                        "This is the third post.",
                        new DateTime("2014-07-28T17:44:13"))
        ));

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.HTML).find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(3));
        final DataFile expectedOne = new DataFile(tmp.getRoot().toString() + "/This-is-the-First-Post.html");
        final DataFile expectedTwo = new DataFile(tmp.getRoot().toString() + "/This-is-the-Second-Post.html");
        final DataFile expectedThree = new DataFile(tmp.getRoot().toString() + "/This-is-the-Third-Post.html");
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
