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
import java.nio.file.Paths;
import java.util.Properties;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.mock;

/**
 * Tests for {
 *
 * @lnk JUberblog.Builder}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class JUberblog_BuilderTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final Directories directories = new Directories(
            Paths.get("."),
            Paths.get("."),
            Paths.get("."),
            Paths.get("."),
            Paths.get("."));
    private final Templates templates = new Templates(
            Paths.get("."),
            Paths.get("."),
            Paths.get("."),
            Paths.get("."),
            Paths.get("."),
            Paths.get("."));
    private final Configuration configuration = new Configuration(new Properties());
    private final Options options = new Options();
    private final IO io = mock(IO.class);

    @Test
    public void product() {
        final JUberblog product = JUberblog.Builder.create()
                .directories(directories)
                .templates(templates)
                .configuration(configuration)
                .options(options)
                .io(io)
                .product();

        assertThat(product.configuration(), is(configuration));
        assertThat(product.directories(), is(directories));
        assertThat(product.io(), is(io));
        assertThat(product.options(), is(options));
        assertThat(product.templates(), is(templates));
    }

    @Test
    public void product_imutabilityBycopy() {
        final JUberblog.Builder sut = JUberblog.Builder.create();
        final JUberblog product = sut
                .directories(directories)
                .templates(templates)
                .configuration(configuration)
                .options(options)
                .io(io)
                .product();

        sut.io(mock(IO.class));

        assertThat(product.io(), is(io));
    }

    @Test
    public void product_nothingSet() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Configuration must not be null! Call Builder#configuration(cfg) first.");

        JUberblog.Builder.create().product();
    }

    @Test
    public void product_configMissed() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Configuration must not be null! Call Builder#configuration(cfg) first.");

        JUberblog.Builder.create()
                .directories(directories)
                .templates(templates)
                .options(options)
                .io(io)
                .product();
    }

    @Test
    public void product_directoriesMissed() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Directories must not be null! Call Builder#directories(dirs) first.");

        JUberblog.Builder.create()
                .templates(templates)
                .configuration(configuration)
                .options(options)
                .io(io)
                .product();
    }

    @Test
    public void product_ioMissed() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("I/O must not be null! Call Builder#io(io) first.");

        JUberblog.Builder.create()
                .directories(directories)
                .templates(templates)
                .configuration(configuration)
                .options(options)
                .product();
    }

    @Test
    public void product_optionsMissed() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Options must not be null! Call Builder#options(opt) first.");

        JUberblog.Builder.create()
                .directories(directories)
                .templates(templates)
                .configuration(configuration)
                .io(io)
                .product();
    }

    @Test
    public void product_templatesMissed() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Templates must not be null! Call Builder#templates(tpls) first.");

        JUberblog.Builder.create()
                .directories(directories)
                .configuration(configuration)
                .options(options)
                .io(io)
                .product();
    }
}
