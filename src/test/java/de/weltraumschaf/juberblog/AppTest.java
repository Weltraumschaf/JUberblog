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

import de.weltraumschaf.commons.guava.Maps;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.freemarkerdown.Layout;
import de.weltraumschaf.freemarkerdown.Options;
import de.weltraumschaf.freemarkerdown.PreProcessor;
import de.weltraumschaf.freemarkerdown.PreProcessors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link App}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AppTest {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    private static final String ENCODING = "utf-8";
    private static final String BASE = "/de/weltraumschaf/juberblog/";

    private Path createPath(final String name) throws URISyntaxException {
        return Paths.get(getClass().getResource(BASE + name).toURI());
    }

    @Test
    public void Renderer_render() throws URISyntaxException, UnsupportedEncodingException, IOException {
        final Renderer renderer = new Renderer(createPath("layout.ftl"), createPath("post.ftl"), ENCODING);

        final String html = renderer.render(createPath("posts/2014-05-30T21.29.20_This-is-the-First-Post.md"));

        assertThat(html, is(
                "<!DOCTYPE html>\n"
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
    }

    @Test
    public void MarkdownFilesFinder_findFiles() throws URISyntaxException, IOException {
        final FilesFinder sut = new FilesFinder(FileNameExtension.MARKDOWN);

        final Collection<DataFile> foundFiles = sut.find(createPath("posts"));

        assertThat(foundFiles.size(), is(3));
        assertThat(foundFiles, containsInAnyOrder(
                new DataFile(createPath("posts/2014-05-30T21.29.20_This-is-the-First-Post.md").toString()),
                new DataFile(createPath("posts/2014-06-30T23.25.44_This-is-the-Second-Post.md").toString()),
                new DataFile(createPath("posts/2014-07-28T17.44.13_This-is-the-Third-Post.md").toString())
        ));
    }

    @Test
    public void DataFile_getBareName() throws URISyntaxException {
        final DataFile sut = new DataFile(createPath("posts/2014-05-30T21.29.20_This-is-the-First-Post.md").toString());

        assertThat(sut.getBareName(), is("This-is-the-First-Post"));
    }

    @Test
    public void Publisher_publishPosts() throws URISyntaxException, IOException {
        final Publisher sut = new Publisher(
                createPath("posts"),
                tmp.getRoot().toPath(),
                createPath("layout.ftl"),
                createPath("post.ftl"),
                ENCODING
        );

        sut.publish();

        final Collection<DataFile> foundFiles = new FilesFinder(FileNameExtension.HTML).find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(3));
        assertThat(foundFiles, containsInAnyOrder(
                new DataFile(tmp.getRoot().toString() + "/This-is-the-First-Post.html"),
                new DataFile(tmp.getRoot().toString() + "/This-is-the-Second-Post.html"),
                new DataFile(tmp.getRoot().toString() + "/This-is-the-Third-Post.html")
        ));
    }

}
