package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.juberblog.JUberblog;
import de.weltraumschaf.juberblog.content.Pages;
import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import de.weltraumschaf.juberblog.BaseTestCase;
import de.weltraumschaf.juberblog.Registry;
import de.weltraumschaf.juberblog.content.PageType;

import java.nio.file.Files;
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
        final JUberblog registry = createRegistry();
        Files.copy(createPath("sites/2014-08-30T15.29.20_Site-One.md"), registry.directories().getSitesData().resolve("2014-08-30T15.29.20_Site-One.md"));
        Files.copy(createPath("sites/2014-09-30T15.29.20_Site-Two.md"), registry.directories().getSitesData().resolve("2014-09-30T15.29.20_Site-Two.md"));
        final PublishTask sut = new PublishTask(registry, PageType.SITE);

        // TODO Verify result.
        final Pages pages = sut.execute();

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.HTML).find(tmp.getRoot().toPath().resolve("public"));
        assertThat(foundFiles.size(), is(2));
        final DataFile expectedOne = new DataFile(tmp.getRoot().toString() + "/public/sites/Site-One.html");
        final DataFile expectedTwo = new DataFile(tmp.getRoot().toString() + "/public/sites/Site-Two.html");
        assertThat(foundFiles, containsInAnyOrder(expectedOne, expectedTwo));
    }

}
