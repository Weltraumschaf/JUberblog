package de.weltraumschaf.juberblog.cmd.install;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.testing.rules.CapturedOutput;
import de.weltraumschaf.juberblog.BaseTestCase;
import de.weltraumschaf.juberblog.options.Options;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link InstallSubCommand}.
 *
 * @author Sven Strittmatter
 */
public class InstallSubCommandTest extends BaseTestCase {

    private static final Collection<String> EXPECTED_FILES = Arrays.asList(
            "configuration/configuration.sample.properties",
            "data/drafts/posts/README",
            "data/drafts/sites/README",
            "data/posts/README",
            "data/sites/README",
            "public/css/README",
            "public/drafts/posts/README",
            "public/drafts/sites/README",
            "public/img/favicon.ico",
            "public/img/logo.jpg",
            "public/img/rss.png",
            "public/js/main.js",
            "public/posts/README",
            "public/robots.txt",
            "public/sites/README",
            "README.md",
            "templates/create/post_or_site.md.ftl",
            "templates/feed.ftl",
            "templates/index.ftl",
            "templates/layout.ftl",
            "templates/post.ftl",
            "templates/site.ftl",
            "templates/site_map.ftl",
            "sass/_normalize.scss",
            "sass/main.scss"
    );

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    private String getRootDir() {
        return tmp.getRoot().getAbsolutePath();
    }

    private InstallSubCommand createSut(final String... args) throws URISyntaxException, IOException {
        final Options opt = new Options();
        opt.parse(args);
        return new InstallSubCommand(createRegistry(opt, false));
    }

    @Test
    public void execute_withLocation() throws ApplicationException, URISyntaxException, IOException {
        final InstallSubCommand sut = createSut("install", "-l", getRootDir());
        sut.setSrcJar(TestingSourceJarProvider.newProvider());

        sut.execute();

        final Collection<File> installedFiles = FileUtils.listFiles(tmp.getRoot(), null, true);
        assertThat(installedFiles.toString(), installedFiles, hasSize(EXPECTED_FILES.size()));

        for (final String path : EXPECTED_FILES) {
            assertThat(path, installedFiles.contains(new File(tmp.getRoot(), path)), is(true));
        }
    }

}
