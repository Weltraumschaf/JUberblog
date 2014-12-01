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
package de.weltraumschaf.juberblog.tasks;

import de.weltraumschaf.juberblog.file.DataFile;
import de.weltraumschaf.juberblog.file.FileNameExtension;
import de.weltraumschaf.juberblog.file.FilesFinder;
import de.weltraumschaf.juberblog.JUberblogTestCase;
import java.util.Collection;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link PublishTask}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PublishTaskTest extends JUberblogTestCase {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test(expected = NullPointerException.class)
    public void constructWithNullThrowsException() {
        new PublishTask(null);
    }

    @Test
    public void execute() throws Exception {
        final Task<Void> sut = new PublishTask(new PublishTask.Config(
                ENCODING,
                createPath("sites"),
                tmp.getRoot().toPath(),
                createPath("layout.ftl"),
                createPath("site.ftl")
        ));

        sut.execute();

        final Collection<DataFile> foundFiles = new FilesFinder(FileNameExtension.HTML).find(tmp.getRoot().toPath());
        assertThat(foundFiles.size(), is(2));
        final DataFile expectedOne = new DataFile(tmp.getRoot().toString() + "/Site-One.html");
        final DataFile expectedTwo = new DataFile(tmp.getRoot().toString() + "/Site-Two.html");
        assertThat(foundFiles, containsInAnyOrder(expectedOne, expectedTwo));
    }

}
