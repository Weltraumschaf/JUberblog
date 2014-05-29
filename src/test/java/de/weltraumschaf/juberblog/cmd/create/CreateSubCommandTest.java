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

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.application.IO;
import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.cmd.install.Scaffold;
import de.weltraumschaf.juberblog.cmd.install.TestingSourceJarProvider;
import de.weltraumschaf.juberblog.opt.CreateOptions;
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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

    @Rule
    //CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    private final TimeProvider time = mock(TimeProvider.class);
    private final CreateSubCommand sut = new CreateSubCommand(mock(IO.class));

    @Before
    public void injectTimeProvider() {
        sut.setTime(time);
    }

    @Test(expected = NullPointerException.class)
    public void createFileNameFromTitle_titleIsNull() {
        sut.createFileNameFromTitle(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createFileNameFromTitle_titleIsEmpty() {
        sut.createFileNameFromTitle("");
    }

    @Test
    public void createFileNameFromTitle() {
        when(time.nowAsString()).thenReturn("1234");

        assertThat(sut.createFileNameFromTitle("This is the title"),
                is(equalTo("1234_This_is_the_title.md")));
    }

    @Test(expected = NullPointerException.class)
    public void createPath_baseDirIsNull() {
        sut.createPath(null, "foobar");
    }

    @Test
    public void createPath() {
        when(time.nowAsString()).thenReturn("1234");

        assertThat(sut.createPath(Paths.get("a"), "This is the title"),
                is(equalTo(Paths.get("a", "1234_This_is_the_title.md"))));
    }

    @Test
    public void writeFile() throws IOException {
        final Path fileName = tmp.newFile("the-file").toPath();
        final String content = "The content";

        sut.writeFile(fileName, content);

        final Collection<String> readedLines = Files.readAllLines(fileName, Charset.forName(Constants.DEFAULT_ENCODING.toString()));
        assertThat(readedLines, both(hasSize(1)).and(contains((Object) content)));
    }

    @Test
    public void execute_noConfigFileGiven() throws ApplicationException {
        // TODO test exit code.
        thrown.expect(ApplicationException.class);
        thrown.expectMessage("No config file argument given!");

        sut.setOptions(new CreateOptions());
        sut.execute();
    }

    @Test
    public void execute_unreadableConfigFileGiven() throws ApplicationException {
        // TODO test exit code.
        thrown.expect(ApplicationException.class);
        thrown.expectMessage("Can't read config file '/foo/bar/baz'!");

        sut.setOptions(new CreateOptions(false, false, "/foo/bar/baz", false, false, "title"));
        sut.execute();
    }

    @Test
    public void execute_notitleGiven() throws ApplicationException, IOException {
        createScaffold();
        // TODO test exit code.
        thrown.expect(ApplicationException.class);
        thrown.expectMessage("No title arguemnt given!");

        sut.setOptions(
            new CreateOptions(
                false,
                false,
                tmp.getRoot().getAbsolutePath() + "/configuration/configuration.sample.properties",
                false,
                false,
                ""));
        sut.execute();
    }

    @Test
    public void execute_createPost() throws ApplicationException, IOException {
        createScaffold();
        when(time.nowAsString()).thenReturn("1234");
        sut.setOptions(
            new CreateOptions(
                false,
                false,
                tmp.getRoot().getAbsolutePath() + "/configuration/configuration.sample.properties",
                false,
                false,
                "title"));

        sut.execute();

        final Path post = tmp.getRoot().toPath().resolve("data/posts/1234_title.md");
        assertThat(Files.exists(post), is(true));
        final String content = new String(Files.readAllBytes(post), "utf-8");
        assertThat(content, is(equalTo("<?juberblog\n    Navi:\n    Description:\n    Keywords:\n?>\n## title")));
    }

    @Test
    public void execute_createPostDraft() throws ApplicationException, IOException {
        createScaffold();
        when(time.nowAsString()).thenReturn("1234");
        sut.setOptions(
            new CreateOptions(
                false,
                false,
                tmp.getRoot().getAbsolutePath() + "/configuration/configuration.sample.properties",
                true,
                false,
                "title"));

        sut.execute();

        final Path post = tmp.getRoot().toPath().resolve("data/drafts/posts/1234_title.md");
        assertThat(Files.exists(post), is(true));
        final String content = new String(Files.readAllBytes(post), "utf-8");
        assertThat(content, is(equalTo("<?juberblog\n    Navi:\n    Description:\n    Keywords:\n?>\n## title")));
    }

    private void createScaffold() throws IOException {
        final Scaffold scaffold = new Scaffold(mock(IO.class));
        scaffold.setSrcJar(TestingSourceJarProvider.newProvider());
        scaffold.copyFiles(tmp.getRoot());

        final Path config = tmp.getRoot().toPath().resolve("configuration/configuration.sample.properties");
        final String root = tmp.getRoot().getAbsolutePath();
        final String content = new String(Files.readAllBytes(config), "utf-8")
                .replace("/data", root + "/data")
                .replace("/templates", root + "/templates")
                .replace("/public", root + "/public");
        Files.write(config, content.getBytes("utf-8"));
    }
}
