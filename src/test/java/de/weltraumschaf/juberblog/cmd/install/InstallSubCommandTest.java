package de.weltraumschaf.juberblog.cmd.install;

import de.weltraumschaf.juberblog.cmd.install.InstallSubCommand;
import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.testing.rules.CapturedOutput;
import de.weltraumschaf.juberblog.BaseTestCase;
import de.weltraumschaf.juberblog.options.Options;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
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
    public final TemporaryFolder tmp = new TemporaryFolder();
    //CHECKSTYLE:ON

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    @Rule
    //CHECKSTYLE:OFF
    public final CapturedOutput output = new CapturedOutput();
    //CHECKSTYLE:ON

    private final IO io = mock(IO.class);

    private String getRootDir() {
        return tmp.getRoot().getAbsolutePath();
    }

    private InstallSubCommand createSut(final String... args) throws URISyntaxException, IOException {
        final Options opt = new Options();
        opt.parse(args);
        return new InstallSubCommand(createRegistry(tmp, opt, io, false));
    }

    @Test
    public void execute_withLocationAndVerboseOption() throws ApplicationException, URISyntaxException, IOException {
        final InstallSubCommand sut = createSut("install", "-l", getRootDir(), "--verbose");
        sut.setSrcJar(TestingSourceJarProvider.newProvider());

        sut.execute();
        verify(io, times(1)).println("Install scaffold to '"
                + getRootDir()
                + "'...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/README.md to "
                + getRootDir()
                + "/README.md ...");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/templates");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/templates/site_map.ftl to "
                + getRootDir()
                + "/templates/site_map.ftl ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/templates/site.ftl to "
                + getRootDir()
                + "/templates/site.ftl ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/templates/post.ftl to "
                + getRootDir()
                + "/templates/post.ftl ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/templates/layout.ftl to "
                + getRootDir()
                + "/templates/layout.ftl ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/templates/index.ftl to "
                + getRootDir()
                + "/templates/index.ftl ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/templates/feed.ftl to "
                + getRootDir()
                + "/templates/feed.ftl ...");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/templates/create");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/templates/create/"
                + "post_or_site.md.ftl to "
                + getRootDir()
                + "/templates/create/post_or_site.md.ftl ...");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/public");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/robots.txt to "
                + getRootDir()
                + "/public/robots.txt ...");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/public/sites");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/sites/README to "
                + getRootDir()
                + "/public/sites/README ...");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/public/posts");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/posts/README to "
                + getRootDir()
                + "/public/posts/README ...");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/public/js");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/js/main.js to "
                + getRootDir()
                + "/public/js/main.js ...");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/public/img");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/rss.png to "
                + getRootDir()
                + "/public/img/rss.png ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/logo.jpg to "
                + getRootDir()
                + "/public/img/logo.jpg ...");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/img/favicon.ico to "
                + getRootDir()
                + "/public/img/favicon.ico ...");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/public/drafts");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/public/drafts/sites");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/drafts/sites/README to "
                + getRootDir()
                + "/public/drafts/sites/README ...");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/public/drafts/posts");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/drafts/posts/README to "
                + getRootDir()
                + "/public/drafts/posts/README ...");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/public/css");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/public/css/README to "
                + getRootDir()
                + "/public/css/README ...");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/data");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/data/sites");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/data/sites/README to "
                + getRootDir()
                + "/data/sites/README ...");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/data/posts");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/data/posts/README to "
                + getRootDir()
                + "/data/posts/README ...");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/data/drafts");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/data/drafts/sites");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/data/drafts/sites/README to "
                + getRootDir()
                + "/data/drafts/sites/README ...");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/data/drafts/posts");
        verify(io, times(1)).println("Copy file /de/weltraumschaf/juberblog/scaffold/data/drafts/posts/README to "
                + getRootDir()
                + "/data/drafts/posts/README ...");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/configuration");
        verify(io, times(1)).println(
                "Copy file /de/weltraumschaf/juberblog/scaffold/configuration/configuration.sample.properties to "
                + getRootDir()
                + "/configuration/configuration.sample.properties ...");
        verify(io, times(1)).println("Create directory "
                + getRootDir()
                + "/sass");
        verify(io, times(1)).println(
                "Copy file /de/weltraumschaf/juberblog/scaffold/sass/_normalize.scss to "
                + getRootDir()
                + "/sass/_normalize.scss ...");
        verify(io, times(1)).println(
                "Copy file /de/weltraumschaf/juberblog/scaffold/sass/main.scss to "
                + getRootDir()
                + "/sass/main.scss ...");

        final Collection<File> installedFiles = FileUtils.listFiles(tmp.getRoot(), null, true);
        assertThat(installedFiles.toString(), installedFiles, hasSize(EXPECTED_FILES.size()));

        for (final String path : EXPECTED_FILES) {
            assertThat(path, installedFiles.contains(new File(tmp.getRoot(), path)), is(true));
        }
    }

    @Test
    public void execute_withLocation() throws ApplicationException, URISyntaxException, IOException {
        final InstallSubCommand sut = createSut("install", "-l", getRootDir());
        sut.setSrcJar(TestingSourceJarProvider.newProvider());

        sut.execute();
        verify(io, times(1)).println("Install scaffold to '" + getRootDir() + "'...");

        final Collection<File> installedFiles = FileUtils.listFiles(tmp.getRoot(), null, true);
        assertThat(installedFiles.toString(), installedFiles, hasSize(EXPECTED_FILES.size()));

        for (final String path : EXPECTED_FILES) {
            assertThat(path, installedFiles.contains(new File(tmp.getRoot(), path)), is(true));
        }
    }

}
