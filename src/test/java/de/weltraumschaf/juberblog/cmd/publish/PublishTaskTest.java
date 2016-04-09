package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import de.weltraumschaf.juberblog.BaseTestCase;
import de.weltraumschaf.juberblog.Registry;
import de.weltraumschaf.juberblog.core.PageType;
import java.util.Collection;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link PublishTask}.
 *
 * @author Sven Strittmatter
 */
public class PublishTaskTest extends BaseTestCase {

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructWithNullAsRegistryThrowsException() {
        new PublishTask(null, PageType.POST);
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructWithNullAsTypeThrowsException() {
        new PublishTask(mock(Registry.class), null);
    }

    @Test
    public void execute() throws Exception {
        final PublishTask sut = new PublishTask(createRegistry(true), PageType.SITE);

        sut.execute();

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.HTML).find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(2));
        final DataFile expectedOne = new DataFile(tmp.getRoot().toString() + "/sites/Site-One.html");
        final DataFile expectedTwo = new DataFile(tmp.getRoot().toString() + "/sites/Site-Two.html");
        assertThat(foundFiles, containsInAnyOrder(expectedOne, expectedTwo));
    }

}
