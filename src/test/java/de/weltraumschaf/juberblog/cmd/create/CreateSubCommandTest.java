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

package de.weltraumschaf.juberblog.cmd.create;

import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.time.TimeProvider;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link CreateSubCommand}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CreateSubCommandTest {

    @Rule
    //CHECKSTYLE:OFF
    public final TemporaryFolder tmp = new TemporaryFolder();
    //CHECKSTYLE:ON

    private final TimeProvider time = mock(TimeProvider.class);
    private final CreateSubCommand sut = new CreateSubCommand(mock(IO.class));

    @Test(expected = NullPointerException.class)
    public void createFileNameFromTitle_timeIsNull() {
        sut.createFileNameFromTitle("foobar", null);
    }

    @Test(expected = NullPointerException.class)
    public void createFileNameFromTitle_titleIsNull() {
        sut.createFileNameFromTitle(null, time);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createFileNameFromTitle_titleIsEmpty() {
        sut.createFileNameFromTitle("", time);
    }

    @Test
    public void createFileNameFromTitle() {
        when(time.nowAsString()).thenReturn("1234");

        assertThat(sut.createFileNameFromTitle("This is the title", time),
                is(equalTo("1234_This_is_the_title.md")));
    }

    @Test(expected = NullPointerException.class)
    public void createPath_baseDirIsNull() {
        sut.createPath(null, "foobar", time);
    }

    @Test
    public void createPath() {
        when(time.nowAsString()).thenReturn("1234");

        assertThat(sut.createPath(Paths.get("a"), "This is the title", time),
                is(equalTo(Paths.get("a", "1234_This_is_the_title.md"))));
    }

    @Test
    public void writeFile() throws IOException {
        final Path fileName = tmp.newFile("the-file").toPath();
        final String content = "The content";

        sut.writeFile(fileName, content);

        final Collection<String> readedLines = Files.readAllLines(fileName, Charset.forName(Constants.DEFAULT_ENCODING.toString()));
        assertThat(readedLines, both(hasSize(1)).and(contains((Object)content)));
    }
}
