/*
 *  LICENSE
 *
 * "THE BEER-WARE LICENSE" (Revision 43):
 * "Sven Strittmatter" <weltraumschaf@googlemail.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it,
 * you can buy me a non alcohol-free beer in return.
 *
 * Copyright (C) 2012 "Sven Strittmatter" <weltraumschaf@googlemail.com>
 */
package de.weltraumschaf.juberblog.core;

import de.weltraumschaf.juberblog.BaseTestCase;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link Configuration}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class ConfigurationTest extends BaseTestCase {

    private static final String DIR_SEP = Constants.DIR_SEP.toString();
    private static final String PACKAGE = "src.main.resources."
            + Constants.SCAFFOLD_PACKAGE.toString() + ".configuration";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private Configuration sut;

    @Before
    public void createSut() throws IOException {
        final String baseDir = System.getProperty("user.dir");
        final String fileName = baseDir + DIR_SEP
                + PACKAGE.replace(".", DIR_SEP)
                + DIR_SEP + "configuration.sample.properties";
        sut = new Configuration(fileName);
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
        final Properties propes = Configuration.validate(createProperties());

        assertThat(propes.getProperty(Configuration.DATA_DIR), is("/data"));
        assertThat(propes.getProperty(Configuration.DESCRIPTION), is("Blog Description"));
        assertThat(propes.getProperty(Configuration.ENCODING), is("utf-8"));
        assertThat(propes.getProperty(Configuration.HTDOCS), is("/htdocs"));
        assertThat(propes.getProperty(Configuration.LANGUAGE), is("en"));
        assertThat(propes.getProperty(Configuration.SITE_URI), is("http://www.myblog.com/"));
        assertThat(propes.getProperty(Configuration.TEMPLATE_DIR), is("/templates"));
        assertThat(propes.getProperty(Configuration.TITLE), is("Blog Title"));
    }

    @Test
    public void validate_emptyTitle() {
        final Properties config = createProperties();
        config.remove(Configuration.TITLE);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The configuration property 'title' must not be empty or missing!");

        Configuration.validate(config);
    }

    @Test
    public void validate_emptyDescription() {
        final Properties config = createProperties();
        config.remove(Configuration.DESCRIPTION);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The configuration property 'description' must not be empty or missing!");

        Configuration.validate(config);
    }

    @Test
    public void validate_emptySiteUrl() {
        final Properties config = createProperties();
        config.remove(Configuration.SITE_URI);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The configuration property 'siteUrl' must not be empty or missing!");

        Configuration.validate(config);
    }

    @Test
    public void validate_emptyLanguage() {
        final Properties config = createProperties();
        config.remove(Configuration.LANGUAGE);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The configuration property 'language' must not be empty or missing!");

        Configuration.validate(config);
    }

    @Test
    public void validate_emptyDataDir() {
        final Properties config = createProperties();
        config.remove(Configuration.DATA_DIR);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The configuration property 'dataDirectory' must not be empty or missing!");

        Configuration.validate(config);
    }

    @Test
    public void validate_emptyTplDir() {
        final Properties config = createProperties();
        config.remove(Configuration.TEMPLATE_DIR);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The configuration property 'temlateDirectory' must not be empty or missing!");

        final Properties propes = Configuration.validate(config);
    }

    @Test
    public void validate_emptyHtdocs() {
        final Properties config = createProperties();
        config.remove(Configuration.HTDOCS);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The configuration property 'publicDirectory' must not be empty or missing!");

        Configuration.validate(config);
    }

    @Test
    public void validate_emptyEncoding() {
        final Properties config = createProperties();
        config.remove(Configuration.ENCODING);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The configuration property 'encoding' must not be empty or missing!");

        Configuration.validate(config);
    }

}
