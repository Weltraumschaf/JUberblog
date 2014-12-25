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

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Before;

/**
 * Tests for {@link Configuration}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class ConfigurationTest {

    private static final String DIR_SEP = Constants.DIR_SEP.toString();
    private static final String PACKAGE = "src.main.resources."
            + Constants.SCAFFOLD_PACKAGE.toString() + ".configuration";

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
        assertThat(sut.getBaseUri(), is(equalTo("http://uberblog.local/")));
        assertThat(sut.getLanguage(), is(equalTo("en")));
        assertThat(sut.getDataDir(), is(equalTo("./data")));
        assertThat(sut.getTemplateDir(), is(equalTo("./templates")));
        assertThat(sut.getHtdocs(), is(equalTo("./public")));
    }

}
