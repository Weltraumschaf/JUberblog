package integrationtests;

import de.weltraumschaf.juberblog.IntegrationTestCase;
import de.weltraumschaf.juberblog.app.App;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests the installation of a blog.
 *
 * @author Sven Strittmatter
 */
public class InstallTest extends IntegrationTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test
    @Ignore
    public void installFreshBlog() {
        App.main(createApp(new String[]{
            "install",
            "-l", tmp.getRoot().getAbsolutePath()}));
    }

    @Test
    @Ignore
    public void updateExistingWithBackupsBlog() {
        App.main(createApp(new String[]{
            "install",
            "-l", tmp.getRoot().getAbsolutePath(),
            "-u"}));
    }

    @Test
    @Ignore
    public void updateExistingWithForceBlog() {
        App.main(createApp(new String[]{
            "install",
            "-l", tmp.getRoot().getAbsolutePath(),
            "-f"}));
    }
}
