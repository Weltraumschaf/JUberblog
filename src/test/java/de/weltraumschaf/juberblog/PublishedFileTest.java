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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PublishedFileTest {

    @Rule
    //CHECKSTYLE:OFF
    public final TemporaryFolder tmp = new TemporaryFolder();
    //CHECKSTYLE:On

    @Test
    public void getUri() throws IOException {
        final File file = tmp.newFile("snafu");
        final PublishedFile sut = new PublishedFile("http://www.foobar.com/", file);
        assertThat(sut.getUri(), is(equalTo("http://www.foobar.com/snafu")));
    }

    @Test
    public void getModificationTime() throws IOException, InterruptedException {
        final File file = tmp.newFile("snafu");
        write(file, "hello world!");
        final PublishedFile sut = new PublishedFile("http://www.foobar.com/", file);
        final long firstModTime = sut.getModificationTime();
        assertThat(firstModTime, is(file.lastModified()));

        Thread.sleep(1000);
        write(file, "SNAFU!");

        assertThat(sut.getModificationTime(), is(file.lastModified()));
        assertThat(sut.getModificationTime(), is(greaterThan(firstModTime)));
    }

    private void write(final File file, final String content) throws IOException {
        final FileWriter writer = new FileWriter(file);
        IOUtils.write(content, writer);
        IOUtils.closeQuietly(writer);
    }

}
