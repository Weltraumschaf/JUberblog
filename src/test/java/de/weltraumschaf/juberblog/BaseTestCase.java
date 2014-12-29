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

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.app.Options;
import de.weltraumschaf.juberblog.core.Configuration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Templates;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.junit.rules.TemporaryFolder;

/**
 * Some helper stuff.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
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

    protected final  Directories createDirs(final TemporaryFolder tmp) throws IOException, URISyntaxException {
        return new Directories(
                createPath("posts"),
                createPath("sites"),
                tmp.getRoot().toPath(),
                tmp.newFolder("posts").toPath(),
                tmp.newFolder("sites").toPath());
    }

    protected final Templates createTemplates() throws URISyntaxException {
        return new Templates(
                createPath(SCAFOLD_PACKAGE_PREFIX + "layout.ftl"),
                createPath(SCAFOLD_PACKAGE_PREFIX + "post.ftl"),
                createPath(SCAFOLD_PACKAGE_PREFIX + "site.ftl"),
                createPath(SCAFOLD_PACKAGE_PREFIX + "feed.ftl"),
                createPath(SCAFOLD_PACKAGE_PREFIX + "index.ftl"),
                createPath(SCAFOLD_PACKAGE_PREFIX + "site_map.ftl"));
    }

    protected final Configuration createConfig() {
        return new Configuration(createProperties());
    }

    protected final Properties createProperties() {
        final Properties config = new Properties();
        config.setProperty(Configuration.DATA_DIR, "/data");
        config.setProperty(Configuration.DESCRIPTION, "Blog Description");
        config.setProperty(Configuration.ENCODING, "utf-8");
        config.setProperty(Configuration.PUBLIC_DIR, "/htdocs");
        config.setProperty(Configuration.LANGUAGE, "en");
        config.setProperty(Configuration.SITE_URI, "http://www.myblog.com/");
        config.setProperty(Configuration.TEMPLATE_DIR, "/templates");
        config.setProperty(Configuration.TITLE, "Blog Title");

        return config;
    }

    protected final JUberblog createRegistry(final TemporaryFolder tmp, final Options options, final IO io) throws URISyntaxException, IOException {
        return JUberblog.Builder.create()
                .directories(createDirs(tmp))
                .templates(createTemplates())
                .configuration(createConfig())
                .options(options)
                .io(io)
                .product();
    }
}
