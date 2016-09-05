package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.juberblog.BaseTestCase;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link BlogConfiguration}.
 *
 * @author Sven Strittmatter
 */
public class BlogConfigurationTest extends BaseTestCase {

    private static final String DIR_SEP = Constants.DIR_SEP.toString();
    private static final String PACKAGE = "src.main.resources."
            + Constants.SCAFFOLD_PACKAGE.toString() + ".configuration";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private BlogConfiguration sut;

    @Before
    public void createSut() throws IOException {
        final String baseDir = System.getProperty("user.dir");
        final String fileName = baseDir + DIR_SEP
                + PACKAGE.replace(".", DIR_SEP)
                + DIR_SEP + "configuration.sample.properties";
        sut = new BlogConfiguration(fileName);
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withNullAsProperties() throws IOException {
        new BlogConfiguration((Properties) null);
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withNullAsFileName() throws IOException {
        new BlogConfiguration((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withEmptyAFileName() throws IOException {
        new BlogConfiguration("");
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(BlogConfiguration.class)
                .verify();
    }

    @Test
    public void getProperties() {
        assertThat(sut.getTitle(), is(equalTo("Blog Title")));
        assertThat(sut.getDescription(), is(equalTo("Blog Description")));
        assertThat(sut.getBaseUri(), is(equalTo(URI.create("http://uberblog.local/"))));
        assertThat(sut.getLanguage(), is(equalTo("en")));
        assertThat(sut.getDataDir(), is(equalTo("./data")));
        assertThat(sut.getTemplateDir(), is(equalTo("./templates")));
        assertThat(sut.getHtdocs(), is(equalTo("./public")));
    }

    @Test
    public void validate() {
        final Properties config = BlogConfiguration.validate(createProperties());

        assertThat(config.getProperty(BlogConfiguration.DATA_DIR), is("/data"));
        assertThat(config.getProperty(BlogConfiguration.DESCRIPTION), is("Blog Description"));
        assertThat(config.getProperty(BlogConfiguration.ENCODING), is("utf-8"));
        assertThat(config.getProperty(BlogConfiguration.PUBLIC_DIR), is("/public"));
        assertThat(config.getProperty(BlogConfiguration.LANGUAGE), is("en"));
        assertThat(config.getProperty(BlogConfiguration.SITE_URI), is("http://uberblog.local/"));
        assertThat(config.getProperty(BlogConfiguration.TEMPLATE_DIR), is("/templates"));
        assertThat(config.getProperty(BlogConfiguration.TITLE), is("Blog Title"));
    }

    @Test
    public void validate_emptyTitle() {
        final Properties config = createProperties();
        config.remove(BlogConfiguration.TITLE);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The configuration property 'title' must not be empty or missing!");

        BlogConfiguration.validate(config);
    }

    @Test
    public void validate_emptyDescription() {
        final Properties config = createProperties();
        config.remove(BlogConfiguration.DESCRIPTION);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The configuration property 'description' must not be empty or missing!");

        BlogConfiguration.validate(config);
    }

    @Test
    public void validate_emptySiteUrl() {
        final Properties config = createProperties();
        config.remove(BlogConfiguration.SITE_URI);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The configuration property 'siteUrl' must not be empty or missing!");

        BlogConfiguration.validate(config);
    }

    @Test
    public void validate_emptyLanguage() {
        final Properties config = createProperties();
        config.remove(BlogConfiguration.LANGUAGE);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The configuration property 'language' must not be empty or missing!");

        BlogConfiguration.validate(config);
    }

    @Test
    public void validate_emptyDataDir() {
        final Properties config = createProperties();
        config.remove(BlogConfiguration.DATA_DIR);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The configuration property 'dataDirectory' must not be empty or missing!");

        BlogConfiguration.validate(config);
    }

    @Test
    public void validate_emptyTplDir() {
        final Properties config = createProperties();
        config.remove(BlogConfiguration.TEMPLATE_DIR);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The configuration property 'templateDirectory' must not be empty or missing!");

        BlogConfiguration.validate(config);
    }

    @Test
    public void validate_emptyHtdocs() {
        final Properties config = createProperties();
        config.remove(BlogConfiguration.PUBLIC_DIR);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The configuration property 'publicDirectory' must not be empty or missing!");

        BlogConfiguration.validate(config);
    }

    @Test
    public void validate_emptyEncoding() {
        final Properties config = createProperties();
        config.remove(BlogConfiguration.ENCODING);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The configuration property 'encoding' must not be empty or missing!");

        BlogConfiguration.validate(config);
    }

}
