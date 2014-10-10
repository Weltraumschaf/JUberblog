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

import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.Directories;
import de.weltraumschaf.juberblog.model.DataFile;
import de.weltraumschaf.juberblog.model.MetaData;
import de.weltraumschaf.juberblog.model.PublishedPages;
import de.weltraumschaf.juberblog.template.Configurations;
import freemarker.template.Configuration;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link PublisherTest}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PublisherTest {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();
    private static final String TEMPLATE_DIRECTORRY = Constants.PACKAGE_BASE.toString() + "/template";
    private static final String FIXTURES_DIRECTORY = Constants.PACKAGE_BASE.toString() + "/cmd/publish";
    private PublishedPages pages = new PublishedPages();
    private File data;
    private File publishedPosts;
    private File publishedSites;
    private File publishedPostDrafts;
    private File publishedDraftSites;
    private Directories dirs;
    private Configuration templateConfig;
    private Publisher sut;

    @Before
    public void prepareFixtures() throws URISyntaxException, IOException {
        publishedPosts = tmp.newFolder("posts");
        publishedSites = tmp.newFolder("sites");
        tmp.newFolder("drafts");
        publishedPostDrafts = tmp.newFolder("drafts/posts");
        publishedDraftSites = tmp.newFolder("drafts/sites");
        data = new File(getClass().getResource(FIXTURES_DIRECTORY).toURI());
        dirs = new Directories(
                data.getAbsolutePath(),
                "foo",
                tmp.getRoot().getAbsolutePath());
        templateConfig = Configurations.forTests(Configurations.SCAFFOLD_TEMPLATE_DIR);
        sut = new Publisher(dirs, templateConfig, "http://www.foobar.com/", pages);
    }

    @Test
    public void readData() throws IOException {
        sut.readData();
        assertThat(sut.getPostsData(), containsInAnyOrder(
                DataFile.from(
                        new File(data.getAbsolutePath()
                                + "/posts/2014-05-30T21.29.20_This-is-the-First-Post.md").toPath(),
                        DataFile.Type.POST),
                DataFile.from(
                        new File(data.getAbsolutePath()
                                + "/posts/2014-05-30T21.34.20_Second-Post-About-Lorem.md").toPath(),
                        DataFile.Type.POST)
        ));
        assertThat(sut.getSitesData(), containsInAnyOrder(
                DataFile.from(
                        new File(data.getAbsolutePath()
                                + "/sites/2014-05-30T21.21.18_About-me.md").toPath(),
                        DataFile.Type.SITE),
                DataFile.from(
                        new File(data.getAbsolutePath()
                                + "/sites/2014-05-30T21.29.20_Projects.md").toPath(),
                        DataFile.Type.SITE)
        ));
    }

    @Test
    public void createUri() throws URISyntaxException {
        final DataFile post = new DataFile(
            "filename",
            "basename",
            1L,
            2L,
            "slug",
            "headline",
            "markdown",
            new MetaData(),
            DataFile.Type.POST);
        assertThat(sut.createUri(post), is(URI.create("http://www.foobar.com/posts/slug.html")));

        final DataFile site = new DataFile(
            "filename",
            "basename",
            1L,
            2L,
            "slug",
            "headline",
            "markdown",
            new MetaData(),
            DataFile.Type.SITE);
        assertThat(sut.createUri(site), is(URI.create("http://www.foobar.com/sites/slug.html")));
    }

    @Test
    public void publishedFileExists() throws IOException {
        final File foo = tmp.newFile("foo");
        final File bar = tmp.newFile("bar");
        final File baz = tmp.newFile("baz");

        assertThat(sut.publishedFileExists(foo), is(true));
        assertThat(sut.publishedFileExists(bar), is(true));
        assertThat(sut.publishedFileExists(baz), is(true));
        assertThat(sut.publishedFileExists(new File(tmp.getRoot(), "snafu")),
                is(false));
    }

    /*
     Assert files:
     - 2 posts
     - 2 sites
     - sitemap.xml
     - feed.xml
     - index.html
     */
    @Test
    public void execute_default_noSitesNoDraftsNoPurge() throws PublishingSubCommandExcpetion {
        sut.execute();

        assertThat(new File(publishedPosts, "This-is-the-First-Post.html").exists(), is(true));
        assertThat(new File(publishedPosts, "Second-Post-About-Lorem.html").exists(), is(true));
        assertThat(new File(publishedSites, "About-me.html").exists(), is(false));
        assertThat(new File(publishedSites, "Projects.html").exists(), is(false));
        // TODO Add assertions for sitemap.xml, feed.xml and index.html, sites and posts data collection.
    }

    @Test
    public void execute_default_sitesNoDraftsNoPurge() throws PublishingSubCommandExcpetion {
        sut.setSites(true);
        sut.execute();

        assertThat(new File(publishedPosts, "This-is-the-First-Post.html").exists(), is(true));
        assertThat(new File(publishedPosts, "Second-Post-About-Lorem.html").exists(), is(true));
        assertThat(new File(publishedSites, "About-me.html").exists(), is(true));
        assertThat(new File(publishedSites, "Projects.html").exists(), is(true));
        // TODO Add assertions for sitemap.xml, feed.xml and index.html, sites and posts data collection.
    }

}
