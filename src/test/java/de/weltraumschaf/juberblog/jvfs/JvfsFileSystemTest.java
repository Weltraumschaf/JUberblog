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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Ignore;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class JvfsFileSystemTest {

    @Before
    public void registerDefaultProvider() {
        JvfsFileSystems.registerAsDefault();
    }

    @After
    public void unregisterDefaultProvider() {
        JvfsFileSystems.unregisterAsDefault();
    }

    @Test
    public void testSomeMethod() throws URISyntaxException, IOException {
        final Path path = Paths.get(URI.create("file:///tmp/foobar"));
        Files.createFile(path);
        final OutputStream out = Files.newOutputStream(path);
        IOUtils.write("hello world", out);
        IOUtils.closeQuietly(out);
//        final InputStream in = Files.newInputStream(path);
//        assertThat(IOUtils.toString(in), is("hello world"));
    }


}
