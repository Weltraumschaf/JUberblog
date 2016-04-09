package de.weltraumschaf.juberblog.cmd.publish;

import de.weltraumschaf.juberblog.BaseTestCase;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.core.PageType;
import de.weltraumschaf.juberblog.core.Pages;
import de.weltraumschaf.juberblog.core.Verbose;
import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link GenerateIndexTask}.
 *
 * @author Sven Strittmatter
 */
public class GenerateIndexTaskTest extends BaseTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    private GenerateIndexTask.Config createTaskConfig() throws URISyntaxException, IOException {
        return new GenerateIndexTask.Config(
            createTemplates(),
            createDirs(tmp, false),
            createConfig(),
            createVersion()
        );
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructWithNullThrowsException() {
        new GenerateIndexTask(null, new Verbose(false, mock(PrintStream.class)));
    }

    @Test
    public void execute_noPages() throws Exception {
        final GenerateIndexTask sut = new GenerateIndexTask(createTaskConfig(), new Verbose(false, mock(PrintStream.class)));

        sut.execute();

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.HTML)
            .find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(1));
        final DataFile expectedFile = new DataFile(tmp.getRoot().toString() + "/index.html");
        assertThat(foundFiles, containsInAnyOrder(expectedFile));
        assertThat(expectedFile.readContent(ENCODING), is(
            "<!DOCTYPE html>\n"
            + "<html lang=\"en\">\n"
            + "    <head>\n"
            + "        <meta name=\"robots\" content=\"all\"/>\n"
            + "        <meta name=\"description\" content=\"Blog Description\"/>\n"
            + "        <meta name=\"keywords\" content=\"\"/>\n"
            + "        <meta charset=\"utf-8\"/>\n"
            + "        <link href=\"http://www.myblog.com//css/main.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\"/>\n"
            + "        <link href=\"http://www.myblog.com//img/favicon.ico\" rel=\"shortcut icon\" type=\"image/x-icon\"/>\n"
            + "    </head>\n"
            + "\n"
            + "    <body>\n"
            + "        <header>\n"
            + "            <h1>Blog Title</h1>\n"
            + "            <h2>Blog Description</h2>\n"
            + "        </header>\n"
            + "\n"
            + "        <section>\n"
            + "        <h3>All Blog Posts</h3>\n"
            + "<ul>\n"
            + "    </ul>\n"
            + "\n"
            + "        </section>\n"
            + "\n"
            + "        <footer>\n"
            + "            Powered by JUberblog.\n"
            + "        </footer>\n"
            + "\n"
            + "        <script type=\"text/javascript\" src=\"http://www.myblog.com//js/main.js\"></script>\n"
            + "    </body>\n"
            + "</html>"));
    }

    @Test
    public void execute_twoPages() throws Exception {
        final GenerateIndexTask sut = new GenerateIndexTask(createTaskConfig(), new Verbose(false, mock(PrintStream.class)));
        final Pages pages = new Pages();
        pages.add(new Page("title1", URI.create("http://www.myblog.com/link1"), "desc1", new DateTime("2014-11-29"), PageType.POST));
        pages.add(new Page("title2", URI.create("http://www.myblog.com/link2"), "desc2", new DateTime("2014-11-30"), PageType.POST));

        sut.execute(pages);

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.HTML)
            .find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(1));
        final DataFile expectedFile = new DataFile(tmp.getRoot().toString() + "/index.html");
        assertThat(foundFiles, containsInAnyOrder(expectedFile));
        assertThat(expectedFile.readContent(ENCODING), is(
            "<!DOCTYPE html>\n"
            + "<html lang=\"en\">\n"
            + "    <head>\n"
            + "        <meta name=\"robots\" content=\"all\"/>\n"
            + "        <meta name=\"description\" content=\"Blog Description\"/>\n"
            + "        <meta name=\"keywords\" content=\"\"/>\n"
            + "        <meta charset=\"utf-8\"/>\n"
            + "        <link href=\"http://www.myblog.com//css/main.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\"/>\n"
            + "        <link href=\"http://www.myblog.com//img/favicon.ico\" rel=\"shortcut icon\" type=\"image/x-icon\"/>\n"
            + "    </head>\n"
            + "\n"
            + "    <body>\n"
            + "        <header>\n"
            + "            <h1>Blog Title</h1>\n"
            + "            <h2>Blog Description</h2>\n"
            + "        </header>\n"
            + "\n"
            + "        <section>\n"
            + "        <h3>All Blog Posts</h3>\n"
            + "<ul>\n"
            + "        <li>\n"
            + "        <a href=\"http://www.myblog.com/link1\">title1</a>\n"
            + "        <!-- Date format see here http://freemarker.org/docs/ref_builtins_date.html -->\n"
            + "        <span>(29.11.2014, 00:00)</span>\n"
            + "    </li>\n"
            + "    <li>\n"
            + "        <a href=\"http://www.myblog.com/link2\">title2</a>\n"
            + "        <!-- Date format see here http://freemarker.org/docs/ref_builtins_date.html -->\n"
            + "        <span>(30.11.2014, 00:00)</span>\n"
            + "    </li>\n"
            + "</ul>\n"
            + "\n"
            + "        </section>\n"
            + "\n"
            + "        <footer>\n"
            + "            Powered by JUberblog.\n"
            + "        </footer>\n"
            + "\n"
            + "        <script type=\"text/javascript\" src=\"http://www.myblog.com//js/main.js\"></script>\n"
            + "    </body>\n"
            + "</html>"));
    }
}
