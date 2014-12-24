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
package integrationtests;

import de.weltraumschaf.juberblog.IntegrationTestCase;
import de.weltraumschaf.juberblog.app.App;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests the publishing of a whole blog.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PublishTest extends IntegrationTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void publishWholeBlog() throws URISyntaxException, IOException {
        final Dirs dirs = createDirs();
        copyData(dirs);
        copyTemplates(dirs);

        App.main(createApp(new String[]{
            "publish",
            "-c", createPath("/integrationtests/", "config.properties").toString(),
            "-l", tmp.getRoot().getAbsolutePath()}));
        // TODO Assert posts
        // TODO Assert sites
        // TODO Assert index
        // TODO Assert site map
        // TODO Assert feed
    }

    private Dirs createDirs() throws IOException {
        final Dirs dirs = new Dirs();
        dirs.dataDir = tmp.newFolder("data").toPath();
        dirs.postsData = dirs.dataDir.resolve("posts");
        dirs.sitesData = dirs.dataDir.resolve("sites");
        Files.createDirectories(dirs.postsData);
        Files.createDirectories(dirs.sitesData);
        dirs.publicDir = tmp.newFolder("public").toPath();
        Files.createDirectories(dirs.publicDir.resolve("posts"));
        Files.createDirectories(dirs.publicDir.resolve("sites"));
        dirs.templatesDir = tmp.newFolder("templates").toPath();
        return dirs;
    }

    private void copyData(final Dirs dirs) throws IOException, URISyntaxException {
        for (final String name : Arrays.asList(
                "2014-05-30T21.29.20_This-is-the-First-Post.md",
                "2014-06-30T23.25.44_This-is-the-Second-Post.md",
                "2014-07-28T17.44.13_This-is-the-Third-Post.md")) {
            Files.copy(
                    createPath("posts/" + name),
                    dirs.postsData.resolve(name));
        }

        for (final String name : Arrays.asList(
                "2014-08-30T15.29.20_Site-One.md",
                "2014-09-30T15.29.20_Site-Two.md")) {
            Files.copy(
                    createPath("sites/" + name),
                    dirs.sitesData.resolve(name));
        }
    }

    private void copyTemplates(final Dirs dirs) throws IOException, URISyntaxException {
        for (final String name : Arrays.asList(
                "feed.ftl",
                "index.ftl",
                "layout.ftl",
                "post.ftl",
                "site.ftl",
                "site_map.ftl")) {
            Files.copy(
                    createPath(SCAFOLD_PACKAGE_PREFIX + name),
                    dirs.templatesDir.resolve(name));
        }
    }

    private static final class Dirs {

        private Path dataDir;
        private Path postsData;
        private Path sitesData;
        private Path publicDir;
        private Path templatesDir;
    }

}
