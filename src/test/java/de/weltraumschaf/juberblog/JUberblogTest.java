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
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
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
                location.resolve("data/posts"),
                location.resolve("data/sites"),
                location.resolve("public"),
                location.resolve("public/posts"),
                location.resolve("public/sites"))));
        assertThat(product.templates(), is(new Templates(
                location.resolve("templates/layout.ftl"),
                location.resolve("templates/post.ftl"),
                location.resolve("templates/site.ftl"),
                location.resolve("templates/feed.ftl"),
                location.resolve("templates/index.ftl"),
                location.resolve("templates/site_map.ftl"))));
        final Properties config = new Properties();
        config.setProperty("title", "Blog Title");
        config.setProperty("description", "Blog Description");
        config.setProperty("siteUrl", "http://uberblog.local/");
        config.setProperty("encoding", "UTF-8");
        config.setProperty("htdocs", "public");
        config.setProperty("language", "en");
        config.setProperty("dataDir", "data");
        config.setProperty("tplDir", "templates");
        assertThat(product.configuration(), is(new Configuration(config)));
    }

}
