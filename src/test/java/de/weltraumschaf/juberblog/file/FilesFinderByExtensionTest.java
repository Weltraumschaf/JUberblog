package de.weltraumschaf.juberblog.file;

import de.weltraumschaf.juberblog.BaseTestCase;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Collection;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link FilesFinderByExtension}.
 *
 * @author Sven Strittmatter
 */
public class FilesFinderByExtensionTest extends BaseTestCase {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    private final FilesFinderByExtension sut = FilesFinderByExtension.MARKDOWN;

    @Test
    public void findMarkdownFiles() throws URISyntaxException, IOException {
        final Collection<DataFile> foundFiles = sut.find(createPath(""));

        assertThat(foundFiles.size(), is(5));
        assertThat(foundFiles, containsInAnyOrder(
                new DataFile(createPath("posts/2014-05-30T21.29.20_This-is-the-First-Post.md").toString()),
                new DataFile(createPath("posts/2014-06-30T23.25.44_This-is-the-Second-Post.md").toString()),
                new DataFile(createPath("posts/2014-07-28T17.44.13_This-is-the-Third-Post.md").toString()),
                new DataFile(createPath("sites/2014-08-30T15.29.20_Site-One.md").toString()),
                new DataFile(createPath("sites/2014-09-30T15.29.20_Site-Two.md").toString())
        ));
    }

    @Test
    public void find_pathIsNull() throws IOException {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("'directory'");

        sut.find(null);
    }

    @Test
    public void find_pathDoesNotExist() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Given path '/foo/bar/baz' does not exist!");

        sut.find(Paths.get("/foo/bar/baz"));
    }

    @Test
    public void find_pathIsNotDirectory() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("is not a directory");

        sut.find(tmp.newFile().toPath());
    }

}
