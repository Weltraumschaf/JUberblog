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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link LockFile}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class LockFileTest {

    @Rule
    // CHECKSTYLE:OFF
    public final TemporaryFolder tmp = new TemporaryFolder();
    // CHECKSTYLE:ON
    @Rule
    // CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    // CHECKSTYLE:ON

    private LockFile sut;
    private Path lock;

    @Before
    public void createSut() throws IOException {
        lock = tmp.getRoot().toPath().resolve("lock");
        sut = new LockFile(lock);
    }

    @Test
    public void exists_returnFalseIfNotCreated() {
        assertThat(sut.exists(), is(false));
    }

    @Test
    public void exists_returnTrueIfCreated() throws IOException {
        sut.create();
        assertThat(sut.exists(), is(true));
    }

    @Test
    public void exists_returnFalseIfRemoved() throws IOException {
        sut.create();
        sut.remove();
        assertThat(sut.exists(), is(false));
    }

    @Test
    public void create_fileExistsAfterCall() throws IOException {
        assertThat(Files.exists(lock), is(false));
        sut.create();
        assertThat(Files.exists(lock), is(true));
    }

    @Test
    public void create_throwsExceptionIfAlreadyExists() throws IOException {
        sut.create();
        thrown.expect(IllegalStateException.class);
        sut.create();
    }

    @Test
    public void remove_fileDoesNotExistAfterCall() throws IOException {
        sut.create();
        sut.remove();
        assertThat(Files.exists(lock), is(false));
    }

    @Test
    public void remove_throwsExcpetionIfFileDoesNotExists() throws IOException {
        thrown.expect(IllegalStateException.class);
        sut.remove();
    }

}
