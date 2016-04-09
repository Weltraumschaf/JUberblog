package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import de.weltraumschaf.juberblog.BaseTestCase;
import de.weltraumschaf.juberblog.core.PageType;
import de.weltraumschaf.juberblog.core.Verbose;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.Collection;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link PublishTask}.
 *
 * @author Sven Strittmatter
 */
public class PublishTaskTest extends BaseTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    private PublishTask.Config createTaskConfig() throws URISyntaxException, IOException {
        return new PublishTask.Config(
            createTemplates(),
            createDirs(tmp),
            createConfig(),
            PageType.SITE,
            createVersion());
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructWithNullThrowsException() {
        new PublishTask(null, new Verbose(false, mock(PrintStream.class)));
    }

    @Test
    public void execute() throws Exception {
        final PublishTask sut = new PublishTask(createTaskConfig(), new Verbose(false, mock(PrintStream.class)));

        sut.execute();

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.HTML).find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(2));
        final DataFile expectedOne = new DataFile(tmp.getRoot().toString() + "/sites/Site-One.html");
        final DataFile expectedTwo = new DataFile(tmp.getRoot().toString() + "/sites/Site-Two.html");
        assertThat(foundFiles, containsInAnyOrder(expectedOne, expectedTwo));
    }

}
