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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
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
