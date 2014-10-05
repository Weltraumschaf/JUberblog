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
package de.weltraumschaf.juberblog.files;

import de.weltraumschaf.commons.system.Environments.Env;
import de.weltraumschaf.juberblog.Constants;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link HomeDir}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class HomeDirTest {

    @Rule
    // CHECKSTYLE:OFF
    public final TemporaryFolder tmp = new TemporaryFolder();
    // CHECKSTYLE:ON
    @Rule
    // CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    // CHECKSTYLE:ON

    private final Env env = mock(Env.class);
    private HomeDir sut;
    private Path home;

    @Before
    public void createSut() {
        sut = new HomeDir(env);
    }

    @Before
    public void defineMockBehaviour() {
        home = tmp.getRoot().toPath().resolve(".juberblog");
        when(env.get(Constants.ENVIRONMENT_VARIABLE_HOME.toString())).thenReturn(home.toString());
    }

    @Test
    public void createIfNotExists_dirExistsAfterCall() throws IOException {
        assertThat(Files.exists(home), is(false));

        sut.createIfNotExists();

        assertThat(Files.exists(home), is(true));
    }

    @Test
    public void createIfNotExists_doesNothingIfDirAlreadyExists() throws IOException {
        Files.createDirectory(home);
        assertThat(Files.exists(home), is(true));

        sut.createIfNotExists();

        assertThat(Files.exists(home), is(true));
    }

    @Test
    public void createIfNotExists_throwsExceptionIfExistsButIsNotReadable() throws IOException {
        Files.createFile(home,
            PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("---------")));

        thrown.expect(RuntimeException.class);
        thrown.expectMessage(String.format("Home direcotry '%s' is not readable!", home.toString()));

        sut.createIfNotExists();
    }

    @Test
    public void createIfNotExists_throwsExcpetionIfExistsButIsNotWritable() throws IOException {
        Files.createFile(home,
            PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("r--r--r--")));

        thrown.expect(RuntimeException.class);
        thrown.expectMessage(String.format("Home direcotry '%s' is not writeable!", home.toString()));

        sut.createIfNotExists();
    }

    @Test
    public void createIfNotExists_throwsExceptionIfExistsButIsNotADirecotry() throws IOException {
        Files.createFile(home);

        thrown.expect(RuntimeException.class);
        thrown.expectMessage(String.format("Home path '%s' is not a direcotry!", home.toString()));

        sut.createIfNotExists();
    }

}
