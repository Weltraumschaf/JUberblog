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
package de.weltraumschaf.juberblog;

import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FilesFinderByExtension;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.tasks.GenerateFeedTask;
import de.weltraumschaf.juberblog.tasks.PublishTask;
import de.weltraumschaf.juberblog.tasks.Task;
import de.weltraumschaf.juberblog.tasks.TaskExecutor;
import java.util.Collection;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link App}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AppTest extends JUberblogTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void publishing() throws Exception {
        final TaskExecutor executor = new TaskExecutor();
        executor.append(new PublishTask(new PublishTask.Config(
                ENCODING,
                createPath("posts"),
                tmp.getRoot().toPath(),
                createPath("layout.ftl"),
                createPath("post.ftl")
        )))
                .append(new GenerateFeedTask(new GenerateFeedTask.Config(
                                        createPath("feed.ftl"),
                                        tmp.getRoot().toPath(),
                                        ENCODING,
                                        "title",
                                        "link",
                                        "description",
                                        "language",
                                        new DateTime()
                                )))
                .append(new PublishTask(new PublishTask.Config(
                                        ENCODING,
                                        createPath("sites"),
                                        tmp.getRoot().toPath(),
                                        createPath("layout.ftl"),
                                        createPath("site.ftl")
                                )))
                .append(new GenerateIndexTask())
                .append(new GenerateSitemapTask())
                .execute();

        final Collection<DataFile> foundFiles = new FilesFinderByExtension(FileNameExtension.HTML, FileNameExtension.XML)
                .find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(6));
        assertThat(
                foundFiles,
                containsInAnyOrder(
                        new DataFile(tmp.getRoot().toString() + "/This-is-the-First-Post.html"),
                        new DataFile(tmp.getRoot().toString() + "/This-is-the-Second-Post.html"),
                        new DataFile(tmp.getRoot().toString() + "/This-is-the-Third-Post.html"),
                        new DataFile(tmp.getRoot().toString() + "/Site-One.html"),
                        new DataFile(tmp.getRoot().toString() + "/Site-Two.html"),
                        new DataFile(tmp.getRoot().toString() + "/feed.xml")));
    }

    class GenerateIndexTask implements Task<Void, Void> {

        @Override
        public Void execute() throws Exception {
            // TODO Implement index generation.
            return null;
        }

        @Override
        public Void execute(Void previusResult) throws Exception {
            return null;
        }

        @Override
        public Class<Void> getDesiredTypeForPreviusResult() {
            return Void.class;
        }
    }

    class GenerateSitemapTask implements Task<Void, Void> {

        @Override
        public Void execute() throws Exception {
            // TODO Implement site map generation.
            return null;
        }

        @Override
        public Void execute(Void previusResult) throws Exception {
            return null;
        }

        @Override
        public Class<Void> getDesiredTypeForPreviusResult() {
            return Void.class;
        }
    }

}
