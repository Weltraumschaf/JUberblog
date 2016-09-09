package de.weltraumschaf.juberblog;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.commons.application.Version;
import de.weltraumschaf.freemarkerdown.FreeMarkerDown;
import de.weltraumschaf.juberblog.core.*;
import de.weltraumschaf.juberblog.options.Options;

import java.io.File;
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
    protected static final String SCAFFOLD_PACKAGE_PREFIX = "scaffold/templates/";
    private static final String BASE = "/de/weltraumschaf/juberblog/";

    protected final Path createPath(final String name) throws URISyntaxException {
        return createPath(BASE, name);
    }

    protected final Path createPath(final String base, final String name) throws URISyntaxException {
        return Paths.get(getClass().getResource(base + name).toURI());
    }

    protected final Directories createDirectories() {
        return createDirectories(true);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private Directories createDirectories(boolean createDirs) {
        final File dataDir = new File(tmp.getRoot(), "data");

        if (createDirs) {
            dataDir.mkdir();
            new File(dataDir, Constants.POSTS_DIR.toString()).mkdir();
            new File(dataDir, Constants.SITES_DIR.toString()).mkdir();
            File drafts = new File(dataDir, Constants.DRAFTS_DIR.toString());
            drafts.mkdir();
            new File(drafts, Constants.POSTS_DIR.toString()).mkdir();
            new File(drafts, Constants.SITES_DIR.toString()).mkdir();
        }

        final File outputDir = new File(tmp.getRoot(), "public");

        if (createDirs) {
            outputDir.mkdir();
            new File(outputDir, Constants.POSTS_DIR.toString()).mkdir();
            new File(outputDir, Constants.SITES_DIR.toString()).mkdir();
            final File drafts = new File(outputDir, Constants.DRAFTS_DIR.toString());
            drafts.mkdir();
            new File(drafts, Constants.POSTS_DIR.toString()).mkdir();
            new File(drafts, Constants.SITES_DIR.toString()).mkdir();
        }

        return new Directories(dataDir.toPath(), outputDir.toPath());
    }

    protected final Templates createTemplates() throws URISyntaxException {
        return new Templates(
            createPath(SCAFFOLD_PACKAGE_PREFIX + "layout.ftl"),
            createPath(SCAFFOLD_PACKAGE_PREFIX + "post.ftl"),
            createPath(SCAFFOLD_PACKAGE_PREFIX + "site.ftl"),
            createPath(SCAFFOLD_PACKAGE_PREFIX + "feed.ftl"),
            createPath(SCAFFOLD_PACKAGE_PREFIX + "index.ftl"),
            createPath(SCAFFOLD_PACKAGE_PREFIX + "site_map.ftl"),
            createPath(SCAFFOLD_PACKAGE_PREFIX + "create/post_or_site.md.ftl"));
    }

    protected final BlogConfiguration createConfig() {
        return new BlogConfiguration(createProperties());
    }

    protected final Properties createProperties() {
        final Properties config = new Properties();
        config.setProperty(BlogConfigurationKeys.DATA_DIR.getKey(), "/data");
        config.setProperty(BlogConfigurationKeys.DESCRIPTION.getKey(), "Blog Description");
        config.setProperty(BlogConfigurationKeys.ENCODING.getKey(), "utf-8");
        config.setProperty(BlogConfigurationKeys.PUBLIC_DIR.getKey(), "/public");
        config.setProperty(BlogConfigurationKeys.LANGUAGE.getKey(), "en");
        config.setProperty(BlogConfigurationKeys.SITE_URI.getKey(), "http://uberblog.local/");
        config.setProperty(BlogConfigurationKeys.TEMPLATE_DIR.getKey(), "/templates");
        config.setProperty(BlogConfigurationKeys.TITLE.getKey(), "Blog Title");

        return config;
    }

    protected final JUberblog createRegistry() throws URISyntaxException, IOException {
        return createRegistry(true);
    }

    private JUberblog createRegistry(final boolean createDirs) throws URISyntaxException, IOException {
        return createRegistry(createOptions(), createDirs);
    }

    protected final JUberblog createRegistry(final Options options) throws URISyntaxException, IOException {
        return createRegistry(options, true);
    }

    protected final JUberblog createRegistry(final Options options, final boolean createDirs) throws URISyntaxException, IOException {
        return JUberblog.Builder.create()
            .directories(createDirectories(createDirs))
            .templates(createTemplates())
            .configuration(createConfig())
            .options(options)
            .io(createIo())
            .verbose(createVerbose())
            .fmd(createFmd())
            .version(createVersion())
            .product();
    }

    private Options createOptions() {
        return new Options();
    }

    private FreeMarkerDown createFmd() {
        return FreeMarkerDown.create("utf-8");
    }

    private IO createIo() {
        final IO io = mock(IO.class);
        when(io.getStderr()).thenReturn(mock(PrintStream.class));
        when(io.getStdout()).thenReturn(mock(PrintStream.class));
        return io;
    }

    private Verbose createVerbose() {
        return new Verbose(false, mock(PrintStream.class));
    }

    protected final Version createVersion() throws IOException {
        final Version version = new Version(Constants.PACKAGE_BASE.toString() + "/version.properties");
        version.load();
        return version;
    }
}
