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
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.joda.time.DateTime;
import org.json.JSONException;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Tests for {@link PublishedPages}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PublishedPagesTest {

    @Rule
    // CHECKSTYLE:OFF
    public final TemporaryFolder tmp = new TemporaryFolder();
    // CHECKSTYLE:ON
    @Rule
    // CHECKSTYLE:OFF
    public final ExpectedException thrown = ExpectedException.none();
    // CHECKSTYLE:ON

    @Test
    public void save_throwsExcpetionIfFileIsNull() throws IOException {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("file");

        PublishedPages.save(null, new PublishedPages());
    }

    @Test
    public void save_throwsExcpetionIfPagesIsNull() throws IOException {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("pages");

        PublishedPages.save(tmp.getRoot().toPath().resolve("published"), null);
    }

    @Test
    public void save_empty() throws IOException {
        final Path file = tmp.getRoot().toPath().resolve("published");
        PublishedPages.save(file, new PublishedPages());

        final String storedData = new String(Files.readAllBytes(file), Constants.DEFAULT_ENCODING.toString());
        assertThat(storedData, is(equalTo("{\"data\":{}}")));
    }

    @Test
//    @Ignore("Does not work.")
    public void save() throws IOException, JSONException {
        final Path file = tmp.getRoot().toPath().resolve("published");
        final Path dataFile = tmp.newFile().toPath();
        final String data = "<?juberblog\n"
            + "    Navi: Projects\n"
            + "    Description: My projects.\n"
            + "    Keywords: projects\n"
            + "?>\n"
            + "\n"
            + "## Projects\n"
            + "\n"
            + "Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt\n"
            + "ut labore et dolore magna aliquyam erat sed diam voluptua at vero eos et accusam et justo duo\n"
            + "dolores et ea rebum stet clita kasd gubergren no sea takimata sanctus est lorem ipsum dolor sit\n"
            + "amet.";
        Files.write(dataFile, data.getBytes());
        final PublishedPages pages = new PublishedPages();
        pages.put(
            new Page(
                "title",
                URI.create("www.foo.com"),
                "description",
                new DateTime(123456789L),
                DataFile.from(dataFile),
                SiteMapUrl.ChangeFrequency.ALWAYS,
                SiteMapUrl.Priority.POST));
        PublishedPages.save(file, pages);
        final FileAttributes attributes = new FileAttributes(file);

        final String storedData = new String(Files.readAllBytes(file), Constants.DEFAULT_ENCODING.toString());
        assertThat(storedData, is(not(equalTo(""))));
        JSONAssert.assertEquals(
            "{\"data\":{"
            + "\"" + dataFile.toString() + "\":{"
                + "\"title\":\"title\","
                + "\"uri\":\"www.foo.com\","
                + "\"description\":\"description\","
                + "\"publishingDate\":\"1970-01-02T11:17:36.789+01:00\","
                + "\"file\":{"
                    + "\"fileName\":\"" + dataFile.toString() + "\","
                    + "\"baseName\":\"" + dataFile.getFileName().toString() + "\","
                    + "\"creationTime\":" + String.valueOf(attributes.creationTime()) + ","
                    + "\"modificationTime\":" + String.valueOf(attributes.lastModifiedTime()) + ","
                    + "\"slug\":\"" + DataFile.slugify(dataFile) + "\","
                    + "\"headline\":\"Projects\","
                    + "\"markdown\":\"## Projects\\n"
                        + "\\n"
                        + "Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt\\n"
                        + "ut labore et dolore magna aliquyam erat sed diam voluptua at vero eos et accusam et justo duo\\n"
                        + "dolores et ea rebum stet clita kasd gubergren no sea takimata sanctus est lorem ipsum dolor sit\\n"
                        + "amet.\","
                    + "\"metadata\":{"
                        + "\"data\":{"
                            + "\"Description\":\"My projects.\","
                            + "\"Navi\":\"Projects\","
                            + "\"Keywords\":\"projects\""
                        + "}"
                    + "}"
                + "},"
                + "\"frequencey\":\"ALWAYS\","
                + "\"priority\":\"POST\""
            + "}"
        + "}}",
        storedData, false);
    }

    @Test
    public void load() {
    }

}
