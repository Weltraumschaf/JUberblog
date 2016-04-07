package de.weltraumschaf.juberblog;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.core.BlogConfiguration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Templates;
import de.weltraumschaf.juberblog.options.Options;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.junit.rules.TemporaryFolder;

/**
 * Some helper stuff.
 *
 * @author Sven Strittmatter
 */
public abstract class BaseTestCase {

    protected static final String ENCODING = "utf-8";
    protected static final String BASE = "/de/weltraumschaf/juberblog/";
    protected static final String SCAFOLD_PACKAGE_PREFIX = "scaffold/templates/";

    protected final Path createPath(final String name) throws URISyntaxException {
        return createPath(BASE, name);
    }

    protected final Path createPath(final String base, final String name) throws URISyntaxException {
        return Paths.get(getClass().getResource(base + name).toURI());
    }

    protected final Directories createDirs(final TemporaryFolder tmp) throws IOException, URISyntaxException {
        return createDirs(tmp, true);
    }

    protected final Directories createDirs(final TemporaryFolder tmp, final boolean createOutputDirs) throws IOException, URISyntaxException {
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

    protected final JUberblog createRegistry(final TemporaryFolder tmp, final Options options, final IO io, final boolean createOutputDirs) throws URISyntaxException, IOException {
        return JUberblog.Builder.create()
                .directories(createDirs(tmp, createOutputDirs))
                .templates(createTemplates())
                .configuration(createConfig())
                .options(options)
                .io(io)
                .product();
    }
}
