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
package de.weltraumschaf.juberblog.app;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.BaseTestCase;
import de.weltraumschaf.juberblog.cmd.SubCommand;
import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.juberblog.app.App.Factory;
import de.weltraumschaf.juberblog.app.App.FactoryImpl;
import de.weltraumschaf.juberblog.core.Configuration;
import de.weltraumschaf.juberblog.core.Directories;
import de.weltraumschaf.juberblog.core.Templates;
import de.weltraumschaf.juberblog.create.CreateSubCommand;
import de.weltraumschaf.juberblog.install.InstallSubCommand;
import de.weltraumschaf.juberblog.publish.PublishSubCommand;
import java.nio.file.Paths;
import java.util.Properties;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link FactoryImpl}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class FactoryTest extends BaseTestCase {

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
    private final JUberblog registry = JUberblog.Builder.create()
                .directories(directories)
                .templates(templates)
                .configuration(new Configuration(createProperties()))
                .options(new Options())
                .io(mock(IO.class))
                .product();

    private final Factory sut = new FactoryImpl();

    @Test
    public void forName_CREATE() {
        assertThat(sut.forName(SubCommand.Name.CREATE, registry),
                is(instanceOf(CreateSubCommand.class)));
    }

    @Test
    public void forName_INSTALL() {
        assertThat(sut.forName(SubCommand.Name.INSTALL, registry),
                is(instanceOf(InstallSubCommand.class)));
    }

    @Test
    public void forName_PUBLISH() {
        assertThat(sut.forName(SubCommand.Name.PUBLISH, registry),
                is(instanceOf(PublishSubCommand.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void forName_UNKNOWN() {
        sut.forName(SubCommand.Name.UNKNOWN, registry);
    }
}
