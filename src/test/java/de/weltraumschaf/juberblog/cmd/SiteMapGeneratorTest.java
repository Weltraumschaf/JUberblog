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

package de.weltraumschaf.juberblog.cmd;

import de.weltraumschaf.juberblog.files.PublishedFile;
import java.io.File;
import java.io.IOException;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Ignore;

/**
 * Tests for {@link SiteMapGenerator}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SiteMapGeneratorTest {

    @Rule
    //CHECKSTYLE:OFF
    public final TemporaryFolder tmp = new TemporaryFolder();
    //CHECKSTYLE:ON
    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON
    private final SiteMapGenerator sut = new SiteMapGenerator();

    @Test
    public void addDirecotry_throwsExceptionIfBasedirIsNull() throws IOException {
        thrown.expect(NullPointerException.class);
        sut.addDirecotry(null, tmp.newFolder());
    }

    @Test
    public void addDirecotry_throwsExceptionIfBasedirIsEmpty() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        sut.addDirecotry("", tmp.newFolder());
    }

    @Test
    public void addDirecotry_throwsExceptionIfDirectoryIsNull() {
        thrown.expect(NullPointerException.class);
        sut.addDirecotry("foo", null);
    }

    @Test
    public void addDirecotry_throwsExceptionIfNotDirectory() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        sut.addDirecotry("foo", tmp.newFile());
    }

    @Test
    public void findPublishedFiles() throws IOException {
        final File sites = tmp.newFolder("sites");
        assertThat(new File(sites, "foo.html").createNewFile(), is(true));
        assertThat(new File(sites, "bar.html").createNewFile(), is(true));
        assertThat(new File(sites, "baz.html").createNewFile(), is(true));
        assertThat(new File(sites, "snafu").createNewFile(), is(true));
        assertThat(new File(sites, "README.md").createNewFile(), is(true));
        final File posts = tmp.newFolder("posts");
        assertThat(new File(posts, "foo.html").createNewFile(), is(true));
        assertThat(new File(posts, "bar.html").createNewFile(), is(true));
        assertThat(new File(posts, "snafu.txt").createNewFile(), is(true));

        assertThat(sut.findPublishedFiles(), hasSize(0));
        sut.addDirecotry("http://www.foobar.com/sites/", sites);
        sut.addDirecotry("http://www.foobar.com/posts/", posts);

        assertThat(sut.findPublishedFiles(), hasSize(5));
        assertThat(sut.findPublishedFiles(), containsInAnyOrder(
                new PublishedFile("http://www.foobar.com/sites/", new File(sites.getAbsolutePath() + "/foo.html")),
                new PublishedFile("http://www.foobar.com/sites/", new File(sites.getAbsolutePath() + "/bar.html")),
                new PublishedFile("http://www.foobar.com/sites/", new File(sites.getAbsolutePath() + "/baz.html")),
                new PublishedFile("http://www.foobar.com/posts/", new File(posts.getAbsolutePath() + "/foo.html")),
                new PublishedFile("http://www.foobar.com/posts/", new File(posts.getAbsolutePath() + "/bar.html"))
        ));
    }

    @Test
    public void execute() throws IOException {
        final File sites = tmp.newFolder("sites");
        final File posts = tmp.newFolder("posts");
        sut.addDirecotry("http://www.foobar.com/sites/", sites);
        sut.addDirecotry("http://www.foobar.com/posts/", posts);
        assertThat(sut.getResult(), is(equalTo("")));

        sut.execute();
        assertThat(sut.getResult(), is(equalTo("")));
    }

}
