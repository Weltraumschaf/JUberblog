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
import org.junit.Ignore;
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
    @Ignore("Not ready yet.")
    public void publishWholeBlog() throws URISyntaxException, IOException {
        final Path dataDir = tmp.newFolder("data").toPath();
        final Path postsData = dataDir.resolve("posts");
        final Path sitesData = dataDir.resolve("sites");
        Files.createDirectories(postsData);
        Files.createDirectories(sitesData);
        final Path publicDir = tmp.newFolder("public").toPath();
        final Path templatesDir = tmp.newFolder("templates").toPath();
        // TODO Copy data.
        // TODO Copy templates.
        App.main(createApp(new String[]{
            "publish",
            "-c", createPath("/integrationtests/", "config.properties").toString(),
            "-l", tmp.getRoot().getAbsolutePath(),
        }));
    }

}
