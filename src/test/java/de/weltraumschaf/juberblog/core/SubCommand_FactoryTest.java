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

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.create.CreateSubCommand;
import de.weltraumschaf.juberblog.install.InstallSubCommand;
import de.weltraumschaf.juberblog.publish.PublishSubCommand;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SubCommand.Factory}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SubCommand_FactoryTest {

    private final SubCommand.Factory sut = new SubCommand.Factory();

    @Test
    public void forName_CREATE() {
        assertThat(sut.forName(SubCommand.Name.CREATE, new Options(), mock(IO.class)),
                is(instanceOf(CreateSubCommand.class)));
    }

    @Test
    public void forName_INSTALL() {
        assertThat(sut.forName(SubCommand.Name.INSTALL, new Options(), mock(IO.class)),
                is(instanceOf(InstallSubCommand.class)));
    }

    @Test
    public void forName_PUBLISH() {
        assertThat(sut.forName(SubCommand.Name.PUBLISH, new Options(), mock(IO.class)),
                is(instanceOf(PublishSubCommand.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void forName_UNKNOWN() {
        sut.forName(SubCommand.Name.UNKNOWN, new Options(), mock(IO.class));
    }
}
