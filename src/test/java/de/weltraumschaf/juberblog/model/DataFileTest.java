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
package de.weltraumschaf.juberblog.model;

import de.weltraumschaf.juberblog.Constants;
import de.weltraumschaf.juberblog.model.DataFile.FileAttributes;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link DataFile}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class DataFileTest {

    private static final String FIXTURE_PACKAGE = Constants.PACKAGE_BASE.toString() + "/cmd/publish/posts";

    @Rule
    //CHECKSTYLE:OFF
    public final TemporaryFolder tmp = new TemporaryFolder();
    //CHECKSTYLE:ON

    @Test
    public void slugify() {
        final Path file = Paths.get("foo", "bar", "baz", "2014-05-30T21.34.20_This-is-the-First-Post.md");
        assertThat(file.toString(), is(equalTo("foo/bar/baz/2014-05-30T21.34.20_This-is-the-First-Post.md")));

        assertThat(
                DataFile.slugify(file),
                is(equalTo("This-is-the-First-Post")));
    }

    @Test
    public void from() throws URISyntaxException, IOException {
        final Path file = Paths.get(
                getClass().getResource(FIXTURE_PACKAGE + "/2014-05-30T21.29.20_This-is-the-First-Post.md").toURI());

        final DataFile data = DataFile.from(file, DataFile.Type.POST);
        final FileAttributes attributes = new FileAttributes(file);
        assertThat(data, is(not(nullValue())));
        assertThat(data.getBasename(), is(equalTo("2014-05-30T21.29.20_This-is-the-First-Post.md")));
        assertThat(data.getFilename(),
                is(endsWith("/de/weltraumschaf/juberblog/cmd/publish/posts/"
                        + "2014-05-30T21.29.20_This-is-the-First-Post.md")));
        assertThat(data.getHeadline(), is(equalTo("This is the First Post")));
        assertThat(data.getMarkdown(), is(equalTo("## This is the First Post\n\nLorem ipsum dolor sit amet "
                + "consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt\nut labore et dolore magna "
                + "aliquyam erat sed diam voluptua at vero eos et accusam et justo duo\ndolores et ea rebum stet "
                + "clita kasd gubergren no sea takimata sanctus est lorem ipsum dolor sit\namet.")));
        assertThat(data.getSlug(), is(equalTo("This-is-the-First-Post")));
        assertThat(data.getCreationTime(), is(attributes.creationTime()));
        assertThat(data.getModificationTime(), is(attributes.lastModifiedTime()));

        final MetaData meta = data.getMetaData();
        assertThat(meta, is(not(nullValue())));
        assertThat(meta.getDescription(), is(equalTo("This is the first post.")));
        assertThat(meta.getKeywords(), is(equalTo("first, post")));
        assertThat(meta.getNavi(), is(equalTo("")));
    }
}
