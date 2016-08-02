package de.weltraumschaf.juberblog;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.juberblog.core.BlogConfiguration;
import de.weltraumschaf.juberblog.core.Constants;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Templates;
import de.weltraumschaf.juberblog.core.Verbose;
import de.weltraumschaf.juberblog.options.Options;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Some helper stuff.
 *
 * @author Sven Strittmatter
 */
public abstract class BaseTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    protected static final String ENCODING = "utf-8";
    protected static final String SCAFOLD_PACKAGE_PREFIX = "scaffold/templates/";
    private static final String BASE = "/de/weltraumschaf/juberblog/";

    protected final Path createPath(final String name) throws URISyntaxException {
        return createPath(BASE, name);
    }

    protected final Path createPath(final String base, final String name) throws URISyntaxException {
        return Paths.get(getClass().getResource(base + name).toURI());
    }

    protected final Directories createDirs(final TemporaryFolder tmp) throws IOException, URISyntaxException {
        return createDirs(tmp, true);
    }

    private Directories createDirs(final TemporaryFolder tmp, final boolean createOutputDirs) throws IOException, URISyntaxException {
        if (createOutputDirs) {
            tmp.newFolder("posts");
            tmp.newFolder("sites");
        }

        return new Directories(
            createPath("."),
            tmp.getRoot().toPath());
    }

    protected final Templates createTemplates() throws URISyntaxException {
        return new Templates(
            createPath(SCAFOLD_PACKAGE_PREFIX + "layout.ftl"),
            createPath(SCAFOLD_PACKAGE_PREFIX + "post.ftl"),
            createPath(SCAFOLD_PACKAGE_PREFIX + "site.ftl"),
            createPath(SCAFOLD_PACKAGE_PREFIX + "feed.ftl"),
            createPath(SCAFOLD_PACKAGE_PREFIX + "index.ftl"),
            createPath(SCAFOLD_PACKAGE_PREFIX + "site_map.ftl"),
            createPath(SCAFOLD_PACKAGE_PREFIX + "create/post_or_site.md.ftl"));
    }

    protected final BlogConfiguration createConfig() {
        return new BlogConfiguration(createProperties());
    }

    protected final Properties createProperties() {
        final Properties config = new Properties();
        config.setProperty(BlogConfiguration.DATA_DIR, "/data");
        config.setProperty(BlogConfiguration.DESCRIPTION, "Blog Description");
        config.setProperty(BlogConfiguration.ENCODING, "utf-8");
        config.setProperty(BlogConfiguration.PUBLIC_DIR, "/htdocs");
        config.setProperty(BlogConfiguration.LANGUAGE, "en");
        config.setProperty(BlogConfiguration.SITE_URI, "http://www.myblog.com/");
        config.setProperty(BlogConfiguration.TEMPLATE_DIR, "/templates");
        config.setProperty(BlogConfiguration.TITLE, "Blog Title");

        return config;
    }

    protected final JUberblog createRegistry(final boolean createOutputDirs) throws URISyntaxException, IOException {
        return createRegistry(createOptions(), createOutputDirs);
    }

    protected final JUberblog createRegistry(final Options options, final boolean createOutputDirs) throws URISyntaxException, IOException {
        return JUberblog.Builder.create()
            .directories(createDirs(tmp, createOutputDirs))
            .templates(createTemplates())
            .configuration(createConfig())
            .options(options)
            .io(createIo())
            .verbose(createVervose())
            .fmd(createFmd())
            .version(createVersion())
            .product();
    }

    protected final Options createOptions() {
        return new Options();
    }

    protected final FreeMarkerDown createFmd() {
        return FreeMarkerDown.create("utf-8");
    }

    protected final IO createIo() {
        final IO io = mock(IO.class);
        when(io.getStderr()).thenReturn(mock(PrintStream.class));
        when(io.getStdout()).thenReturn(mock(PrintStream.class));
        return io;
    }

    protected final Verbose createVervose() {
        return new Verbose(false, mock(PrintStream.class));
    }

    protected final Version createVersion() throws IOException {
        final Version version = new Version(Constants.PACKAGE_BASE.toString() + "/version.properties");
        version.load();
        return version;
    }
}
