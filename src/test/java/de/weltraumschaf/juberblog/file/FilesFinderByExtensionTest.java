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
package de.weltraumschaf.juberblog.file;

import de.weltraumschaf.juberblog.JUberblogTestCase;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Ignore;

/**
 * Tests for {@link FilesFinderByExtension}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class FilesFinderByExtensionTest extends JUberblogTestCase {

    @Test
    public void findMarkdownFiles() throws URISyntaxException, IOException {
        final FilesFinderByExtension sut = FilesFinderByExtension.MARKDOWN;

        final Collection<DataFile> foundFiles = sut.find(createPath("posts"));

        assertThat(foundFiles.size(), is(3));
        assertThat(foundFiles, containsInAnyOrder(
                new DataFile(createPath("posts/2014-05-30T21.29.20_This-is-the-First-Post.md").toString()),
                new DataFile(createPath("posts/2014-06-30T23.25.44_This-is-the-Second-Post.md").toString()),
                new DataFile(createPath("posts/2014-07-28T17.44.13_This-is-the-Third-Post.md").toString())
        ));
    }

    @Test
    @Ignore("TODO Implement FilesFinderByExtensionTest#find_pathIsNull()")
    public void find_pathIsNull() {
    }

    @Test
    @Ignore("TODO Implement FilesFinderByExtensionTest#find_pathDoesNotExist()")
    public void find_pathDoesNotExist() {
    }

    @Test
    @Ignore("TODO Implement FilesFinderByExtensionTest#find_pathIsNotDirecotry()")
    public void find_pathIsNotDirecotry() {
    }

}
