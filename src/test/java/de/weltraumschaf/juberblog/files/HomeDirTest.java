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

import de.weltraumschaf.juberblog.Environments.Env;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;

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

    @Mock
    private Env env;
    private HomeDir sut;

    @Before
    public void createSut() {
        sut = new HomeDir(env);
    }

    @Test
    public void createIfNotExists_dirExistsAfterCall() {
    }

    @Test
    public void createIfNotExists_doesNothingIfDirAlreadyExists() {
    }

    @Test
    public void createIfNotExists_throwsExceptionIfExistsButIsNotReadable() {
    }

    @Test
    public void createIfNotExists_throwsExcpetionIfExistsButIsNotWritable() {
    }

    @Test
    public void createIfNotExists_throwsExceptionIfExistsButIsNotADirecotry() {
    }

}
