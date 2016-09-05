package de.weltraumschaf.juberblog;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.core.BlogConfiguration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Templates;
import de.weltraumschaf.juberblog.options.Options;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Properties;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link JUberblog}.
 *
 * @author Sven Strittmatter
 */
public class JUberblogTest extends BaseTestCase {

    @Test
    public void generateWithDefaultConfig() throws URISyntaxException, ApplicationException {
        final Options options = new Options();
        final IO io = mock(IO.class);

        final JUberblog product = JUberblog.generateWithDefaultConfig(options, io);

        assertThat(product.options(), is(options));
        assertThat(product.io(), is(io));
        assertThat(product.directories(), is(new Directories(
            Paths.get("data"),
            Paths.get("public"))));
        assertThat(product.templates(), is(new Templates(
            Paths.get("templates/layout.ftl"),
            Paths.get("templates/post.ftl"),
            Paths.get("templates/site.ftl"),
            Paths.get("templates/feed.ftl"),
            Paths.get("templates/index.ftl"),
            Paths.get("templates/site_map.ftl"),
            Paths.get("templates/create/post_or_site.md.ftl"))));
        final Properties config = new Properties();
        config.setProperty(BlogConfiguration.TITLE, "Blog Title");
        config.setProperty(BlogConfiguration.DESCRIPTION, "Blog Description");
        config.setProperty(BlogConfiguration.SITE_URI, "http://uberblog.local/");
        config.setProperty(BlogConfiguration.ENCODING, "utf-8");
        config.setProperty(BlogConfiguration.PUBLIC_DIR, "public");
        config.setProperty(BlogConfiguration.LANGUAGE, "en");
        config.setProperty(BlogConfiguration.DATA_DIR, "data");
        config.setProperty(BlogConfiguration.TEMPLATE_DIR, "templates");
        assertThat(product.configuration(), is(new BlogConfiguration(config)));
    }

}
