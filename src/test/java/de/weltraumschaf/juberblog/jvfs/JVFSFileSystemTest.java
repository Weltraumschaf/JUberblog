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

package de.weltraumschaf.juberblog.jvfs;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThat;
/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class JVFSFileSystemTest {

    public static final String PROP_NAME = "java.nio.file.spi.DefaultFileSystemProvider";

    @Before
    public void registerDefaultProvider() {
        System.setProperty(PROP_NAME, "de.weltraumschaf.juberblog.jvfs.JVFSFileSystemProvider");
    }

    @After
    public void unregisterDefaultProvider() {
        System.setProperty(PROP_NAME, "");
    }

    @Test
    public void testSomeMethod() throws URISyntaxException, IOException {
//        FileSystemProvider prov = JVFSFileSystems.newProvider();
//        FileSystem fs = prov.getFileSystem(new URI("file:///"));
        final Path path = Paths.get(URI.create("file:///tmp/foobar"));
        Files.createFile(path);
    }


}
