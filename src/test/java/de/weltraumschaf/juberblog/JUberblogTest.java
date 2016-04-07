package de.weltraumschaf.juberblog;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.core.BlogConfiguration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Templates;
import de.weltraumschaf.juberblog.options.Options;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Properties;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@lnk JUberblog}.
 *
 * @author Sven Strittmatter
 */
public class JUberblogTest extends BaseTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test
    @Ignore
    public void generate() throws URISyntaxException, ApplicationException {
        final Path location = tmp.getRoot().toPath();
        final Options options = new Options();
        final IO io = mock(IO.class);

        final JUberblog product = JUberblog.generateWithDefaultConfig(options, io);

        assertThat(product.options(), is(options));
        assertThat(product.io(), is(io));
        assertThat(product.directories(), is(new Directories(
                location.resolve("data"),
                location.resolve("public"))));
        assertThat(product.templates(), is(new Templates(
                location.resolve("templates/layout.ftl"),
                location.resolve("templates/post.ftl"),
                location.resolve("templates/site.ftl"),
                location.resolve("templates/feed.ftl"),
                location.resolve("templates/index.ftl"),
                location.resolve("templates/site_map.ftl"),
                location.resolve("templates/create/post_or_site.md.ftl"))));
        final Properties config = new Properties();
        config.setProperty(BlogConfiguration.TITLE, "Blog Title");
        config.setProperty(BlogConfiguration.DESCRIPTION, "Blog Description");
        config.setProperty(BlogConfiguration.SITE_URI, "http://uberblog.local/");
        config.setProperty(BlogConfiguration.ENCODING, "UTF-8");
        config.setProperty(BlogConfiguration.PUBLIC_DIR, "public");
        config.setProperty(BlogConfiguration.LANGUAGE, "en");
        config.setProperty(BlogConfiguration.DATA_DIR, "data");
        config.setProperty(BlogConfiguration.TEMPLATE_DIR, "templates");
        assertThat(product.configuration(), is(new BlogConfiguration(config)));
    }

}
