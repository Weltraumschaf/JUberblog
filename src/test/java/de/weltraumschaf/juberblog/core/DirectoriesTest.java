package de.weltraumschaf.juberblog.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link Directories}.
 *
 * @author Sven Strittmatter
 */
public class DirectoriesTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final Directories sut = new Directories(
        Paths.get("/blog/data"),
        Paths.get("/blog/public"));

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withNullAsDataDir() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("dataDir");

        new Directories(null, mock(Path.class));
    }

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withNullAsOutputDir() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("outputDir");

        new Directories(mock(Path.class), null);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Directories.class)
            .suppress(Warning.REFERENCE_EQUALITY)
            .verify();
    }

    @Test
    public void toStringContainsAllProperties() {
        assertThat(sut.toString(), is(
            "Directories{"
            + "postsData=/blog/data/posts, "
            + "sitesData=/blog/data/sites, "
            + "postsDraftData=/blog/data/drafts/posts, "
            + "sitesDraftData=/blog/data/drafts/sites, "
            + "output=/blog/public, "
            + "postsOutput=/blog/public/posts, "
            + "siteOutput=/blog/public/sites}"));
    }

    @Test
    public void getPostsData() {
        assertThat(sut.getPostsData().toString(), is("/blog/data/posts"));
    }

    @Test
    public void getSitesData() {
        assertThat(sut.getSitesData().toString(), is("/blog/data/sites"));
    }

    @Test
    public void getOutput() {
        assertThat(sut.getOutput().toString(), is("/blog/public"));
    }

    @Test
    public void getPostsOutput() {
        assertThat(sut.getPostsOutput().toString(), is("/blog/public/posts"));
    }

    @Test
    public void getSitesOutput() {
        assertThat(sut.getSitesOutput().toString(), is("/blog/public/sites"));
    }

    @Test
    public void getPostsDraftData() {
        assertThat(sut.getPostsDraftData().toString(), is("/blog/data/drafts/posts"));
    }

    @Test
    public void getSitesDraftData() {
        assertThat(sut.getSitesDraftData().toString(), is("/blog/data/drafts/sites"));
    }
}
