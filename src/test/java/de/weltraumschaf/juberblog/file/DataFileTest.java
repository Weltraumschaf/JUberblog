package de.weltraumschaf.juberblog.file;

import de.weltraumschaf.juberblog.BaseTestCase;
import java.io.IOException;
import java.net.URISyntaxException;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link DataFile}.
 *
 * @author Sven Strittmatter
 */
public class DataFileTest extends BaseTestCase {

    private final DataFile sut = new DataFile(
            createPath("posts/2014-05-30T21.29.20_This-is-the-First-Post.md").toString());

    public DataFileTest() throws URISyntaxException {
        super();
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withNullAsAbsoluteFileName() {
        new DataFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withEmptyAsAbsoluteFileName() {
        new DataFile("");
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(DataFile.class)
                .verify();
    }

    @Test
    public void toStringContainsAllProperties() {
        assertThat(sut.toString(), startsWith(
                "DataFile{"
                + "absoluteFileName=/"));
        assertThat(sut.toString(), endsWith(
                "/de/weltraumschaf/juberblog/posts/2014-05-30T21.29.20_This-is-the-First-Post.md"
                + "}"));
    }

    @Test
    public void getPath() {
        assertThat(
                sut.getPath().toString(),
                endsWith("de/weltraumschaf/juberblog/posts/2014-05-30T21.29.20_This-is-the-First-Post.md"));
    }

    @Test
    public void getBaseName() {
        assertThat(
                sut.getBaseName(),
                is("2014-05-30T21.29.20_This-is-the-First-Post.md"));
        assertThat(new DataFile("/foo/bar/baz.md").getBaseName(), is("baz.md"));
    }

    @Test
    public void getBareName() {
        assertThat(sut.getBareName(), is("This-is-the-First-Post"));
        assertThat(new DataFile("/foo/bar/baz.md").getBareName(), is("baz"));
    }

    @Test
    public void getCreationDate() {
        assertThat(sut.getCreationDate(), is(new DateTime("2014-05-30T21:29:20")));
    }

    @Test
    public void getCreationDate_noDateAvailable() {
        assertThat(new DataFile("/foo/bar/baz.md").getCreationDate(), is(notNullValue()));
    }

    @Test
    public void readContent() throws IOException {
        assertThat(
                sut.readContent(ENCODING),
                is("<?juberblog\n"
                        + "    Description: This is the first post.\n"
                        + "?>\n"
                        + "\n"
                        + "### This is the First Post\n"
                        + "\n"
                        + "Lorem ipsum  dolor sit amet consetetur  sadipscing elitr sed diam  nonumy eirmod\n"
                        + "tempor invidunt ut labore et dolore magna aliquyam.\n"
                        + "\n"
                        + "Lorem ipsum  dolor sit amet consetetur  sadipscing elitr sed diam  nonumy eirmod\n"
                        + "tempor invidunt ut labore et dolore magna aliquyam.\n\n"));
    }
}
