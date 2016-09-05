
package de.weltraumschaf.juberblog.cmd.create;

import de.weltraumschaf.commons.application.ApplicationException;
import de.weltraumschaf.commons.system.ExitCode;
import de.weltraumschaf.juberblog.core.ExitCodeImpl;
import de.weltraumschaf.juberblog.options.Options;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Ignore;
import de.weltraumschaf.juberblog.BaseTestCase;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Tests for {@link CreateSubCommand}.
 */
public class CreateSubCommandTest extends BaseTestCase {

    private final TimeProvider time = mock(TimeProvider.class);
    private CreateSubCommand sut;

    @Before
    public void prepareTimeProvider() {
        when(time.nowAsString()).thenReturn("now");
    }

    private CreateSubCommand createSut(final String... args) throws URISyntaxException, IOException {
        final Options opt = new Options();
        opt.parse(args);
        final CreateSubCommand sut = new CreateSubCommand(createRegistry(opt));
        sut.setTime(time);
        return sut;
    }

    @Test
    public void validateArguments_throwsExceptionIfNoTitleGiven() throws IOException, URISyntaxException {
        try {
            createSut().validateArguments();
        } catch (final ApplicationException ex) {
            assertThat(ex.getExitCode(), Matchers.<ExitCode>is(ExitCodeImpl.TOO_FEW_ARGUMENTS));
            assertThat(ex.getMessage(), is("No title argument given!"));
        }
    }

    @Test(expected = NullPointerException.class)
    public void createFileNameFromTitle_titleMustNotBeNull() throws IOException, URISyntaxException {
        createSut().createFileNameFromTitle(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createFileNameFromTitle_titleMustNotBeEmpty() throws IOException, URISyntaxException {
        createSut().createFileNameFromTitle("");
    }

    @Test
    public void createFileNameFromTitle() throws IOException, URISyntaxException {
        final CreateSubCommand sut = createSut();

        assertThat(sut.createFileNameFromTitle("foo bar/baz:snafu"), is("now_foo_bar_baz_snafu.md"));
    }

    @Test(expected = NullPointerException.class)
    public void createPath_baseDirMustNotBeNull() throws IOException, URISyntaxException {
        createSut().createPath(null, "snafu");
    }

    @Test(expected = NullPointerException.class)
    public void createPath_titleMustNotBeNull() throws IOException, URISyntaxException {
        createSut().createPath(mock(Path.class), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createPath_titleMustNotBeEmpty() throws IOException, URISyntaxException {
        createSut().createPath(mock(Path.class), "");
    }

    @Test
    public void createPath() throws IOException, URISyntaxException {
        final CreateSubCommand sut = createSut();
        final Path baseDir = tmp.getRoot().toPath();

        assertThat(sut.createPath(baseDir, "foo"), is(baseDir.resolve("now_foo.md")));
    }

    @Test(expected = NullPointerException.class)
    public void writeFile_fileNameMustNotBeNull() throws IOException, URISyntaxException {
        createSut().writeFile(null, "foo");
    }

    @Test(expected = NullPointerException.class)
    public void writeFile_contentMustNotBeNull() throws IOException, URISyntaxException {
        createSut().writeFile(mock(Path.class), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeFile_contentMustNotBeEmpty() throws IOException, URISyntaxException {
        createSut().writeFile(mock(Path.class), "");
    }

    @Test
    public void writeFile() throws IOException, URISyntaxException {
        final Path file = tmp.newFile().toPath();

        createSut().writeFile(file, "foo");

        assertThat(Files.readAllBytes(file), is("foo".getBytes()));
    }

    @Test
    public void createPost_draft() throws IOException, URISyntaxException {
        final CreateSubCommand sut = createSut("create", "-t", "title", "-d", "-c", "config");

        sut.createPost("foo");

        final Path post = sut.directories().getPostsDraftData().resolve(sut.createFileNameFromTitle("title"));
        assertThat(Files.exists(post), is(true));
        assertThat(Files.readAllBytes(post), is("foo".getBytes()));
    }

    @Test
    @Ignore
    public void createPost() {
    }

    @Test
    @Ignore
    public void createSite_draft() {
    }

    @Test
    @Ignore
    public void createSite() {
    }

    @Test(expected = NullPointerException.class)
    public void setTime_doesNotAcceptNull() throws IOException, URISyntaxException {
        createSut().setTime(null);
    }
}