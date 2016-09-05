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
 * Tests for {@link Templates}.
 *
 * @author Sven Strittmatter
 */
public class TemplatesTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final Templates sut = new Templates(
        Paths.get("layoutTemplate"),
        Paths.get("postTemplate"),
        Paths.get("siteTemplate"),
        Paths.get("feedTemplate"),
        Paths.get("indexTemplate"),
        Paths.get("siteMapTemplate"),
        Paths.get("create/post_or_site")
    );

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withNullAsLayoutTemplate() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("layoutTemplate");

        new Templates(
            null,
            mock(Path.class),
            mock(Path.class),
            mock(Path.class),
            mock(Path.class),
            mock(Path.class),
            mock(Path.class));
    }

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withNullAsPostTemplate() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("postTemplate");

        new Templates(
            mock(Path.class),
            null,
            mock(Path.class),
            mock(Path.class),
            mock(Path.class),
            mock(Path.class),
            mock(Path.class));
    }

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withNullAsSiteTemplate() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("siteTemplate");

        new Templates(
            mock(Path.class),
            mock(Path.class),
            null,
            mock(Path.class),
            mock(Path.class),
            mock(Path.class),
            mock(Path.class));
    }

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withNullAsFeedTemplate() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("feedTemplate");

        new Templates(
            mock(Path.class),
            mock(Path.class),
            mock(Path.class),
            null,
            mock(Path.class),
            mock(Path.class),
            mock(Path.class));
    }

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withNullAsIndexTemplate() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("indexTemplate");

        new Templates(
            mock(Path.class),
            mock(Path.class),
            mock(Path.class),
            mock(Path.class),
            null,
            mock(Path.class),
            mock(Path.class));
    }

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withNullAsSiteMapTemplate() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("siteMapTemplate");

        new Templates(
            mock(Path.class),
            mock(Path.class),
            mock(Path.class),
            mock(Path.class),
            mock(Path.class),
            null,
            mock(Path.class));
    }

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void construct_withNullAsCreateSiteOrPostTemplate() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("createSiteOrPostTemplate");

        new Templates(
            mock(Path.class),
            mock(Path.class),
            mock(Path.class),
            mock(Path.class),
            mock(Path.class),
            mock(Path.class),
            null);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Templates.class)
            .suppress(Warning.REFERENCE_EQUALITY)
            .verify();
    }

    @Test
    public void toStringContainsAllProperties() {
        assertThat(sut.toString(), is(
            "Templates{"
            + "layoutTemplate=layoutTemplate, "
            + "postTemplate=postTemplate, "
            + "siteTemplate=siteTemplate, "
            + "feedTemplate=feedTemplate, "
            + "indexTemplate=indexTemplate, "
            + "siteMapTemplate=siteMapTemplate, "
            + "createSiteOrPostTemplate=create/post_or_site"
            + "}"));
    }

    @Test
    public void getLayoutTemplate() {
        assertThat(sut.getLayoutTemplate().toString(), is("layoutTemplate"));
    }

    @Test
    public void getPostTemplate() {
        assertThat(sut.getPostTemplate().toString(), is("postTemplate"));
    }

    @Test
    public void getSiteTemplate() {
        assertThat(sut.getSiteTemplate().toString(), is("siteTemplate"));
    }

    @Test
    public void getFeedTemplate() {
        assertThat(sut.getFeedTemplate().toString(), is("feedTemplate"));
    }

    @Test
    public void getIndexTemplate() {
        assertThat(sut.getIndexTemplate().toString(), is("indexTemplate"));
    }

    @Test
    public void getSiteMapTemplate() {
        assertThat(sut.getSiteMapTemplate().toString(), is("siteMapTemplate"));
    }

    @Test
    public void getCreateSiteOrPostTemplate() {
        assertThat(sut.getCreateSiteOrPostTemplate().toString(), is("create/post_or_site"));
    }

}
