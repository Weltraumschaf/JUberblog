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

import static de.weltraumschaf.juberblog.JUberblogTestCase.ENCODING;
import de.weltraumschaf.juberblog.tasks.PublishTask;
import de.weltraumschaf.juberblog.tasks.Task;
import de.weltraumschaf.juberblog.tasks.TaskExecutor;
import java.util.Collection;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
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
                .append(new GenerateFeedTask())
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

        final Collection<DataFile> foundFiles = new FilesFinder(FileNameExtension.HTML).find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(5));
        final DataFile expectedOne = new DataFile(tmp.getRoot().toString() + "/This-is-the-First-Post.html");
        final DataFile expectedTwo = new DataFile(tmp.getRoot().toString() + "/This-is-the-Second-Post.html");
        final DataFile expectedThree = new DataFile(tmp.getRoot().toString() + "/This-is-the-Third-Post.html");
        final DataFile expectedFour = new DataFile(tmp.getRoot().toString() + "/Site-One.html");
        final DataFile expectedFive = new DataFile(tmp.getRoot().toString() + "/Site-Two.html");
        assertThat(foundFiles, containsInAnyOrder(expectedOne, expectedTwo, expectedThree, expectedFour, expectedFive));
    }

    class GenerateIndexTask implements Task<Void> {

        @Override
        public Void execute() throws Exception {
            return null;
        }
    }

    class GenerateSitemapTask implements Task<Void> {

        @Override
        public Void execute() throws Exception {
            return null;
        }
    }

    class GenerateFeedTask implements Task<Void> {

        @Override
        public Void execute() throws Exception {
            return null;
        }
    }
}
