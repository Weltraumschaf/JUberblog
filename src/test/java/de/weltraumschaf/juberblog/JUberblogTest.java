package de.weltraumschaf.juberblog;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.app.Options;
import de.weltraumschaf.juberblog.core.Configuration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Templates;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Properties;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
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
    public void generate() throws URISyntaxException, ApplicationException {
        final Path location = tmp.getRoot().toPath();
        final Options options = new Options(location.toString(), createPath("config.properties").toString());
        final IO io = mock(IO.class);

        final JUberblog product = JUberblog.generate(options, io);

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
        config.setProperty(Configuration.TITLE, "Blog Title");
        config.setProperty(Configuration.DESCRIPTION, "Blog Description");
        config.setProperty(Configuration.SITE_URI, "http://uberblog.local/");
        config.setProperty(Configuration.ENCODING, "UTF-8");
        config.setProperty(Configuration.PUBLIC_DIR, "public");
        config.setProperty(Configuration.LANGUAGE, "en");
        config.setProperty(Configuration.DATA_DIR, "data");
        config.setProperty(Configuration.TEMPLATE_DIR, "templates");
        assertThat(product.configuration(), is(new Configuration(config)));
    }

}
