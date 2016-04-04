package de.weltraumschaf.juberblog.install;

import de.weltraumschaf.commons.application.IO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Tests for {@link CopyDirectoryVisitor}.
 *
 * @author Sven Strittmatter
 */
public class CopyDirectoryVisitorTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    @Rule
    public final TemporaryFolder source = new TemporaryFolder();
    @Rule
    public final TemporaryFolder target = new TemporaryFolder();

    private final IO io = mock(IO.class);

    private CopyDirectoryVisitor createVerboseSut(final InstallationType strategy) {
        return createSut(strategy, true);
    }

    private CopyDirectoryVisitor createNotVerboseSut(final InstallationType strategy) {
        return createSut(strategy, false);
    }

    private CopyDirectoryVisitor createSut(final InstallationType strategy, final boolean verbose) {
        return new CopyDirectoryVisitor(
                target.getRoot().toPath(),
                source.getRoot().getAbsolutePath() + "/",
                io,
                verbose,
                strategy);
    }

    private void createSource() throws IOException {
        source.newFile("foo");
        source.newFile("bar");
        new File(source.newFolder("baz"), "snafu").createNewFile();
    }

    private void createTarget() throws IOException {
        target.newFile("foo");
        new File(target.newFolder("baz"), "snafu").createNewFile();
    }

    @Test
    public void baseName() {
        final CopyDirectoryVisitor sut = createNotVerboseSut(InstallationType.FRESH);

        assertThat(sut.baseName(Paths.get("a/file/name")), is("a/file/name"));
        assertThat(sut.baseName(Paths.get(source.getRoot().getAbsolutePath() + "/a/file/name")), is("a/file/name"));
    }

    @Test
    public void baseName_nullInput() {
        thrown.expect(NullPointerException.class);

        createNotVerboseSut(InstallationType.FRESH).baseName(null);
    }

    @Test
    public void println_verbose() {
        final CopyDirectoryVisitor sut = createVerboseSut(InstallationType.FRESH);

        sut.println("msg");

        verify(io, times(1)).println("msg");
    }

    @Test
    public void println_notVerbose() {
        final CopyDirectoryVisitor sut = createNotVerboseSut(InstallationType.FRESH);

        sut.println("msg");

        verify(io, never()).println(anyString());
        verifyNoMoreInteractions(io);
    }

    @Test
    public void walkFileTree_fresh() throws IOException {
        createSource();

        Files.walkFileTree(
                source.getRoot().toPath(),
                createNotVerboseSut(InstallationType.FRESH));

        final Collection<File> installedFiles = FileUtils.listFiles(target.getRoot(), null, true);
        assertThat(installedFiles, hasSize(3));
        assertThat(
                installedFiles,
                containsInAnyOrder(
                        new File(target.getRoot(), "foo"),
                        new File(target.getRoot(), "bar"),
                        new File(new File(target.getRoot(), "baz"), "snafu")));
    }

    @Test
    public void walkFileTree_overwrite() throws IOException {
        createSource();
        createTarget();

        Files.walkFileTree(
                source.getRoot().toPath(),
                createNotVerboseSut(InstallationType.OVERWRITE));

        final Collection<File> installedFiles = FileUtils.listFiles(target.getRoot(), null, true);
        assertThat(installedFiles, hasSize(3));
        assertThat(
                installedFiles,
                containsInAnyOrder(
                        new File(target.getRoot(), "foo"),
                        new File(target.getRoot(), "bar"),
                        new File(new File(target.getRoot(), "baz"), "snafu")));
    }

    @Test
    public void walkFileTree_backup() throws IOException {
        createSource();
        createTarget();

        Files.walkFileTree(
                source.getRoot().toPath(),
                createNotVerboseSut(InstallationType.BACKUP));

        final Collection<File> installedFiles = FileUtils.listFiles(target.getRoot(), null, true);
        assertThat(installedFiles, hasSize(5));
        assertThat(
                installedFiles,
                containsInAnyOrder(
                        new File(target.getRoot(), "foo"),
                        new File(target.getRoot(), "foo.bak"),
                        new File(target.getRoot(), "bar"),
                        new File(new File(target.getRoot(), "baz"), "snafu"),
                        new File(new File(target.getRoot(), "baz"), "snafu.bak")));
    }

}
