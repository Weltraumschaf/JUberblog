package de.weltraumschaf.juberblog;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.core.BlogConfiguration;
import de.weltraumschaf.juberblog.core.BlogConfigurationKeys;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Templates;
import de.weltraumschaf.juberblog.options.Options;

import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Properties;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        when(io.getStdout()).thenReturn(mock(PrintStream.class));
        when(io.getStderr()).thenReturn(mock(PrintStream.class));

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
        config.setProperty(BlogConfigurationKeys.TITLE.getKey(), "Blog Title");
        config.setProperty(BlogConfigurationKeys.DESCRIPTION.getKey(), "Blog Description");
        config.setProperty(BlogConfigurationKeys.SITE_URI.getKey(), "http://uberblog.local/");
        config.setProperty(BlogConfigurationKeys.ENCODING.getKey(), "utf-8");
        config.setProperty(BlogConfigurationKeys.PUBLIC_DIR.getKey(), "public");
        config.setProperty(BlogConfigurationKeys.LANGUAGE.getKey(), "en");
        config.setProperty(BlogConfigurationKeys.DATA_DIR.getKey(), "data");
        config.setProperty(BlogConfigurationKeys.TEMPLATE_DIR.getKey(), "templates");
        assertThat(product.configuration(), is(new BlogConfiguration(config)));
    }

}
