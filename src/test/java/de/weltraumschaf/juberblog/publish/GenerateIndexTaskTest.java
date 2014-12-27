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
package de.weltraumschaf.juberblog.publish;

import de.weltraumschaf.juberblog.BaseTestCase;
import de.weltraumschaf.juberblog.core.Page;
import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import java.net.URI;
import java.util.Collection;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link GenerateIndexTask}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class GenerateIndexTaskTest extends BaseTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test(expected = NullPointerException.class)
    public void constructWithNullThrowsException() {
        new GenerateIndexTask(null);
    }

    @Test
    public void execute_noPages() throws Exception {
        final GenerateIndexTask sut = new GenerateIndexTask(new GenerateIndexTask.Config(
                ENCODING,
                tmp.getRoot().toPath(),
                createPath(SCAFOLD_PACKAGE_PREFIX + "layout.ftl"),
                createPath(SCAFOLD_PACKAGE_PREFIX + "index.ftl"),
                "name",
                "description"));

        sut.execute();

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.HTML)
                .find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(1));
        final DataFile expectedFile = new DataFile(tmp.getRoot().toString() + "/index.html");
        assertThat(foundFiles, containsInAnyOrder(expectedFile));
        assertThat(expectedFile.readContent(ENCODING), is(
                "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <body>\n"
                + "        <h1>name</h1>\n"
                + "        <h2>description</h2>\n"
                + "\n"
                + "        <h3>All Blog Posts</h3>\n"
                + "<ul>\n"
                + "    </ul>\n"
                + "\n"
                + "    </body>\n"
                + "</html>"));
    }

    @Test
    public void execute_twoPages() throws Exception {
        final GenerateIndexTask sut = new GenerateIndexTask(new GenerateIndexTask.Config(
                ENCODING,
                tmp.getRoot().toPath(),
                createPath(SCAFOLD_PACKAGE_PREFIX + "layout.ftl"),
                createPath(SCAFOLD_PACKAGE_PREFIX + "index.ftl"),
                "name",
                "description"));
        final Page.Pages pages = new Page.Pages();
        pages.add(new Page("title1", URI.create("http://www.myblog.com/link1"), "desc1", new DateTime("2014-11-29"), Page.Type.POST));
        pages.add(new Page("title2", URI.create("http://www.myblog.com/link2"), "desc2", new DateTime("2014-11-30"), Page.Type.POST));

        sut.execute(pages);

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.HTML)
                .find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(1));
        final DataFile expectedFile = new DataFile(tmp.getRoot().toString() + "/index.html");
        assertThat(foundFiles, containsInAnyOrder(expectedFile));
        assertThat(expectedFile.readContent(ENCODING), is(
                "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <body>\n"
                + "        <h1>name</h1>\n"
                + "        <h2>description</h2>\n"
                + "\n"
                + "        <h3>All Blog Posts</h3>\n"
                + "<ul>\n"
                + "        <li>\n"
                + "        <a href=\"http://www.myblog.com/link1\">title1</a>\n"
                + "        <span>(Sat, 29 Nov 2014 00:00:00 +0100)</span>\n"
                + "    </li>\n"
                + "    <li>\n"
                + "        <a href=\"http://www.myblog.com/link2\">title2</a>\n"
                + "        <span>(Sun, 30 Nov 2014 00:00:00 +0100)</span>\n"
                + "    </li>\n"
                + "</ul>\n"
                + "\n"
                + "    </body>\n"
                + "</html>"));
    }
}
